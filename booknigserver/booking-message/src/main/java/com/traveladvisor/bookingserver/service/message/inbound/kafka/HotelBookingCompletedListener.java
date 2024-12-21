package com.traveladvisor.bookingserver.service.message.inbound.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class HotelBookingCompletedListener {

//    private final BookingApplicationService bookingApplicationService;
//    private final AvroMessageConverter avroMessageConverter;
//
//    @Bean
//    public Consumer<?> hotelBookingCompleted(@Payload byte[] message) {
//        // Avro 메시지를 GenericRecord로 변환
//        GenericRecord record = (GenericRecord) avroMessageConverter.fromMessage(message, GenericRecord.class);
//
//        // 메시지에서 필요한 데이터 추출
//        String email = record.get("email").toString();
//        // 다른 필요한 필드도 추출
//
//        // CreateBookingCommand 객체 생성
//        CreateBookingCommand createBookingCommand = new CreateBookingCommand(
//                email,
//                // 다른 필드들...
//        );
//
//        // 예약 처리
//        bookingApplicationService.createBooking(createBookingCommand);
//    }

}
