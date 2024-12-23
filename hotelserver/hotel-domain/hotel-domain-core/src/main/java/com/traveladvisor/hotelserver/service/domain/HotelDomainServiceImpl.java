package com.traveladvisor.hotelserver.service.domain;

import com.traveladvisor.common.domain.vo.BookingId;
import com.traveladvisor.common.domain.vo.HotelBookingApprovalStatus;
import com.traveladvisor.hotelserver.service.domain.entity.HotelOffer;
import com.traveladvisor.hotelserver.service.domain.event.HotelBookingCancelledEvent;
import com.traveladvisor.hotelserver.service.domain.event.HotelBookingCompletedEvent;
import com.traveladvisor.hotelserver.service.domain.event.HotelBookingEvent;
import com.traveladvisor.hotelserver.service.domain.event.HotelBookingFailedEvent;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static com.traveladvisor.common.domain.constant.common.DomainConstants.UTC;

@Slf4j
public class HotelDomainServiceImpl implements HotelDomainService {

    @Override
    public HotelBookingEvent initializeBookingApproval(BookingId bookingId,
                                                       HotelOffer hotelOffer,
                                                       List<String> failureMessages) {
        // HotelOffer가 유효한지 검증합니다.
        hotelOffer.validateHotelOffer(failureMessages);

        // 검증에 실패한 경우 이를 이벤트에 기록합니다.
        if (!failureMessages.isEmpty()) {
            log.info("호텔 예약에 실패했습니다. BookingID: {}", bookingId.getValue());

            // HotelOffer Aggregate Root 를 통해 BookingApproval을 초기화 합니다.
            // DDD 관점에서 HotelOffer가 BookingApproval 엔터티를 다루는 주체가 되도록 설계했기 때문에 하위 엔터티를 관리할 책임을 갖습니다.
            hotelOffer.initializeBookingApproval(bookingId, HotelBookingApprovalStatus.FAILED);

            // 호텔 예약 반려 이벤트를 반환합니다.
            return new HotelBookingFailedEvent(
                    hotelOffer.getBookingApproval(),
                    failureMessages,
                    ZonedDateTime.now(ZoneId.of(UTC)));
        }

        // 검증에 성공한 경우 이를 이벤트에 기록합니다.
        log.info("호텔 예약을 정상적으로 마쳤습니다. BookingID: {}", bookingId.getValue());

        hotelOffer.initializeBookingApproval(bookingId, HotelBookingApprovalStatus.COMPLETED);

        // 호텔 예약 완료 이벤트를 반환합니다.
        return new HotelBookingCompletedEvent(
                hotelOffer.getBookingApproval(),
                failureMessages,
                ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public HotelBookingEvent cancelBookingApproval(BookingId bookingId,
                                                   HotelOffer hotelOffer,
                                                   List<String> failureMessages) {
        // HotelOffer가 유효한지 검증합니다.
        hotelOffer.validateHotelOffer(failureMessages);

        // 검증에 실패한 경우 이를 이벤트에 기록합니다.
        if (!failureMessages.isEmpty()) {
            log.info("호텔 예약 취소에 실패했습니다. BookingID: {}", bookingId.getValue());

            // HotelOffer Aggregate Root 를 통해 BookingApproval을 수정 합니다.
            // DDD 관점에서 HotelOffer가 BookingApproval 엔터티를 다루는 주체가 되도록 설계했기 때문에 하위 엔터티를 관리할 책임을 갖습니다.
            hotelOffer.initializeBookingApproval(bookingId, HotelBookingApprovalStatus.FAILED);

            // 호텔 예약 취소 실패 이벤트를 반환합니다.
            return new HotelBookingFailedEvent(
                    hotelOffer.getBookingApproval(),
                    failureMessages,
                    ZonedDateTime.now(ZoneId.of(UTC)));
        }

        // 검증에 성공한 경우 이를 이벤트에 기록합니다.
        log.info("호텔 예약을 정상적으로 취소했습니다. BookingID: {}", bookingId.getValue());

        hotelOffer.initializeBookingApproval(bookingId, HotelBookingApprovalStatus.CANCELLED);

        // 호텔 예약 취소 완료 이벤트를 반환합니다.
        return new HotelBookingCancelledEvent(
                hotelOffer.getBookingApproval(),
                failureMessages,
                ZonedDateTime.now(ZoneId.of(UTC)));
    }

}
