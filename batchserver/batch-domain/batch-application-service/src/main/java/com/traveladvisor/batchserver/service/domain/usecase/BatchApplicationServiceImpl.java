package com.traveladvisor.batchserver.service.domain.usecase;

import com.traveladvisor.batchserver.service.domain.dto.command.CreateHotelsJobCommand;
import com.traveladvisor.batchserver.service.domain.exception.BatchApplicationServiceException;
import com.traveladvisor.batchserver.service.domain.port.input.service.BatchApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@RequiredArgsConstructor
@Validated
@Service
public class BatchApplicationServiceImpl implements BatchApplicationService {

    private final JobLauncher jobLauncher;
    private final Job createHotelsJob;

    @Override
    public void createHotels(CreateHotelsJobCommand createHotelsJobCommand) {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("run.id", System.currentTimeMillis())
                .addString("client_id", createHotelsJobCommand.clientId())
                .addString("client_secret", createHotelsJobCommand.clientSecret())
                .addString("grant_type", createHotelsJobCommand.grantType())
                .toJobParameters();
        try {
            jobLauncher.run(createHotelsJob, jobParameters);
        } catch (Exception ex) {
            throw new BatchApplicationServiceException("테스트 호텔 데이터 생성 배치 잡 구동에 실패했습니다.", ex);
        }
    }

}
