package com.swyp3.babpool.domain.appointment.application;

import com.swyp3.babpool.domain.appointment.api.request.AppointmentAcceptRequest;
import com.swyp3.babpool.domain.appointment.api.request.AppointmentCreateRequest;
import com.swyp3.babpool.domain.appointment.api.request.AppointmentRejectRequest;
import com.swyp3.babpool.domain.appointment.application.response.*;
import com.swyp3.babpool.domain.appointment.application.response.appointmentdetail.AppointmentAcceptDetailResponse;
import com.swyp3.babpool.domain.appointment.application.response.appointmentdetail.AppointmentDetailResponse;
import com.swyp3.babpool.domain.appointment.application.response.appointmentdetail.AppointmentReceiveWaitingDetailResponse;
import com.swyp3.babpool.domain.appointment.application.response.appointmentdetail.AppointmentSendWaitingDetailResponse;
import com.swyp3.babpool.domain.appointment.dao.AppointmentRepository;
import com.swyp3.babpool.domain.appointment.domain.*;
import com.swyp3.babpool.domain.appointment.exception.AppointmentException;
import com.swyp3.babpool.domain.appointment.exception.errorcode.AppointmentErrorCode;
import com.swyp3.babpool.domain.possibledatetime.application.PossibleDateTimeService;
import com.swyp3.babpool.domain.possibledatetime.domain.PossibleDateTime;
import com.swyp3.babpool.domain.possibledatetime.domain.PossibleDateTimeStatusType;
import com.swyp3.babpool.domain.profile.application.ProfileService;
import com.swyp3.babpool.domain.reject.application.RejectService;
import com.swyp3.babpool.domain.user.application.UserService;
import com.swyp3.babpool.domain.user.application.response.MyPageUserDto;
import com.swyp3.babpool.domain.user.dao.UserRepository;
import com.swyp3.babpool.global.message.SimpleMessagingPublisher;
import com.swyp3.babpool.global.tsid.TsidKeyGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class AppointmentServiceImpl implements AppointmentService{

    private final SimpleMessagingPublisher simpleMessagingPublisher;
    private final TsidKeyGenerator tsidKeyGenerator;

    private final PossibleDateTimeService possibleDateTimeService;
    private final RejectService rejectService;
    private final UserService userService;
    private final ProfileService profileService;

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;


    /**
     * 밥약 요청 API. 동시 요청을 고려해 최초로 접근한 밥약 요청만 처리하고, 다른 요청은 실패 처리한다.
     * @param appointmentCreateRequest : senderUserId, receiverUserId, targetProfileId, possibleDateTime, appointmentContent
     *                                 데이터가 포함되어 AppointmentService에 전달된다.
     * @return @{@code AppointmentCreateResponse} : appointmentId, targetProfileId, appointmentStatus 를 응답한다.
     */
    @Transactional
    @Override
    public AppointmentCreateResponse makeAppointmentResolveConcurrency(AppointmentCreateRequest appointmentCreateRequest) {

        // 요청 송신자와 수신자 동일한 요청일 경우 예외를 발생한다.
        throwExceptionIfSenderAndReceiverAreSame(appointmentCreateRequest);
        appointmentCreateRequest.setPossibleDateTimeId(possibleDateTimeService.findPossibleDateTimeIdByReceiverAndDateTimeAndStatus(
                appointmentCreateRequest.getReceiverUserId(), appointmentCreateRequest.getPossibleDateTime(), PossibleDateTimeStatusType.AVAILABLE));

        // 약속 요청한 일정이 이미 다른 사용자에 의해 예약 신청된 시간대인지 조회. 이미 예약된 일정이면 예외 발생.
        PossibleDateTime possibleDateTimeEntity = possibleDateTimeService.throwExceptionIfAppointmentAlreadyAcceptedAtSameTime(
                appointmentCreateRequest.getReceiverUserId(), appointmentCreateRequest.getPossibleDateTimeId(), appointmentCreateRequest.getPossibleDateTime());

        // AVAILABLE 상태의 일정을 RESERVED 상태로 변경.
        boolean isStatusUpdated = possibleDateTimeService.changeStatusAsReserved(possibleDateTimeEntity.getPossibleDateTimeId());
        if (!isStatusUpdated){
            throw new AppointmentException(AppointmentErrorCode.APPOINTMENT_CREATE_FAILED, "밥약 가능한 일정 상태를 RESERVED 상태로 변경에 실패.");
        }

        // 약속 생성 및 저장.
        appointmentCreateRequest.setAppointmentId(tsidKeyGenerator.generateTsid());
        int insertedRows = appointmentRepository.saveAppointment(appointmentCreateRequest.toEntity());
        if(insertedRows != 1){
            throw new AppointmentException(AppointmentErrorCode.APPOINTMENT_CREATE_FAILED, "밥약 요청. t_appointment insert fail.");
        }

        // 생성된 약속을 조회해 응답.
        Appointment savedAppointment = appointmentRepository.findByAppointmentId(appointmentCreateRequest.getAppointmentId())
                .orElseThrow(() -> new AppointmentException(AppointmentErrorCode.APPOINTMENT_CREATE_FAILED, "밥약 요청. t_appointment select fail."));

        // 약속 수신자에게 알림 메시지 전송.
        simpleMessagingPublisher.sendAppointmentRequestMessageToAppointmentReceiver(savedAppointment.getAppointmentId(), savedAppointment.getAppointmentSenderId(), savedAppointment.getAppointmentReceiverId());

        return AppointmentCreateResponse.of(savedAppointment, appointmentCreateRequest.getTargetProfileId());
    }

    private static void throwExceptionIfSenderAndReceiverAreSame(AppointmentCreateRequest appointmentCreateRequest) {
        if(appointmentCreateRequest.getSenderUserId().equals(appointmentCreateRequest.getReceiverUserId())){
            throw new AppointmentException(AppointmentErrorCode.APPOINTMENT_REQUESTER_AND_RECEIVER_EQUAL,
                                            "밥 약속 생성. 송신자와 수신자가 동일하게 요청됨.");
        }
    }

    @Override
    public List<AppointmentSendResponse> getSendAppointmentList(Long userId) {
        List<AppointmentSendResponse> sendResponseList = appointmentRepository.findAppointmentListByRequesterId(userId);
        if (sendResponseList.isEmpty()) {
            throw new AppointmentException(AppointmentErrorCode.APPOINTMENT_SEND_NOT_FOUND, "발신한 밥약이 존재하지 않습니다.");
        }
        return sendResponseList;
    }

    @Override
    public List<AppointmentReceiveResponse> getReceiveAppointmentList(Long userId) {
        List<AppointmentReceiveResponse> receiveResponseList = appointmentRepository.findAppointmentListByReceiverId(userId);
        if (receiveResponseList.isEmpty()) {
            throw new AppointmentException(AppointmentErrorCode.APPOINTMENT_RECEIVE_NOT_FOUND, "수신한 밥약이 존재하지 않습니다.");
        }
        return receiveResponseList;
    }

    @Override
    public List<AppointmentHistoryDoneResponse> getDoneAppointmentList(Long userId) {
        List<AppointmentHistoryDoneResponse> historyDoneResponseList = appointmentRepository.findDoneAppointmentListByRequesterId(userId);
        if (historyDoneResponseList.isEmpty()) {
            throw new AppointmentException(AppointmentErrorCode.APPOINTMENT_DONE_NOT_FOUND, "완료된 밥약이 존재하지 않습니다.");
        }
        return historyDoneResponseList;
    }

    @Override
    public List<AppointmentHistoryRefuseResponse> getRefusedAppointmentList(Long requesterUserId) {
        List<AppointmentHistoryRefuseResponse> historyRefuseResponseList = appointmentRepository.findRefuseAppointmentListByRequesterId(requesterUserId);
        if (historyRefuseResponseList.isEmpty()) {
            throw new AppointmentException(AppointmentErrorCode.APPOINTMENT_REFUSE_NOT_FOUND, "거절당한 밥약이 존재하지 않습니다.");
        }
        return historyRefuseResponseList;
    }

//    @Override
//    public List<AppointmentPossibleDateTimeResponse> getAppointmentPossibleDateTime(Long profileId) {
//        List<AppointmentPossibleDateTimeResponse> possibleDateTimeResponseList = appointmentRepository.findAppointmentPossibleDateTimeByProfileId(profileId);
//        if (possibleDateTimeResponseList.isEmpty()) {
//            throw new AppointmentException(AppointmentErrorCode.APPOINTMENT_POSSIBLE_DATETIME_NOT_FOUND, "밥약 가능한 날짜 및 시간이 존재하지 않습니다.");
//        }
//        return possibleDateTimeResponseList;
//    }

    @Override
    @Transactional
    public AppointmentRejectResponse rejectAppointment(AppointmentRejectRequest appointmentRejectRequest, Long userId) {
        Appointment appointment = appointmentRepository.findByAppointmentId(appointmentRejectRequest.getAppointmentId())
                .orElseThrow(() -> new AppointmentException(AppointmentErrorCode.APPOINTMENT_NOT_FOUND, "밥약 요청이 존재하지 않습니다."));

        validateReceiver(userId, appointment);
        validateAppointmentStatus(appointment);

        appointmentRepository.updateAppointmentStatus(appointmentRejectRequest.getAppointmentId(), AppointmentStatus.REJECTED);
        rejectService.createReject(appointmentRejectRequest);

        // 밥약 요청자에게 거절 알림 전송.
        simpleMessagingPublisher.sendAppointmentRejectMessageToAppointmentSender(appointment.getAppointmentId(),
                appointment.getAppointmentSenderId(), appointment.getAppointmentReceiverId());

        return new AppointmentRejectResponse("밥약 거절이 처리되었습니다.");
    }

    @Transactional
    @Override
    public AppointmentAcceptResponse acceptAppointment(AppointmentAcceptRequest appointmentAcceptRequest, Long userId) {
        Appointment appointment = appointmentRepository.findByAppointmentId(appointmentAcceptRequest.getAppointmentId())
                .orElseThrow(() -> new AppointmentException(AppointmentErrorCode.APPOINTMENT_NOT_FOUND, "밥약 요청이 존재하지 않습니다."));

        validateReceiver(userId, appointment);
        validateAppointmentStatus(appointment);

        // 약속의 상태를 수락(ACCEPTED)로 변경
        appointmentRepository.updateAppointmentStatus(appointmentAcceptRequest.getAppointmentId(), AppointmentStatus.ACCEPTED);

        AppointmentAcceptResponse response = appointmentRepository.findAcceptAppointment(appointment.getAppointmentId());

        // 밥약 요청자에게 수락 알림 전송. + 수락 메시지에는 수락자의 프로필 식별 번호도 포함.
        simpleMessagingPublisher.sendAppointmentAcceptMessageToAppointmentSender(appointment.getAppointmentId(),
                appointment.getAppointmentSenderId(), appointment.getAppointmentReceiverId());
        return response;
    }


    /**
     * 사용자가 요청한 특정 약속에 대한 세부정보를 조회한다.
     * @param userId
     * @param appointmentId
     * @return
     */
    @Override
    public AppointmentDetailResponse getAppointmentDetail(Long userId, Long appointmentId) {
        Appointment appointment = appointmentRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new AppointmentException(AppointmentErrorCode.APPOINTMENT_NOT_FOUND, "밥약 요청이 존재하지 않습니다."));

        MyPageUserDto requesterData = userRepository.findMyProfile(appointment.getAppointmentSenderId());
        MyPageUserDto receiverData = userRepository.findMyProfile(appointment.getAppointmentReceiverId());

        // 해당 약속의 설정된 일정 정보 조회
        LocalDateTime appointmentDateTimeValue = possibleDateTimeService.getPossibleDateTimeByDateTimeId(appointment.getPossibleDateTimeId());

        // 대기 중인 밥약
        if(appointment.getAppointmentStatus() == AppointmentStatus.WAITING){
            // 약속 요청 만료까지 남은 시간 계산
            Map<String, Long> timeRemainingMap = getTimeUntilAppointmentRequestExpiration(appointment.getAppointmentCreateDate());

            // 클라이언트가 약속 송신자인 경우
            if (appointment.getAppointmentSenderId().equals(userId)) {
                receiverData.setContactPhone(null);
                receiverData.setContactChat(null);
                return new AppointmentSendWaitingDetailResponse(receiverData, timeRemainingMap, appointmentDateTimeValue, appointment.getAppointmentContent());
            // 클라이언트가 약속 수신자인 경우
            } else if (appointment.getAppointmentReceiverId().equals(userId)) {
                requesterData.setContactPhone(null);
                requesterData.setContactChat(null);
                return new AppointmentReceiveWaitingDetailResponse(requesterData, timeRemainingMap, appointmentDateTimeValue, appointment.getAppointmentContent());
            }
        // 수락된 밥약
        } else if (appointment.getAppointmentStatus() == AppointmentStatus.ACCEPTED){
            // 클라이언트가 약속 송신자인 경우
            if (appointment.getAppointmentSenderId().equals(userId)) {
                return new AppointmentAcceptDetailResponse(receiverData, appointmentDateTimeValue, appointment.getAppointmentContent(), appointment.getPossibleDateTimeId());
            // 클라이언트가 약속 수신자인 경우
            } else if (appointment.getAppointmentReceiverId().equals(userId)) {
                return new AppointmentAcceptDetailResponse(requesterData, appointmentDateTimeValue, appointment.getAppointmentContent(), appointment.getPossibleDateTimeId());
            }
        }
        throw new AppointmentException(AppointmentErrorCode.APPOINTMENT_DETAIL_ERROR,"대기중 혹은 수락된 밥약이 아닙니다.");
    }

    @Override
    public AppointmentCancelResponse cancelAppointmentRequested(Long userId, Long appointmentId) {
        Appointment appointment = appointmentRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new AppointmentException(AppointmentErrorCode.APPOINTMENT_NOT_FOUND, "밥약 요청이 존재하지 않습니다."));

        validateRequester(userId, appointment);
        validateAppointmentStatus(appointment);

        int updatedRows = appointmentRepository.deleteAppointmentById(appointmentId);
        if(updatedRows != 1){
            throw new AppointmentException(AppointmentErrorCode.APPOINTMENT_CANCEL_FAILED, "밥약 요청 삭제에 실패하였습니다.");
        }
        return AppointmentCancelResponse.builder()
                .appointmentId(appointmentId)
                .appointmentCancelResult("밥약 요청이 정상 취소되었습니다.")
                .build();
    }

    @Override
    public AppointmentRefuseDetailResponse getRefuseAppointmentDetail(Long userId, Long appointmentId) {
        Appointment targetAppointment = appointmentRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new AppointmentException(AppointmentErrorCode.APPOINTMENT_NOT_FOUND, "밥약 요청이 존재하지 않습니다."));

        throwExceptionIfRequesterIsNotAppointmentSender(userId, targetAppointment.getAppointmentSenderId());

        return switch (targetAppointment.getAppointmentStatus()) {
            case REJECTED -> appointmentRepository.findRejectAppointmentDetail(appointmentId);
            case EXPIRED -> appointmentRepository.findExpireAppointmentDetail(appointmentId);
            default -> throw new AppointmentException(AppointmentErrorCode.APPOINTMENT_STATUS_IS_NOT_REFUSED,
                    "거절되었거나 수락 시간이 만료된 약속이 아닙니다.");
        };
    }


    private static void throwExceptionIfRequesterIsNotAppointmentSender(Long userId, Long appointmentSenderId) {
        if(!appointmentSenderId.equals(userId))
            throw new AppointmentException(AppointmentErrorCode.APPOINTMENT_REFUSE_DETAIL_NOT_ALLOW,
                    "API 를 호출한 사용자가 조회하려는 약속의 요청자가 아닙니다.");
    }

    private Map<String, Long> getTimeUntilAppointmentRequestExpiration(LocalDateTime createdDateTime) {
        LocalDateTime expireDate = createdDateTime.plusDays(1);
        Duration duration = Duration.between(LocalDateTime.now(), expireDate);

        Map<String,Long> lastingTime = new HashMap<>();
        lastingTime.put("hour", duration.toHours());
        lastingTime.put("minute", duration.toMinutes()-duration.toHours()*60);

        return lastingTime;
    }

    private void validateAppointmentStatus(Appointment appointment) {
        if(appointment.getAppointmentStatus() != AppointmentStatus.WAITING){
            throw new AppointmentException(AppointmentErrorCode.APPOINTMENT_IS_NOT_WAITING,
                    "밥약 요청 상태가 WAITING이 아닙니다.");
        }
    }

    private void validateReceiver(Long userId, Appointment appointment) {
        if(!appointment.getAppointmentReceiverId().equals(userId)){
            throw new AppointmentException(AppointmentErrorCode.APPOINTMENT_NOT_RECEIVER,
                    "밥약 수신자가 아니므로 거절을 할 수 없습니다.");
        }
    }

    private void validateRequester(Long userId, Appointment appointment) {
        if(!appointment.getAppointmentSenderId().equals(userId)){
            throw new AppointmentException(AppointmentErrorCode.APPOINTMENT_NOT_REQUESTER,
                    "밥약 요청자가 아니므로 취소를 할 수 없습니다.");
        }
    }

    @Override
    public int updateAppointmentStatusTo(AppointmentStatus appointmentStatus, Long appointmentId) {
        return appointmentRepository.updateAppointmentStatus(appointmentId, appointmentStatus);
    }

    @Override
    public Long getAppointmentSenderId(Long targetAppointmentId) {
        return appointmentRepository.findByAppointmentId(targetAppointmentId)
                .orElseThrow(() -> new AppointmentException(AppointmentErrorCode.APPOINTMENT_NOT_FOUND, "일치하는 약속이 존재하지 않습니다."))
                .getAppointmentSenderId();
    }
}