package com.traveladvisor.hotelserver.service.domain.usecase.command;

import com.traveladvisor.common.domain.outbox.OutboxStatus;
import com.traveladvisor.common.domain.vo.BookingApprovalId;
import com.traveladvisor.common.domain.vo.HotelBookingApprovalStatus;
import com.traveladvisor.hotelserver.service.domain.HotelDomainService;
import com.traveladvisor.hotelserver.service.domain.dto.command.HotelBookingCommand;
import com.traveladvisor.hotelserver.service.domain.entity.BookingApproval;
import com.traveladvisor.hotelserver.service.domain.entity.HotelOffer;
import com.traveladvisor.hotelserver.service.domain.event.HotelBookingEvent;
import com.traveladvisor.hotelserver.service.domain.exception.HotelNotFoundException;
import com.traveladvisor.hotelserver.service.domain.mapper.BookingApprovalMapper;
import com.traveladvisor.hotelserver.service.domain.port.output.repository.BookingApprovalRepository;
import com.traveladvisor.hotelserver.service.domain.port.output.repository.HotelOfferRepository;
import com.traveladvisor.hotelserver.service.domain.usecase.outbox.BookingOutboxHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class CompleteHotelBookingHelper {

    private final HotelDomainService hotelDomainService;
    private final HotelOfferRepository hotelOfferRepository;
    private final BookingApprovalMapper bookingApprovalMapper;
    private final BookingApprovalRepository bookingApprovalRepository;
    private final BookingOutboxHelper bookingOutboxHelper;

    @Transactional
    public void completeHotelBooking(HotelBookingCommand hotelBookingCommand) {
        // 이미 처리된 예약은 스킵합니다.
        if (isOutboxMessageProcessedFor(hotelBookingCommand, HotelBookingApprovalStatus.COMPLETED)) {
            log.info("이미 예약 처리된 호텔 객실 입니다. Saga Action ID: {}", hotelBookingCommand.sagaActionId());
            return;
        }

        log.info("예약서 BookingID: {}에 대한 호텔 객실 예약 요청 처리를 시작합니다.", hotelBookingCommand.bookingId());

        // 호텔 객실 예약 승인서를 생성합니다.
        BookingApproval bookingApproval = bookingApprovalMapper.toBookingApproval(hotelBookingCommand);

        // 예약 가능한 호텔 객실 정보를 조회합니다.
        HotelOffer hotelOffer = findHotelOfferById(Long.parseLong(hotelBookingCommand.hotelOfferId()));


        // ※ 프로덕션에서는 Amadeus OTA에 해당 Offer ID 로 객실이 예약 가능한 상태인지 Feign Client로 Sync 호출하여 검증하고 반드시 통과했을 때에만 예약 성공 처리해야 합니다.


        // hotelOffer가 유효한지 검증하고 예약 승인서 초기화 합니다.
        HotelBookingEvent hotelBookingEvent = hotelDomainService.initializeBookingApproval(
                bookingApproval.getBookingId(), hotelOffer, new ArrayList<>());

        // 예약 상태를 저장합니다.
        bookingApprovalRepository.save(hotelBookingEvent.getBookingApproval());

        // TODO: payment 서비스에 Feign Client로 결제 요청을 보냅니다. 만약 포인트가 존재하는 경우 포인트를 차감하도록 합니다.

        /*
         * 이벤트 전달: Hotel 서비스 ---HotelBookingEvent---> Booking 서비스
         * Outbox 상태를 STARTED로 설정하고 hotel.booking_outbox 테이블에 HotelBookingEvent 이벤트와 함께 저장합니다.
         */
        saveBookingOutbox(hotelBookingCommand, hotelBookingEvent);
    }

    /**
     * 예약 가능 호텔 객실을 조회합니다.
     *
     * @param hotelOfferId
     * @return HotelOffer
     */
    private HotelOffer findHotelOfferById(Long hotelOfferId) {
        return hotelOfferRepository.findById(hotelOfferId)
                .orElseThrow(() -> {
                    log.error("해당 호텔 객실이 존재하지 않습니다. hotelOfferId: {}", hotelOfferId);
                    return new HotelNotFoundException("해당 호텔 객실이 존재하지 않습니다. hotelOfferId: " + hotelOfferId);
                });
    }

    @Transactional
    public void cancelHotelBooking(HotelBookingCommand hotelBookingCommand) {
        // 이미 처리된 예약은 스킵합니다.
        if (isOutboxMessageProcessedFor(hotelBookingCommand, HotelBookingApprovalStatus.CANCELLED)) {
            log.info("이미 예약 취소 처리된 호텔 객실 입니다. Saga Action ID: {}", hotelBookingCommand.sagaActionId());
            return;
        }

        log.info("예약서 BookingID: {}에 대한 호텔 객실 예약 취소 처리를 시작합니다.", hotelBookingCommand.bookingId());

        // 애그리거트 루트 HotelOffer 검색
        HotelOffer hotelOffer = findHotelOfferById(Long.parseLong(hotelBookingCommand.hotelOfferId()));

        // 예약서 검색
        BookingApproval bookingApproval = bookingApprovalRepository.findById(
                        new BookingApprovalId(UUID.fromString(hotelBookingCommand.bookingId())))
                .orElseThrow(() -> {
                    log.error("호텔 예약서를 찾을 수 없습니다. BookingApprovalId: {}", hotelBookingCommand.bookingId());
                    return new HotelNotFoundException("호텔 예약서를 찾을 수 없습니다. BookingApprovalId: " +
                            hotelBookingCommand.bookingId());
                });

        // HotelOffer가 유효한지 검증하고 예약 승인서 초기화 합니다.
        HotelBookingEvent hotelBookingEvent = hotelDomainService.cancelBookingApproval(
                bookingApproval.getBookingId(), hotelOffer, new ArrayList<>());
        
        // 변경된 호텔 예약서 저장
        bookingApprovalRepository.save(hotelBookingEvent.getBookingApproval());

        /*
         * 이벤트 전달: Hotel 서비스 ---HotelBookingEvent---> Booking 서비스
         * Outbox 상태를 STARTED로 설정하고 hotel.booking_outbox 테이블에 HotelBookingEvent 이벤트와 함께 저장합니다.
         */
        saveBookingOutbox(hotelBookingCommand, hotelBookingEvent);
    }

    /**
     * 이미 처리된 Outbox 메시지를 조회합니다.
     *
     * @param hotelBookingCommand
     * @param hotelBookingApprovalStatus
     * @return
     */
    private boolean isOutboxMessageProcessedFor(HotelBookingCommand hotelBookingCommand,
                                                HotelBookingApprovalStatus hotelBookingApprovalStatus) {

        return bookingOutboxHelper.findCompletedBookingOutboxMessage(
                UUID.fromString(hotelBookingCommand.sagaActionId()),
                hotelBookingApprovalStatus).isPresent();
    }

    /**
     * 호텔 예약 상태를 Outbox 메시지함에 저장합니다.
     *
     * @param hotelBookingCommand
     * @param hotelBookingEvent
     */
    private void saveBookingOutbox(HotelBookingCommand hotelBookingCommand,
                                   HotelBookingEvent hotelBookingEvent) {
        bookingOutboxHelper.saveBookingOutbox(
                bookingApprovalMapper.toHotelBookingCompletedEventPayload(hotelBookingEvent),
                hotelBookingEvent.getBookingApproval().getStatus(),
                OutboxStatus.STARTED, // Outbox 메시지함에 저장할 때에는 새로운 Outbox 시작이므로 STARTED 상태로 저장합니다.
                UUID.fromString(hotelBookingCommand.sagaActionId()));
    }

}
