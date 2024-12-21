package com.traveladvisor.gatewayserver.filters;//package com.traveladvisor.gatewayserver.filters;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Configuration
public class GlobalFilterConfig {

    @Bean
    @Order(-1)
    public GlobalFilter global404Filter() {
        return (exchange, chain) -> chain.filter(exchange)
                .then(Mono.defer(() -> {
                    // 404 상태 코드일 때 HTML을 반환하도록 합니다.
                    if (exchange.getResponse().getStatusCode() == HttpStatus.NOT_FOUND) {
                        return handleNotFound(exchange);
                    }

                    return Mono.empty();
                }));
    }

    private Mono<Void> handleNotFound(ServerWebExchange exchange) {
        String html = """
                <!DOCTYPE html>
                <html lang="ko">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>404 페이지를 찾을 수 없음 - Travel Advisor</title>
                    <style>
                        body {
                            background-color: #ffffff;
                            font-family: 'Arial', sans-serif;
                            color: #333333;
                            display: flex;
                            justify-content: center;
                            align-items: center;
                            height: 100vh;
                            margin: 0;
                            opacity: 0;
                            animation: fadeIn 1.5s ease-in-out forwards;
                        }
                
                        @keyframes fadeIn {
                            0% {
                                opacity: 0;
                            }
                            100% {
                                opacity: 1;
                            }
                        }
                
                        .content {
                            display: flex;
                            align-items: center;
                            position: relative;
                        }
                        .image-container {
                            position: relative;
                            padding-right: 40px;
                        }
                        .image-container img {
                            width: 300px;
                            height: auto;
                        }
                        .balloon {
                            position: absolute;
                            top: -100px;
                            left: 50%;
                            transform: translateX(-50%);
                            background: #ffffff;
                            border: 3px solid #333;
                            border-radius: 20px;
                            color: #333;
                            padding: 20px 30px;
                            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
                            opacity: 0;
                            pointer-events: none;
                            transition: opacity 0.6s ease-in-out, transform 0.6s ease-in-out;
                        }
                        .image-container:hover .balloon {
                            opacity: 1;
                            transform: translate(-50%, -20px);
                        }
                        .balloon::after {
                            content: '';
                            position: absolute;
                            bottom: -20px;
                            left: 50%;
                            transform: translateX(-50%);
                            width: 0;
                            height: 0;
                            border: 10px solid transparent;
                            border-top-color: #ffffff;
                            border-bottom: none;
                            margin-left: -5px;
                            border-width: 15px;
                            border-top-color: #ffffff;
                            border-left-color: transparent;
                            border-right-color: transparent;
                        }
                        .balloon::before {
                            content: '';
                            position: absolute;
                            bottom: -23px;
                            left: 50%;
                            transform: translateX(-50%);
                            width: 0;
                            height: 0;
                            border: 12px solid transparent;
                            border-top-color: #333;
                            border-bottom: none;
                            margin-left: -6px;
                            border-width: 18px;
                            border-top-color: #333;
                            border-left-color: transparent;
                            border-right-color: transparent;
                        }
                        .text-container {
                            text-align: left;
                        }
                        h1 {
                            font-size: 48px;
                            margin-bottom: 20px;
                            font-weight: bold;
                        }
                        p {
                            font-size: 20px;
                            line-height: 1.6;
                            margin-bottom: 30px;
                        }
                        a {
                            text-decoration: none;
                            background-color: #000000;
                            color: #ffffff;
                            padding: 15px 30px;
                            border-radius: 5px;
                            font-weight: bold;
                            transition: background-color 0.3s ease, transform 0.3s ease;
                        }
                        a:hover {
                            background-color: #333333;
                            transform: scale(1.05);
                        }
                    </style>
                </head>
                <body>
                    <div class="content">
                        <div class="image-container">
                            <div class="balloon">여기가 아닌가?</div>
                            <img src="https://images.squarespace-cdn.com/content/v1/60241cb68df65b530cd84d95/90a5c716-a191-4b46-bafe-6652ff5c5418/mcs_Anxiety1_104.per16.104.jpg" alt="캐릭터">
                        </div>
                        <div class="text-container">
                            <h1>이런! 페이지를 찾을 수 없습니다</h1>
                            <p>
                                길을 잘못 드셨군요! 하지만 걱정 마세요, 우리가 새로운 모험으로 안내해드릴게요!
                            </p>
                            <a href="/">홈으로 돌아가기</a>
                        </div>
                    </div>
                </body>
                </html>
                """;

        exchange.getResponse().setStatusCode(HttpStatus.NOT_FOUND);
        exchange.getResponse().getHeaders().setContentType(MediaType.TEXT_HTML);
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                .bufferFactory()
                .wrap(bytes)));
    }

}
