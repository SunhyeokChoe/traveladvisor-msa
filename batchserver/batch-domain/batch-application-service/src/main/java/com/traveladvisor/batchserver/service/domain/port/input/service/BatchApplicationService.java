package com.traveladvisor.batchserver.service.domain.port.input.service;

import com.traveladvisor.batchserver.service.domain.dto.command.CreateHotelsJobCommand;
import jakarta.validation.Valid;

/**
 * 외부 요인(e.g., 클라이언트, 서버, cURL...)에 의해 호출되는 input port 입니다.
 */
public interface BatchApplicationService {

    /**
     * 호텔 가데이터를 생성합니다.
     */
    void createHotels(@Valid CreateHotelsJobCommand createHotelsJobCommand);

}
