package com.traveladvisor.bookingserver.service.domain;

import com.traveladvisor.bookingserver.service.domain.entity.Booking;
import com.traveladvisor.bookingserver.service.domain.event.BookingCreatedEvent;
import com.traveladvisor.bookingserver.service.domain.event.FlightBookedEvent;
import com.traveladvisor.bookingserver.service.domain.event.HotelBookedEvent;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static com.traveladvisor.common.domain.constant.common.DomainConstants.UTC;

@Slf4j
public class BookingDomainServiceImpl implements BookingDomainService {

    // 도메인 서비스에서 애그리거트 루트(Booking)를 사용해 일부 비즈니스 요구사항을 체크합니다. 이는 DDD에서 애그리거트 루트의 책임입니다.
    @Override
    public BookingCreatedEvent initializeBooking(Booking booking) {
        /*
         * 프로덕션에서는 호텔, 항공권, 차량 모두 예약 가능 기간이 지나지는 않았는지,
         * 객실이 활성 상태가 맞는지, 항공사가 EU에 제재를 받는 곳은 아닌지 등의 완전한 검증이 필요합니다.
         * 이 검증 단계는 사실 Hotel, Flight, Car 데이터를 호출한 시점에 각 마이크로서비스에서 담당해야 합니다.
         * 그리고 그 후 Booking(Saga Orchestrator) 서비스에서 비즈니스 니즈에 맞게 최종 검증을 수행해야 합니다.
         * 하지만 이 데모 프로젝트의 목적은 Saga + Outbox 패턴 구현과 쿠버네티스 인프라 구성에 무게를 두고 있기 때문에 비즈니스 검증은 간략하게 진행합니다.
         */
        booking.initializeBooking();
        log.info("호텔/항공권/차량 예약서 초기화를 완료했습니다. BookingId: {}", booking.getId().getValue());

        /*
         * 예약서에 대한 모든 검증을 마쳤으면(비즈니스 요구사항을 충족시켰으면) 예약서 생성됨 이벤트를 반환합니다. (*이벤트 이름은 과거형으로 만듭니다.)
         * 여기서 중요한 점은 도메인 코어 레이어는 도메인 이벤트를 이벤트를 직접 Publish 하는 것이 아닌, 이벤트 객체를 생성하기만 해야 한다는 점입니다.
         * 도메인 코어는 비즈니스 도메인에 대한 문제를 풀이하는 영역이며, 이를 위해서만 존재해야 합니다.
         * 이벤트 발행은 도메인 코어의 책임 밖의 일이며 이는 Application Service에서 담당합니다.
         */
        return new BookingCreatedEvent(booking, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    /**
     * 예약서의 예약 상태를 호텔 예약 완료로 업데이트 합니다.
     *
     * @param booking
     * @return
     */
    @Override
    public HotelBookedEvent markAsHotelBooked(Booking booking) {
        booking.markAsHotelBooked();

        return new HotelBookedEvent(booking, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    /**
     * 예약서의 예약 상태를 항공권 예약 완료로 업데이트 합니다.
     *
     * @param booking
     * @return
     */
    @Override
    public FlightBookedEvent markAsFlightBooked(Booking booking) {
        booking.markAsFlightBooked();

        return new FlightBookedEvent(booking, ZonedDateTime.now(ZoneId.of(UTC)));
    }

}
