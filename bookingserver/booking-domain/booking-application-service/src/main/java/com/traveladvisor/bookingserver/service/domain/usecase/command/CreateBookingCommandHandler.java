package com.traveladvisor.bookingserver.service.domain.usecase.command;

import com.traveladvisor.bookingserver.service.domain.BookingDomainService;
import com.traveladvisor.bookingserver.service.domain.dto.command.CreateBookingCommand;
import com.traveladvisor.bookingserver.service.domain.dto.command.CreateBookingResponse;
import com.traveladvisor.bookingserver.service.domain.dto.query.QueryCarOffersResponse;
import com.traveladvisor.bookingserver.service.domain.dto.query.QueryFlightOffersResponse;
import com.traveladvisor.bookingserver.service.domain.dto.query.QueryHotelOffersResponse;
import com.traveladvisor.bookingserver.service.domain.dto.query.QueryMemberResponse;
import com.traveladvisor.bookingserver.service.domain.entity.Booking;
import com.traveladvisor.bookingserver.service.domain.event.BookingCreatedEvent;
import com.traveladvisor.bookingserver.service.domain.exception.BookingApplicationServiceException;
import com.traveladvisor.bookingserver.service.domain.mapper.BookingMapper;
import com.traveladvisor.bookingserver.service.domain.port.output.client.CarServiceApiClient;
import com.traveladvisor.bookingserver.service.domain.port.output.client.FlightServiceApiClient;
import com.traveladvisor.bookingserver.service.domain.port.output.client.HotelServiceApiClient;
import com.traveladvisor.bookingserver.service.domain.port.output.client.MemberServiceApiClient;
import com.traveladvisor.bookingserver.service.domain.port.output.repository.BookingRepository;
import com.traveladvisor.bookingserver.service.domain.usecase.outbox.HotelOutboxHelper;
import com.traveladvisor.bookingserver.service.domain.usecase.saga.BookingSagaActionHelper;
import com.traveladvisor.common.domain.outbox.OutboxStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class CreateBookingCommandHandler {

    private final BookingDomainService bookingDomainService;
    private final BookingSagaActionHelper bookingSagaActionHelper;
    private final HotelOutboxHelper hotelOutboxHelper;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    private final MemberServiceApiClient memberServiceApiClient;

    private final HotelServiceApiClient hotelServiceApiClient;

    private final FlightServiceApiClient flightServiceApiClient;

    private final CarServiceApiClient carServiceApiClient;

    @Transactional
    public CreateBookingResponse createBooking(CreateBookingCommand createBookingCommand, String correlationId) {
        // 가입된 회원인지 확인합니다.
        validateMemberIsExists(createBookingCommand.email(), correlationId);

        // TODO: 호텔/항공권/차량 데이터 요청 메서드에서 응답을 전달받음과 동시에 각 도메인 객체로 매핑해 반환하고, 이곳에서 전달받아 initializeBooking 에
        //       Booking/HotelOffer/FlightOffer/CarOffer 데이터를 모두 넘기고 Booking 에서 검증해야 한다.
        // 예약 가능한 호텔인지 확인합니다.
        validateHotelOfferIsExists(createBookingCommand.hotelOfferId(), correlationId);
        // 예약 가능한 항공권인지 확인합니다.
        validateFlightOfferIsExists(createBookingCommand.flightOfferId(), correlationId);
        // 예약 가능한 차량인지 확인합니다.
        validateCarOfferIsExists(createBookingCommand.carOfferId(), correlationId);

        // 예약 도메인 코어 서비스를 사용해 예약서를 생성하고 영속화 합니다.
        BookingCreatedEvent bookingCreatedEvent = bookingDomainService.initializeBooking(
                bookingMapper.toBooking(createBookingCommand));
        Booking savedBooking = saveBooking(bookingCreatedEvent.getBooking());

        /*
         * 이벤트 전달: Booking 서비스 ---BookingCreatedEvent---> Hotel 서비스
         * Saga 시작 단계이므로 Saga Action, Outbox 상태를 STARTED로 지정해 hotel_outbox 테이블에 BookingCreatedEvent 이벤트와 함께 저장합니다.
         * Saga Action 상태는 BookingStatus에 따라 결정됩니다.
         */
        saveHotelOutbox(bookingCreatedEvent);

        return bookingMapper.toCreateBookingResponse(savedBooking, "호텔/항공권/차량 예약을 완료했습니다. 즐거운 여행되세요!");
    }

    private Booking saveBooking(Booking booking) {
        Booking savedBooking = bookingRepository.save(booking);
        if (savedBooking == null) {
            throw new BookingApplicationServiceException("예약서 저장에 실패했습니다.");
        }

        log.info("예약서를 정상적으로 저장했습니다. BookingId: {}", savedBooking.getId().getValue());
        return savedBooking;
    }

    /**
     * 회원이 존재하는지 확인합니다.
     *
     * @param email 회원 이메일
     * @return QueryMemberResponse
     */
    private QueryMemberResponse validateMemberIsExists(String email, String correlationId) {
        return Optional.ofNullable(memberServiceApiClient.queryMember(correlationId, email))
                .map(ResponseEntity::getBody)
                .orElseThrow(() -> new BookingApplicationServiceException("회원이 존재하지 않습니다."));
    }

    /**
     * 호텔 객실이 존재하는지 확인합니다.
     *
     * @param hotelOfferId
     * @return QueryHotelOffersResponse
     */
    private QueryHotelOffersResponse validateHotelOfferIsExists(Long hotelOfferId, String correlationId) {
        return Optional.ofNullable(hotelServiceApiClient.queryHotelOffer(correlationId, hotelOfferId))
                .map(ResponseEntity::getBody)
                .orElseThrow(() -> new BookingApplicationServiceException("해당 호텔 객실이 존재하지 않습니다."));
    }

    /**
     * 항공권이 존재하는지 확인합니다.
     *
     * @param flightOfferId
     * @return QueryFlightOffersResponse
     */
    private QueryFlightOffersResponse validateFlightOfferIsExists(Long flightOfferId, String correlationId) {
        return Optional.ofNullable(flightServiceApiClient.queryFlightOffer(correlationId, flightOfferId))
                .map(ResponseEntity::getBody)
                .orElseThrow(() -> new BookingApplicationServiceException("해당 항공권이 존재하지 않습니다."));
    }

    /**
     * 차량이 존재하는지 확인합니다.
     *
     * @param carOfferId
     * @return QueryCarOffersResponse
     */
    private QueryCarOffersResponse validateCarOfferIsExists(Long carOfferId, String correlationId) {
        return Optional.ofNullable(carServiceApiClient.queryCarOffer(correlationId, carOfferId))
                .map(ResponseEntity::getBody)
                .orElseThrow(() -> new BookingApplicationServiceException("해당 차량이 존재하지 않습니다."));
    }

    /**
     * CreateBooking Command 단계에서 HotelOutbox에 초기 Saga Action 상태로 이벤트를 저장합니다.
     *
     * @param bookingCreatedEvent
     */
    private void saveHotelOutbox(BookingCreatedEvent bookingCreatedEvent) {
        hotelOutboxHelper.save(
                bookingMapper.toBookingCreatedEventPayload(bookingCreatedEvent),
                bookingCreatedEvent.getBooking().getBookingStatus(),
                bookingSagaActionHelper.toSagaActionStatus(bookingCreatedEvent.getBooking().getBookingStatus()),
                OutboxStatus.STARTED,
                UUID.randomUUID()
        );
    }

}
