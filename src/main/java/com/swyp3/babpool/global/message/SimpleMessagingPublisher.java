package com.swyp3.babpool.global.message;

import com.swyp3.babpool.domain.appointment.domain.AppointmentAcceptMessage;
import com.swyp3.babpool.domain.appointment.domain.AppointmentRejectMessage;
import com.swyp3.babpool.domain.appointment.domain.AppointmentRequestMessage;
import com.swyp3.babpool.domain.appointment.domain.AppointmentSocketMessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class SimpleMessagingPublisher {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public void sendAppointmentRequestMessageToAppointmentReceiver(Long appointmentId, Long appointmentSenderUserId, Long appointmentReceiverUserId) {
        // 밥약 요청 수신자에게 알림 메시지 전송. + 알림 메시지에는 밥약 송신자(요청자)의 프로필 식별 번호도 포함.
        try {
            simpMessagingTemplate.convertAndSend("/topic/appointment/" + appointmentReceiverUserId.toString(),
                    AppointmentRequestMessage.builder()
                            .appointmentSenderUserId(appointmentSenderUserId)
                            .messageType(AppointmentSocketMessageType.APPOINTMENT_REQUESTED)
                            .build());
        } catch (Exception e) {
            log.error("Failed to send stomp message to receiver. appointmentId: {}", appointmentId);
        }
    }

    // 밥약 요청자에게 거절 알림 메시지 전송 + 알림 메시지에는 거절자의 프로필 식별 번호도 포함.
    public void sendAppointmentRejectMessageToAppointmentSender(Long appointmentId, Long appointmentSenderUserId, Long appointmentReceiverUserId) {
        try {
        simpMessagingTemplate.convertAndSend("/topic/appointment/" + appointmentSenderUserId.toString(),
                AppointmentRejectMessage.builder()
                        .appointmentReceiverUserId(appointmentReceiverUserId)
                        .messageType(AppointmentSocketMessageType.APPOINTMENT_REJECTED)
                        .build());
        } catch (Exception e) {
            log.error("Failed to send stomp message to sender. appointmentId: {}", appointmentId);
        }
    }

    public void sendAppointmentAcceptMessageToAppointmentSender(Long appointmentId, Long appointmentSenderId, Long appointmentReceiverUserId) {
        try {
            simpMessagingTemplate.convertAndSend("/topic/appointment/" + appointmentSenderId.toString(),
                    AppointmentAcceptMessage.builder()
                            .receiverProfileId(appointmentReceiverUserId)
                            .messageType(AppointmentSocketMessageType.APPOINTMENT_ACCEPTED)
                            .build());
        } catch (Exception e) {
            log.error("Failed to send stomp message to sender. appointmentId: {}", appointmentId);
        }
    }
}
