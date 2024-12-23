package com.traveladvisor.flightserver.service.domain.usecase.command;

import com.traveladvisor.common.domain.outbox.OutboxStatus;
import com.traveladvisor.common.domain.vo.BookingApprovalId;
import com.traveladvisor.common.domain.vo.FlightBookingApprovalStatus;
import com.traveladvisor.common.domain.vo.HotelBookingApprovalStatus;
import com.traveladvisor.flightserver.service.domain.FlightDomainService;
import com.traveladvisor.flightserver.service.domain.dto.command.FlightBookingCommand;
import com.traveladvisor.flightserver.service.domain.entity.BookingApproval;
import com.traveladvisor.flightserver.service.domain.entity.FlightOffer;
import com.traveladvisor.flightserver.service.domain.event.FlightBookingEvent;
import com.traveladvisor.flightserver.service.domain.exception.FlightNotFoundException;
import com.traveladvisor.flightserver.service.domain.mapper.BookingApprovalMapper;
import com.traveladvisor.flightserver.service.domain.port.output.repository.BookingApprovalRepository;
import com.traveladvisor.flightserver.service.domain.port.output.repository.FlightOfferRepository;
import com.traveladvisor.flightserver.service.domain.usecase.outbox.BookingOutboxHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class CompleteFlightBookingHelper {

    private final FlightDomainService flightDomainService;
    private final FlightOfferRepository flightOfferRepository;

    private final BookingApprovalMapper bookingApprovalMapper;
    private final BookingApprovalRepository bookingApprovalRepository;
    private final BookingOutboxHelper bookingOutboxHelper;

    @Transactional
    public void completeFlightBooking(FlightBookingCommand flightBookingCommand) {
        // 이미 처리된 예약은 스킵합니다.
        if (isOutboxMessageProcessedFor(flightBookingCommand, FlightBookingApprovalStatus.COMPLETED)) {
            log.info("이미 예약 처리된 항공권 입니다. Saga Action ID: {}", flightBookingCommand.sagaActionId());
            return;
        }

        log.info("예약서 BookingID: {}에 대한 항공권 예약 처리를 시작합니다.", flightBookingCommand.bookingId());

        // 항공권 예약 승인서를 생성합니다.
        BookingApproval bookingApproval = bookingApprovalMapper.toBookingApproval(flightBookingCommand);

        // 예약 가능한 항공권 정보를 조회합니다.
        FlightOffer flightOffer = findFlightOfferById(Long.parseLong(flightBookingCommand.flightOfferId()));


        // ※ 프로덕션에서는 Amadeus OTA에 해당 Offer ID 로 항공권이 예약 가능한 상태인지 Feign Client로 Sync 호출하여 검증하고 반드시 통과했을 때에만 예약 성공 처리해야 합니다.


        // flightOffer가 유효한지 검증하고 예약 승인서 초기화 합니다.
        FlightBookingEvent flightBookingEvent = flightDomainService.initializeBookingApproval(
                bookingApproval.getBookingId(), flightOffer, new ArrayList<>());

        // 예약 상태를 저장합니다.
        bookingApprovalRepository.save(flightBookingEvent.getBookingApproval());

        /*
         * 이벤트 전달: Flight 서비스 ---FlightBookingEvent---> Booking 서비스
         * Outbox 상태를 STARTED로 설정하고 flight.booking_outbox 테이블에 FlightBookingEvent 이벤트와 함께 저장합니다.
         */
        saveBookingOutbox(flightBookingCommand, flightBookingEvent);
    }

    /**
     * 예약 가능 호텔 객실을 조회합니다.
     *
     * @param flightOfferId
     * @return FlightOffer
     */
    private FlightOffer findFlightOfferById(Long flightOfferId) {
        return flightOfferRepository.findById(flightOfferId)
                .orElseThrow(() -> {
                    log.error("해당 항공권이 존재하지 않습니다. flightOfferId: {}", flightOfferId);
                    return new FlightNotFoundException("해당 항공권이 존재하지 않습니다. flightOfferId: " + flightOfferId);
                });
    }

    @Transactional
    public void cancelFlightBooking(FlightBookingCommand flightBookingCommand) {
        // 이미 처리된 예약은 스킵합니다.
        if (isOutboxMessageProcessedFor(flightBookingCommand, FlightBookingApprovalStatus.CANCELLED)) {
            log.info("이미 예약 취소 처리된 항공권 입니다. Saga Action ID: {}", flightBookingCommand.sagaActionId());
            return;
        }

        log.info("예약서 BookingID: {}에 대한 항공권 예약 취소 처리를 시작합니다.", flightBookingCommand.bookingId());

        // 애그리거트 루트 FlightOffer 검색
        FlightOffer flightOffer = findFlightOfferById(Long.parseLong(flightBookingCommand.flightOfferId()));

        // 예약서 검색
        BookingApproval bookingApproval = bookingApprovalRepository.findById(
                        new BookingApprovalId(UUID.fromString(flightBookingCommand.bookingId())))
                .orElseThrow(() -> {
                    log.error("항공권 예약서를 찾을 수 없습니다. BookingApprovalId: {}", flightBookingCommand.bookingId());
                    return new FlightNotFoundException("항공권 예약서를 찾을 수 없습니다. BookingApprovalId: " +
                            flightBookingCommand.bookingId());
                });

        // FlightOffer가 유효한지 검증하고 예약 승인서 초기화 합니다.
        FlightBookingEvent flightBookingEvent = flightDomainService.cancelBookingApproval(
                bookingApproval.getBookingId(), flightOffer, new ArrayList<>());

        // 변경된 호텔 예약서 저장
        bookingApprovalRepository.save(flightBookingEvent.getBookingApproval());

        /*
         * 이벤트 전달: Hotel 서비스 ---HotelBookingEvent---> Booking 서비스
         * Outbox 상태를 STARTED로 설정하고 hotel.booking_outbox 테이블에 HotelBookingEvent 이벤트와 함께 저장합니다.
         */
        saveBookingOutbox(flightBookingCommand, flightBookingEvent);
    }

    /**
     * 이미 처리된 Outbox 메시지를 조회합니다.
     *
     * @param flightBookingCommand
     * @param flightBookingApprovalStatus
     * @return
     */
    private boolean isOutboxMessageProcessedFor(FlightBookingCommand flightBookingCommand,
                                                FlightBookingApprovalStatus flightBookingApprovalStatus) {

        return bookingOutboxHelper.findCompletedBookingOutboxMessage(
                UUID.fromString(flightBookingCommand.sagaActionId()),
                flightBookingApprovalStatus).isPresent();
    }

    /**
     * 호텔 예약 상태를 Outbox 메시지함에 저장합니다.
     *
     * @param flightBookingCommand
     * @param flightBookingEvent
     */
    private void saveBookingOutbox(FlightBookingCommand flightBookingCommand,
                                   FlightBookingEvent flightBookingEvent) {
        bookingOutboxHelper.saveBookingOutbox(
                bookingApprovalMapper.toFlightBookingCompletedEventPayload(flightBookingEvent),
                flightBookingEvent.getBookingApproval().getStatus(),
                OutboxStatus.STARTED,
                UUID.fromString(flightBookingCommand.sagaActionId()));
    }

}
