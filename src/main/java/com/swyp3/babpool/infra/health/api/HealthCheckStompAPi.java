package com.swyp3.babpool.infra.health.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HealthCheckStompAPi {

    private final SimpMessagingTemplate simpMessagingTemplate;


    @PostMapping("/api/test/stomp/send")
    public void testStompSend(@RequestBody String userUUID) {
        simpMessagingTemplate.convertAndSend("/topic/appointment/" + userUUID, "요청 잘 가니?");
    }

}
