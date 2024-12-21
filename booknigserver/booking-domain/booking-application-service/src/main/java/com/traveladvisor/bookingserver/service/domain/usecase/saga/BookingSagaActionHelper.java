package com.traveladvisor.bookingserver.service.domain.usecase.saga;

import com.traveladvisor.bookingserver.service.domain.entity.Booking;
import com.traveladvisor.bookingserver.service.domain.exception.BookingApplicationServiceException;
import com.traveladvisor.bookingserver.service.domain.exception.BookingNotFoundException;
import com.traveladvisor.bookingserver.service.domain.port.output.repository.BookingRepository;
import com.traveladvisor.common.domain.saga.SagaActionStatus;
import com.traveladvisor.common.domain.vo.BookingId;
import com.traveladvisor.common.domain.vo.BookingStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class BookingSagaActionHelper {

    private final BookingRepository bookingRepository;

    public Booking findOrder(BookingId bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> {
                    return new BookingNotFoundException("예약서를 찾을 수 없습니다. bookingId: " + bookingId);
                });
    }

    public void save(Booking booking) {
        bookingRepository.save(booking);
    }

    /**
     * 예약 상태에 따라 SagaActionStatus 값을 생성해 반환합니다.
     */
    public SagaActionStatus toSagaActionStatus(BookingStatus bookingStatus) {
        switch (bookingStatus) {
            case HOTEL_BOOKED:
            case FLIGHT_BOOKED:
            case CAR_BOOKED:
                return SagaActionStatus.PROCESSING; // 예약이 진행 중인 경우 Saga Action 상태는 PROCESSING
            case CANCELLING:
            case HOTEL_CANCELLED:
            case FLIGHT_CANCELLED:
            case CAR_CANCELLED:
                return SagaActionStatus.COMPENSATING; // 예약이 취소중일 경우 Saga Action 상태는 COMPENSATING(보상 진행 단계)
            case APPROVED:
                return SagaActionStatus.SUCCEEDED; // 전체 예약이 성공한 경우 Saga Action 상태는 SUCCEEDED
            case CANCELLED:
                return SagaActionStatus.COMPENSATED; // 예약이 취소 완료된 경우 Saga Action 상태는 COMPENSATED(보상 완료)
            case PENDING:
                return SagaActionStatus.STARTED; // 예약이 진행중(PENDING)인 경우 Saga Action 상태는 STARTED
            default:
                throw new BookingApplicationServiceException("BookingStatus가 새로 생성됨을 감지했습니다. " +
                        "변경된 상태에 따른 SagaActionStatus 값을 재정의 해주세요.");
        }
    }

}
