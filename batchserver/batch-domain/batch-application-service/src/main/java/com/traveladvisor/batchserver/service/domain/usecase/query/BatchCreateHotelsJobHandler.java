package com.traveladvisor.batchserver.service.domain.usecase.query;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.traveladvisor.batchserver.service.domain.entity.Country;
import com.traveladvisor.batchserver.service.domain.exception.BatchApplicationServiceException;
import com.traveladvisor.batchserver.service.domain.mapper.HotelMapper;
import com.traveladvisor.batchserver.service.domain.port.output.client.OtaApiClient;
import com.traveladvisor.batchserver.service.domain.port.output.repository.CountryRepository;
import com.traveladvisor.batchserver.service.domain.port.output.repository.HotelRepository;
import com.traveladvisor.common.message.dto.query.amadeus.QueryAccessToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.client.HttpClientErrorException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableBatchProcessing
public class BatchCreateHotelsJobHandler {


    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    private final OtaApiClient otaApiClient;
    private final CountryRepository countryRepository;
    private final HotelRepository hotelRepository;

    private final ObjectMapper objectMapper;
    private final HotelMapper hotelMapper;


    /**
     * Job 1 정의입니다. 절차는 다음과 같습니다.
     *
     * Step 1-1. Access Token 발급
     */
    @Bean
    public Job createHotelsJob() {
        return new JobBuilder("createHotelsJob", jobRepository)
                .start(getAccessTokenStep())
                .next(createHotelsStep())
                .build();
    }

    /**
     * Step 1. OTA로부터 Access Token 을 발급받습니다.
     */
    @Bean
    public Step getAccessTokenStep() {
        return new StepBuilder("getAccessTokenStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    JobParameters jobParameters = chunkContext.getStepContext().getStepExecution().getJobExecution().getJobParameters();
                    String clientId     = jobParameters.getString("client_id");
                    String clientSecret = jobParameters.getString("client_secret");
                    String grantType    = jobParameters.getString("grant_type");
                    log.info("Client ID: {}, Client Secret: {}, Grant Type: {}", clientId, clientSecret, grantType);

                    String accessToken = getAccessToken(QueryAccessToken.of(clientId, clientSecret, grantType));
                    ExecutionContext jobContext = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
                    jobContext.put("accessToken", accessToken);

                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .allowStartIfComplete(true)
                .build();
    }

    /**
     * Job 2 정의입니다. 절차는 다음과 같습니다.
     *
     * Step 2-1. Countries 조회
     *      2-2. Hotels 생성
     *      2-3. Hotels 적재
     */
    @Bean
    public Step createHotelsStep() {
        return new StepBuilder("createHotelsStep", jobRepository)
                .<Country, List<LinkedHashMap<String, Object>>>chunk(10, transactionManager)
                .reader(countriesItemReader())
                .processor(hotelDataItemProcessor(null))
                .writer(hotelDataItemWriter())
                .build();
    }

    /**
     * Step 2-1. Countries를 조회합니다.
     */
    @Bean
    public ItemReader<Country> countriesItemReader() {
        List<Country> countries = countryRepository.findAll();
        return new ListItemReader<>(countries);
    }

    /**
     * Step 2-2. Processor: 각 Country별로 OTA API 호출해 호텔 데이터를 획득합니다.
     *
     * @StepScope를 이용해 jobExecutionContext에서 accessToken을 주입받습니다.
     */
    @Bean
    @StepScope
    public ItemProcessor<Country, List<LinkedHashMap<String, Object>>> hotelDataItemProcessor(
            @Value("#{jobExecutionContext['accessToken']}") String accessToken) {
        return country -> {
            int attempt = 0;
            int maxAttempts = 5;
            long retryDelay = 60;

            while (attempt < maxAttempts) {
                try {
                    ResponseEntity<String> response = otaApiClient.getHotelsByCityCode(
                            "Bearer " + accessToken, country.getIataCode()); // 주의! City Code 가 아닌 IATA Code를 전달해야 합니다.

                    if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                        JsonNode hotelData = objectMapper.readTree(response.getBody());

                        JsonNode hotelDataArray = hotelData.get("data");
                        // 응답 데이터 내 'data' 필드가 존재 & 배열인지 확인합니다.
                        if (hotelDataArray != null && hotelDataArray.isArray()) {
                            return objectMapper.convertValue(hotelDataArray, List.class);
                        }

                        log.info("응답 JSON 데이터에 data 필드가 없거나 배열이 아닙니다. 응답 데이터: {}", hotelData);
                        return List.of();
                    } else {
                        throw new BatchApplicationServiceException("호텔 데이터 요청시 에러가 발생했습니다. 에러 메시지: "
                                + response.getBody().toString());
                    }
                } catch (HttpClientErrorException.TooManyRequests e) {
                    log.warn("Rate limit 도달: {}초 대기 후 재시도합니다.", retryDelay);
                    TimeUnit.SECONDS.sleep(retryDelay);
                    attempt    += 1;
                    retryDelay *= 2;
                }
            }

            log.error("API 요청 재시도 {}회 모두 실패했습니다. Country ISO code: {}", attempt, country.getIsoCode());

            return null;
        };
    }

    /**
     * Step 2-3. Hotels를 적재합니다.
     */
    @Bean
    public ItemWriter<List<LinkedHashMap<String, Object>>> hotelDataItemWriter() {
        return hotelsJsonList -> {
            for (List<LinkedHashMap<String, Object>> hotelJsonArray : hotelsJsonList) {
                for (LinkedHashMap<String, Object> hotelJson : hotelJsonArray) {
                    try {
                        LinkedHashMap<String, Object> hotelData = objectMapper.convertValue(hotelJson, LinkedHashMap.class);
                        log.info("저장 대상 Hotel Data: {}", hotelData);

                        var savedHotel = hotelRepository.save(hotelMapper.toDomain(hotelData));
                        hotelRepository.flush();

                        log.info("호텔 저장 성공: {}", savedHotel);
                    } catch (Exception e) {
                        log.error("Hotel 저장 중 오류 발생: {}", e.getMessage(), e);
                    }
                }
            }
        };
    }

    /**
     * OTA Access Token을 발급받습니다.
     */
    private String getAccessToken(QueryAccessToken queryAccessToken) {
        try {
            ResponseEntity<Map<String, Object>> response = otaApiClient.getAccessToken(queryAccessToken);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                String accessToken = response.getBody().get("access_token").toString();
                log.info("Access Token 발급 성공!");
                return accessToken;
            } else {
                throw new BatchApplicationServiceException("Access Token 발급에 실패했습니다. Status Code: " + response.getStatusCode());
            }
        } catch (Exception ex) {
            throw new BatchApplicationServiceException("Access Token 발급 도중 에러가 발생했습니다.", ex);
        }
    }

}
