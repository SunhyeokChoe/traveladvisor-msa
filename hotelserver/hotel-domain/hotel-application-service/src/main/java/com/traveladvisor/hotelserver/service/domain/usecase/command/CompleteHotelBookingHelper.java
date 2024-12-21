package com.traveladvisor.hotelserver.service.domain.usecase.command;

import com.traveladvisor.common.domain.outbox.OutboxStatus;
import com.traveladvisor.common.domain.vo.HotelBookingStatus;
import com.traveladvisor.hotelserver.service.domain.HotelDomainService;
import com.traveladvisor.hotelserver.service.domain.dto.command.CompleteHotelBookingCommand;
import com.traveladvisor.hotelserver.service.domain.entity.BookingApproval;
import com.traveladvisor.hotelserver.service.domain.entity.HotelOffer;
import com.traveladvisor.hotelserver.service.domain.event.BookingApprovalEvent;
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
    private final PaymentRepository paymentRepository;
    private final CreditEntryRepository creditEntryRepository;
    private final CreditHistoryRepository creditHistoryRepository;
    private final BookingOutboxHelper bookingOutboxHelper;

    @Transactional
    public void completeHotelBooking(CompleteHotelBookingCommand completeHotelBookingCommand) {
        // 이미 처리된 예약은 스킵합니다.
        if (isOutboxMessageProcessedFor(completeHotelBookingCommand, HotelBookingStatus.HOTEL_BOOKED)) {
            log.info("이미 처리된 호텔 예약 입니다. Saga Action ID: {}", completeHotelBookingCommand.sagaActionId());
            return;
        }

        log.info("예약서 BookingID: {}에 대한 호텔 예약 요청 처리를 시작합니다.", completeHotelBookingCommand.bookingId());

        // 예약서를 생성합니다.
        BookingApproval bookingApproval = bookingApprovalMapper.toBookingApproval(completeHotelBookingCommand);

        // 예약 가능한 호텔 객실 정보를 조회합니다.
        HotelOffer hotelOffer = queryHotelOffer(Long.parseLong(completeHotelBookingCommand.hotelOfferId()));

        // hotelOffer가 유효한지 검증하고 예약 승인서 초기화 합니다.
        BookingApprovalEvent bookingApprovalEvent = hotelDomainService.initializeBookingApproval(
                bookingApproval.getBookingId(), hotelOffer, new ArrayList<>());

        // 예약 상태를 저장합니다.
        bookingApprovalRepository.save(bookingApprovalEvent.getBookingApproval());

        // TODO: payment 서비스에 Feign Client로 결제 요청을 보냅니다. 만약 포인트가 존재하는 경우 포인트를 차감하도록 합니다.

        /*
         * 이벤트 전달: Hotel 서비스 ---HotelBookingCompletedEvent---> Booking 서비스
         * 예약 Saga Action 중 두 번째 단계이므로 Saga Action 상태를 PROCESSING으로, Outbox 상태를 STARTED로 설정해
         * hotel.booking_outbox 테이블에 HotelBookingCompletedEvent 이벤트와 함께 저장합니다.
         */
        saveBookingOutbox(completeHotelBookingCommand, bookingApprovalEvent);
    }

    /**
     * 예약 가능 호텔 객실을 조회합니다.
     *
     * @param hotelOfferId
     * @return HotelOffer
     */
    private HotelOffer queryHotelOffer(Long hotelOfferId) {
        return hotelOfferRepository.findById(hotelOfferId)
                .orElseThrow(() -> {
                    log.error("해당 호텔 객실이 존재하지 않습니다. hotelOfferId: {}", hotelOfferId);
                    return new HotelNotFoundException("해당 호텔 객실이 존재하지 않습니다.");
                });
    }

    @Transactional
    public void cancelHotelBooking(CompleteHotelBookingCommand completeHotelBookingCommand) {
        return;
    }

    /**
     * 이미 처리된 Outbox 메시지를 조회합니다.
     *
     * @param completeHotelBookingCommand
     * @param hotelBookingStatus
     * @return
     */
    private boolean isOutboxMessageProcessedFor(
            CompleteHotelBookingCommand completeHotelBookingCommand,
            HotelBookingStatus hotelBookingStatus) {

        return bookingOutboxHelper.queryCompletedBookingOutboxMessage(
                UUID.fromString(completeHotelBookingCommand.sagaActionId()),
                hotelBookingStatus).isPresent();
    }

    /**
     * 호텔 예약 상태를 Outbox 메시지함에 저장합니다.
     *
     * @param completeHotelBookingCommand
     * @param bookingApprovalEvent
     */
    private void saveBookingOutbox(CompleteHotelBookingCommand completeHotelBookingCommand, BookingApprovalEvent bookingApprovalEvent) {
        bookingOutboxHelper.saveBookingOutbox(
                bookingApprovalMapper.toHotelBookingCompletedEventPayload(bookingApprovalEvent),
                bookingApprovalEvent.getBookingApproval().getStatus(),
                OutboxStatus.STARTED,
                UUID.fromString(completeHotelBookingCommand.sagaActionId()));
    }

}
