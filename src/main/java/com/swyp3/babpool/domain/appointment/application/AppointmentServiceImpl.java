package com.swyp3.babpool.domain.appointment.application;

import com.swyp3.babpool.domain.appointment.api.request.AppointmentCreateRequest;
import com.swyp3.babpool.domain.appointment.application.response.AppointmentReceiveResponse;
import com.swyp3.babpool.domain.appointment.application.response.AppointmentCreateResponse;
import com.swyp3.babpool.domain.appointment.application.response.AppointmentSendResponse;
import com.swyp3.babpool.domain.appointment.dao.AppointmentRepository;
import com.swyp3.babpool.domain.appointment.domain.Appointment;
import com.swyp3.babpool.domain.appointment.domain.AppointmentRequestMessage;
import com.swyp3.babpool.domain.appointment.exception.AppointmentException;
import com.swyp3.babpool.domain.appointment.exception.eoorcode.AppointmentErrorCode;
import com.swyp3.babpool.domain.profile.dao.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AppointmentServiceImpl implements AppointmentService{

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final AppointmentRepository appointmentRepository;
    private final ProfileRepository profileRepository;
    
    @Transactional
    @Override
    public AppointmentCreateResponse makeAppointment(AppointmentCreateRequest appointmentCreateRequest) {
        // 프로필 카드 식별 번호로, 타겟(요청받을) 사용자 내부 식별 값 조회.
        Long targetReceiverUserId = profileRepository.findUserIdByProfileId(appointmentCreateRequest.getTargetProfileId());

        throwExceptionIfOtherAppointmentAlreadyAcceptedAtSameTime(targetReceiverUserId, appointmentCreateRequest.getPossibleTimeIdList());

        // t_appointment, t_appointment_request, t_appointment_request_time 테이블에 초기 데이터 저장.
        appointmentRepository.saveAppointmentInit(appointmentCreateRequest);

        // 요청 상대에게 알림 메시지 전송.
        simpMessagingTemplate.convertAndSend("/topic/appointment/" + appointmentCreateRequest.getTargetProfileId(),
                AppointmentRequestMessage.builder()
                        .targetProfileId(appointmentCreateRequest.getTargetProfileId())
                        .message(HttpStatus.OK.name())
                        .build());
        Appointment appointmentEntity = appointmentRepository.findByAppointmentId(appointmentCreateRequest.getAppointmentId());
        return AppointmentCreateResponse.of(appointmentEntity, appointmentCreateRequest.getTargetProfileId());
    }

    /**
     * 이미 다른 사용자에 의해 예약 확정된 시간대가 존재하는지 확인.
     * possible_time_id, appointment_receiver_id 으로 조회.
     * 조회 결과 개수가 0이 아니면 이미 예약된 시간대가 존재.
     * @param targetReceiverUserId,possibleTimeIdList
     */
    private void throwExceptionIfOtherAppointmentAlreadyAcceptedAtSameTime(Long targetReceiverUserId, List<Long> possibleTimeIdList) {
        Integer countDuplicateAppointmentRequest = profileRepository.countAcceptedAppointmentByReceiverIdAndPossibleTimeId(targetReceiverUserId, possibleTimeIdList);
        if(countDuplicateAppointmentRequest > 0) {
            throw new AppointmentException(AppointmentErrorCode.DUPLICATE_APPOINTMENT_REQUEST, "이미 예약된 시간대가 존재합니다.");
        }
    }

    @Override
    public List<AppointmentSendResponse> getSendAppointmentList(Long userId) {
        return appointmentRepository.findAppointmentListByRequesterId(userId);
    }

    @Override
    public List<AppointmentReceiveResponse> getReceiveAppointmentList(Long userId) {
        return appointmentRepository.findAppointmentListByReceiverId(userId);
    }


}
