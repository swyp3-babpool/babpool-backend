package com.swyp3.babpool.domain.appointment.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class AppointmentWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final AppointmentWebSocketHandler appointmentWebSocketHandler;

//    @Value("${property.url.clientUrl}")
//    private String clientUrl;
//    @Value("${property.url.clientUrlMain}")
//    private String clientUrlMain;
//    @Value("${property.url.clientUrlSub}")
//    private String clientUrlSub;

    /**
     * stompClient 연결을 위한 endpoint 설정
     * stompClient connection local url : ws://localhost:9090/websocket/appointment
     * setAllowedOrigins("*") : 모든 도메인에서 접근 허용, CORS 허용
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket/appointment")
                .setAllowedOrigins("http://localhost:5173", "http://localhost:9090", "https://bab-pool.com", "https://www.bab-pool.com")
                .withSockJS();
    }

    /**
     * 메시지 브로커 구성
     * enableSimpleBroker : Subscribe prefix 설정
     * @param registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic/appointment");
        // 메시지앞에 "/app" 이 붙어있는 경로로 발신되면 해당 경로를 처리하고 있는 핸들러로 전달
        // registry.setApplicationDestinationPrefixes("/app");
    }


    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(appointmentWebSocketHandler);
    }

}
