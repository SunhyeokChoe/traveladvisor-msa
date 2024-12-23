package com.traveladvisor.carserver.service.domain.usecase.command;

import com.traveladvisor.carserver.service.domain.CarDomainService;
import com.traveladvisor.carserver.service.domain.dto.query.command.CarBookingCommand;
import com.traveladvisor.carserver.service.domain.entity.BookingApproval;
import com.traveladvisor.carserver.service.domain.entity.CarOffer;
import com.traveladvisor.carserver.service.domain.event.CarBookingEvent;
import com.traveladvisor.carserver.service.domain.exception.CarNotFoundException;
import com.traveladvisor.carserver.service.domain.mapper.BookingApprovalMapper;
import com.traveladvisor.carserver.service.domain.port.output.repository.BookingApprovalRepository;
import com.traveladvisor.carserver.service.domain.port.output.repository.CarOfferRepository;
import com.traveladvisor.carserver.service.domain.usecase.outbox.BookingOutboxHelper;
import com.traveladvisor.common.domain.outbox.OutboxStatus;
import com.traveladvisor.common.domain.vo.CarBookingApprovalStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class CompleteCarBookingHelper {

    private final CarDomainService carDomainService;
    private final CarOfferRepository carOfferRepository;

    private final BookingApprovalMapper bookingApprovalMapper;
    private final BookingApprovalRepository bookingApprovalRepository;
    private final BookingOutboxHelper bookingOutboxHelper;

    @Transactional
    public void completeCarBooking(CarBookingCommand carBookingCommand) {
        // 이미 처리된 예약은 스킵합니다.
        if (isOutboxMessageProcessedFor(carBookingCommand, CarBookingApprovalStatus.COMPLETED)) {
            log.info("이미 예약 처리된 차량 입니다. Saga Action ID: {}", carBookingCommand.sagaActionId());
            return;
        }

        log.info("예약서 BookingID: {}에 대한 차량 예약 처리를 시작합니다.", carBookingCommand.bookingId());

        // 항공권 예약 승인서를 생성합니다.
        BookingApproval bookingApproval = bookingApprovalMapper.toBookingApproval(carBookingCommand);

        // 예약 가능한 차량 정보를 조회합니다.
        CarOffer carOffer = findCarOfferById(Long.parseLong(carBookingCommand.carOfferId()));


        // ※ 프로덕션에서는 Amadeus OTA에 해당 Offer ID 로 차량이 예약 가능한 상태인지 Feign Client로 Sync 호출하여 검증하고 반드시 통과했을 때에만 예약 성공 처리해야 합니다.


        // flightOffer가 유효한지 검증하고 예약 승인서 초기화 합니다.
        CarBookingEvent carBookingEvent = carDomainService.initializeBookingApproval(
                bookingApproval.getBookingId(), carOffer, new ArrayList<>());

        // 예약 상태를 저장합니다.
        bookingApprovalRepository.save(carBookingEvent.getBookingApproval());

        /*
         * 이벤트 전달: Car 서비스 ---CarBookingEvent---> Booking 서비스
         * Outbox 상태를 STARTED로 설정하고 flight.booking_outbox 테이블에 CarBookingEvent 이벤트와 함께 저장합니다.
         */
        saveBookingOutbox(carBookingCommand, carBookingEvent);
    }

    /**
     * 예약 가능 호텔 객실을 조회합니다.
     *
     * @param carOfferId
     * @return CarOffer
     */
    private CarOffer findCarOfferById(Long carOfferId) {
        return carOfferRepository.findById(carOfferId)
                .orElseThrow(() -> {
                    log.error("해당 차량이 존재하지 않습니다. carOfferId: {}", carOfferId);
                    return new CarNotFoundException("해당 차량이 존재하지 않습니다. carOfferId: " + carOfferId);
                });
    }

    @Transactional
    public void cancelCarBooking(CarBookingCommand carBookingCommand) {
    }

    /**
     * 이미 처리된 Outbox 메시지를 조회합니다.
     *
     * @param carBookingCommand
     * @param carBookingApprovalStatus
     * @return
     */
    private boolean isOutboxMessageProcessedFor(CarBookingCommand carBookingCommand,
                                                CarBookingApprovalStatus carBookingApprovalStatus) {

        return bookingOutboxHelper.findCompletedBookingOutboxMessage(
                UUID.fromString(carBookingCommand.sagaActionId()),
                carBookingApprovalStatus).isPresent();
    }

    /**
     * 호텔 예약 상태를 Outbox 메시지함에 저장합니다.
     *
     * @param carBookingCommand
     * @param carBookingEvent
     */
    private void saveBookingOutbox(CarBookingCommand carBookingCommand,
                                   CarBookingEvent carBookingEvent) {
        bookingOutboxHelper.saveBookingOutbox(
                bookingApprovalMapper.toCarBookingCompletedEventPayload(carBookingEvent),
                carBookingEvent.getBookingApproval().getStatus(),
                OutboxStatus.STARTED,
                UUID.fromString(carBookingCommand.sagaActionId()));
    }

}
