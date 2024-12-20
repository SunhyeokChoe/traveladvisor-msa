package com.traveladvisor.common.datasource.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ISO-8601 지속 시간 표기법
 * ISO-8601의 지속 시간 형식은 P로 시작합니다.
 * 시간(T) 뒤에 H(시간), M(분), S(초)가 결합됩니다.
 *
 * 주요 패턴
 * P: 기간(Period) 시작
 * T: 시간(Time) 단위 시작
 * H: 시간(Hours)
 * M: 분(Minutes)
 * S: 초(Seconds)
 *
 * ISO-8601 형식	의미	설명
 * PT11H17M	      11시간 17분	         PT(시간), 11H, 17M
 * P1DT2H	      1일 2시간	             P(기간), 1D(일), T2H
 * PT3H15M30S	  3시간 15분 30초	     PT(시간), 3H, 15M, 30S
 * P2Y3M4DT5H6M7S 2년 3개월 4일 5:06:07	 복합적인 기간 표현
 */
@Converter(autoApply = true)
public class DurationToIntervalConverter implements AttributeConverter<Duration, String> {

    // 정규식: PostgreSQL INTERVAL 형식 또는 HH:mm:ss 형식
    private static final Pattern INTERVAL_PATTERN = Pattern.compile(
            "(?:(\\d+)\\s+years\\s+)?(?:(\\d+)\\s+mons\\s+)?(?:(\\d+)\\s+days\\s+)?(\\d+):(\\d+):(\\d+)(?:\\.(\\d+))?"
    );

    @Override
    public String convertToDatabaseColumn(Duration duration) {
        if (duration == null) {
            return null;
        }
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    @Override
    public Duration convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }

        Matcher matcher = INTERVAL_PATTERN.matcher(dbData);
        if (matcher.matches()) {
            long years = parseOrZero(matcher.group(1));
            long months = parseOrZero(matcher.group(2));
            long days = parseOrZero(matcher.group(3));
            long hours = Long.parseLong(matcher.group(4));
            long minutes = Long.parseLong(matcher.group(5));
            long seconds = Long.parseLong(matcher.group(6));

            // Duration은 시간 기준으로 계산하므로, years와 months를 days로 변환해 합산합니다.
            long totalDays = days + (years * 365) + (months * 30);
            return Duration.ofDays(totalDays).plusHours(hours).plusMinutes(minutes).plusSeconds(seconds);
        } else {
            throw new IllegalArgumentException("Invalid interval format: " + dbData);
        }
    }

    private long parseOrZero(String value) {
        return (value == null || value.isEmpty()) ? 0 : Long.parseLong(value);
    }

}
