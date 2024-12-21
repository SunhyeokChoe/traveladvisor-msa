package com.traveladvisor.gatewayserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @GetMapping("/contact-us")
    public String contactUs() {
        return "서비스에 문제가 발생했습니다. 관리자에게 문의 바랍니다.";
    }

}
