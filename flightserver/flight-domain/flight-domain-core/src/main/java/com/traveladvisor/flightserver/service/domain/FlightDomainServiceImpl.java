package com.traveladvisor.flightserver.service.domain;

import com.traveladvisor.common.domain.vo.BookingId;
import com.traveladvisor.common.domain.vo.FlightBookingApprovalStatus;
import com.traveladvisor.common.domain.vo.HotelBookingStatus;
import com.traveladvisor.flightserver.service.domain.entity.FlightOffer;
import com.traveladvisor.flightserver.service.domain.event.FlightBookingCompletedEvent;
import com.traveladvisor.flightserver.service.domain.event.FlightBookingEvent;
import com.traveladvisor.flightserver.service.domain.event.FlightBookingRejectedEvent;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static com.traveladvisor.common.domain.constant.common.DomainConstants.UTC;

@Slf4j
public class FlightDomainServiceImpl implements FlightDomainService {

    @Override
    public FlightBookingEvent initializeBookingApproval(BookingId bookingId,
                                                        FlightOffer flightOffer,
                                                        List<String> failureMessages) {
        // FlightOffer가 유효한지 검증합니다.
        flightOffer.validateFlightOffer(failureMessages);

        // 검증에 실패한 경우 이를 이벤트에 기록합니다.
        if (!failureMessages.isEmpty()) {
            log.info("항공권 예약에 실패했습니다. BookingID: {}", bookingId.getValue());

            // FlightOffer Aggregate Root 를 통해 BookingApproval을 초기화 합니다.
            // DDD 관점에서 FotelOffer가 BookingApproval 엔터티를 다루는 주체가 되도록 설계했기 때문에 하위 엔터티를 관리할 책임을 갖습니다.
            flightOffer.initializeBookingApproval(bookingId, FlightBookingApprovalStatus.FAILED);

            return new FlightBookingRejectedEvent(
                    flightOffer.getBookingApproval(),
                    failureMessages,
                    ZonedDateTime.now(ZoneId.of(UTC)));
        }

        // 검증에 성공한 경우 이를 이벤트에 기록합니다.
        log.info("항공권 예약을 정상적으로 마쳤습니다. BookingID: {}", bookingId.getValue());

        flightOffer.initializeBookingApproval(bookingId, FlightBookingApprovalStatus.COMPLETED);

        return new FlightBookingCompletedEvent(
                flightOffer.getBookingApproval(),
                failureMessages,
                ZonedDateTime.now(ZoneId.of(UTC)));
    }

}
