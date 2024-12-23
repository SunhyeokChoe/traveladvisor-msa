package com.traveladvisor.carserver.service.domain;

import com.traveladvisor.carserver.service.domain.entity.CarOffer;
import com.traveladvisor.carserver.service.domain.event.CarBookingCompletedEvent;
import com.traveladvisor.carserver.service.domain.event.CarBookingEvent;
import com.traveladvisor.carserver.service.domain.event.CarBookingRejectedEvent;
import com.traveladvisor.common.domain.vo.BookingId;
import com.traveladvisor.common.domain.vo.CarBookingApprovalStatus;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static com.traveladvisor.common.domain.constant.common.DomainConstants.UTC;

@Slf4j
public class CarDomainServiceImpl implements CarDomainService {

    @Override
    public CarBookingEvent initializeBookingApproval(BookingId bookingId,
                                                     CarOffer carOffer,
                                                     List<String> failureMessages) {
        // CarOffer가 유효한지 검증합니다.
        carOffer.validateCarOffer(failureMessages);

        // 검증에 실패한 경우 이를 이벤트에 기록합니다.
        if (!failureMessages.isEmpty()) {
            log.info("차량 예약에 실패했습니다. BookingID: {}", bookingId.getValue());

            // CarOffer Aggregate Root 를 통해 BookingApproval을 초기화 합니다.
            // DDD 관점에서 CarOffer가 BookingApproval 엔터티를 다루는 주체가 되도록 설계했기 때문에 하위 엔터티를 관리할 책임을 갖습니다.
            carOffer.initializeBookingApproval(bookingId, CarBookingApprovalStatus.FAILED);

            return new CarBookingRejectedEvent(
                    carOffer.getBookingApproval(),
                    failureMessages,
                    ZonedDateTime.now(ZoneId.of(UTC)));
        }

        // 검증에 성공한 경우 이를 이벤트에 기록합니다.
        log.info("항공권 예약을 정상적으로 마쳤습니다. BookingID: {}", bookingId.getValue());

        carOffer.initializeBookingApproval(bookingId, CarBookingApprovalStatus.COMPLETED);

        return new CarBookingCompletedEvent(
                carOffer.getBookingApproval(),
                failureMessages,
                ZonedDateTime.now(ZoneId.of(UTC)));
    }

}
