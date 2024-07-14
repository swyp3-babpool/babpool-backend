package com.swyp3.babpool.domain.appointment.application;

import com.swyp3.babpool.domain.appointment.api.request.AppointmentAcceptRequest;
import com.swyp3.babpool.domain.appointment.api.request.AppointmentCreateRequestDeprecated;
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
import com.swyp3.babpool.domain.profile.dao.ProfileRepository;
import com.swyp3.babpool.domain.profile.domain.Profile;
import com.swyp3.babpool.domain.user.application.response.MyPageUserDto;
import com.swyp3.babpool.domain.user.dao.UserRepository;
import com.swyp3.babpool.global.tsid.TsidKeyGenerator;
import com.swyp3.babpool.global.uuid.application.UuidService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class AppointmentServiceImpl implements AppointmentService{

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final TsidKeyGenerator tsidKeyGenerator;

    private final UuidService uuidService;
    private final PossibleDateTimeService possibleDateTimeService;

    private final AppointmentRepository appointmentRepository;
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    /**
     * 밥약 생성 요청 처리.
     * Deprecated [2024.07.12] 동시성 처리를 위한 메서드(makeAppointmentResolveConcurrency)으로 대체
     * @param appointmentCreateRequestDeprecated
     * @return AppointmentCreateResponse
     */
    @Deprecated
    @Transactional
    @Override
    public AppointmentCreateResponse makeAppointment(AppointmentCreateRequestDeprecated appointmentCreateRequestDeprecated) {
        // is requesterUserId same as receiverUserId?
        if(appointmentCreateRequestDeprecated.getRequesterUserId().equals(appointmentCreateRequestDeprecated.getReceiverUserId())){
            throw new AppointmentException(AppointmentErrorCode.APPOINTMENT_REQUESTER_AND_RECEIVER_EQUAL, "본인에게 밥약을 요청할 수 없습니다.");
        }

        // 프로필 카드 식별 번호로, 타겟(요청받을) 사용자 내부 식별 값 조회.
        Long targetReceiverUserId = profileRepository.findUserIdByProfileId(appointmentCreateRequestDeprecated.getTargetProfileId());
        appointmentCreateRequestDeprecated.setReceiverUserId(targetReceiverUserId);
        throwExceptionIfOtherAppointmentAlreadyAcceptedAtSameTime(targetReceiverUserId, appointmentCreateRequestDeprecated.getPossibleTimeIdList());

        // t_appointment, t_appointment_request, t_appointment_request_time 테이블에 초기 데이터 저장.
        appointmentRepository.saveAppointment(appointmentCreateRequestDeprecated);
        appointmentRepository.saveAppointmentRequest(appointmentCreateRequestDeprecated);
        appointmentRepository.saveAppointmentRequestTime(appointmentCreateRequestDeprecated);


//        sendStompToAppointmentReceiver(appointmentCreateRequestDeprecated);
        Appointment appointmentEntity = appointmentRepository.findByAppointmentId(appointmentCreateRequestDeprecated.getAppointmentId());
        return AppointmentCreateResponse.of(appointmentEntity, appointmentCreateRequestDeprecated.getTargetProfileId());
    }

    private void sendStompToAppointmentReceiver(AppointmentCreateRequest appointmentCreateRequest) {
        // 밥약 요청 상대에게 알림 메시지 전송. + 알림 메시지에는 요청자의 프로필 식별 번호도 포함.
        Long requesterProfileId = profileRepository.findByUserId(appointmentCreateRequest.getSenderUserId()).getProfileId();
        UUID receiverUserUUID = uuidService.getUuidByUserId(appointmentCreateRequest.getReceiverUserId());

        simpMessagingTemplate.convertAndSend("/topic/appointment/" + receiverUserUUID.toString(),
                AppointmentRequestMessage.builder()
                        .requesterProfileId(requesterProfileId)
                        .messageType(AppointmentSocketMessageType.APPOINTMENT_REQUESTED)
                        .build());
    }

    @Transactional
    @Override
    public AppointmentCreateResponse makeAppointmentResolveConcurrency(AppointmentCreateRequest appointmentCreateRequest) {
        // 요청한 프로필 식별 번호로, 수신자의 식별 값(user_id) 조회.
//        Profile profileByProfileId = profileService.getProfileByProfileId(appointmentCreateRequest.getTargetProfileId());
//        appointmentCreateRequest.setReceiverUserId(profileByProfileId.getUserId());

        // 요청 송신자와 수신자가 다르게 요청되었는지 검증.
        if(appointmentCreateRequest.getSenderUserId().equals(appointmentCreateRequest.getReceiverUserId())){
            throw new AppointmentException(AppointmentErrorCode.APPOINTMENT_REQUESTER_AND_RECEIVER_EQUAL, "밥 약속 생성. 송신자와 수신자가 동일하게 요청됨.");
        }

        // 약속 요청한 일정이 이미 다른 사용자에 의해 예약 신청된 시간대인지 조회. 이미 예약된 일정이면 예외 발생.
        PossibleDateTime possibleDateTimeEntity = possibleDateTimeService.throwExceptionIfAppointmentAlreadyAcceptedAtSameTime(
                appointmentCreateRequest.getReceiverUserId(), appointmentCreateRequest.getPossibleDateTimeId(), appointmentCreateRequest.getPossibleDateTime());

        // AVAILABLE 상태의 일정을 RESERVED 상태로 변경.
        boolean isStatusUpdated = possibleDateTimeService.changeStatusAsReserved(possibleDateTimeEntity.getPossibleDateTimeId());
        if (!isStatusUpdated){
            throw new AppointmentException(AppointmentErrorCode.APPOINTMENT_CREATE_FAILED, "밥약 가능한 일정 상태를 RESERVED 상태로 변경에 실패.");
        }

        // 약속 생성 및 저장.
        int insertedRows = appointmentRepository.saveAppointment(appointmentCreateRequest.toEntity(tsidKeyGenerator.generateTsid()));
        if(insertedRows != 1){
            throw new AppointmentException(AppointmentErrorCode.APPOINTMENT_CREATE_FAILED, "밥약 요청. t_appointment insert fail.");
        }

        // TODO : 외부 패키지로 리팩토링 예정.
        // 약속 수신자에게 알림 메시지 전송.
        sendStompToAppointmentReceiver(appointmentCreateRequest);

        // 생성된 약속을 조회해 응답.
        return AppointmentCreateResponse.of(appointmentRepository.findByAppointmentId(appointmentCreateRequest.getAppointmentId()),
                                            appointmentCreateRequest.getTargetProfileId());
    }

    /**
     * 이미 다른 사용자에 의해 예약 확정된 시간대가 존재하는지 확인.
     * possible_time_id, appointment_receiver_id 으로 조회.
     * 조회 결과 개수가 0이 아니면 이미 예약된 시간대가 존재.
     * @param targetReceiverUserId,possibleTimeIdList
     */
    private void throwExceptionIfOtherAppointmentAlreadyAcceptedAtSameTime(Long targetReceiverUserId, List<Long> possibleTimeIdList) {
        Integer countDuplicateAppointmentRequest = appointmentRepository.countAcceptedAppointmentByReceiverIdAndPossibleTimeId(targetReceiverUserId, possibleTimeIdList);
        if(countDuplicateAppointmentRequest > 0) {
            throw new AppointmentException(AppointmentErrorCode.DUPLICATE_APPOINTMENT_REQUEST, "이미 예약된 시간대가 존재합니다.");
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

    @Override
    public List<AppointmentPossibleDateTimeResponse> getAppointmentPossibleDateTime(Long profileId) {
        List<AppointmentPossibleDateTimeResponse> possibleDateTimeResponseList = appointmentRepository.findAppointmentPossibleDateTimeByProfileId(profileId);
        if (possibleDateTimeResponseList.isEmpty()) {
            throw new AppointmentException(AppointmentErrorCode.APPOINTMENT_POSSIBLE_DATETIME_NOT_FOUND, "밥약 가능한 날짜 및 시간이 존재하지 않습니다.");
        }
        return possibleDateTimeResponseList;
    }

    @Override
    @Transactional
    public AppointmentRejectResponse rejectAppointment(AppointmentRejectRequest appointmentRejectRequest,Long userId) {
        Appointment appointment = appointmentRepository.findByAppointmentId(appointmentRejectRequest.getAppointmentId());

        validateReceiver(userId, appointment);
        validateAppointmentStatus(appointment);

        appointmentRepository.updateAppointmentReject(appointmentRejectRequest);
        appointmentRepository.saveRejectData(appointmentRejectRequest);

        // 밥약 요청자에게 거절 알림 메시지 전송 + 알림 메시지에는 거절자의 프로필 식별 번호도 포함.
        Long receiverProfileId = profileRepository.findByUserId(appointment.getAppointmentReceiverUserId()).getProfileId();
        UUID requesterUserUUID = uuidService.getUuidByUserId(appointment.getAppointmentRequesterUserId());

        simpMessagingTemplate.convertAndSend("/topic/appointment/" + requesterUserUUID.toString(),
                AppointmentRejectMessage.builder()
                        .receiverProfileId(receiverProfileId)
                        .messageType(AppointmentSocketMessageType.APPOINTMENT_REJECTED)
                        .build());
        return new AppointmentRejectResponse("밥약 거절이 처리되었습니다.");
    }

    @Transactional
    @Override
    public AppointmentAcceptResponse acceptAppointment(AppointmentAcceptRequest appointmentAcceptRequest, Long userId) {
        Appointment appointment = appointmentRepository.findByAppointmentId(appointmentAcceptRequest.getAppointmentId());

        validateReceiver(userId, appointment);
        validateAppointmentStatus(appointment);

        appointmentRepository.updateAppointment(appointmentAcceptRequest);
        AppointmentAcceptResponse response = appointmentRepository.findAcceptAppointment(appointment.getAppointmentId());

        // 밥약 요청자에게 수락 알림 전송. + 수락 메시지에는 수락자의 프로필 식별 번호도 포함.
        Long receiverUserId = appointment.getAppointmentReceiverUserId();
        Long receiverProfileId = profileRepository.findByUserId(receiverUserId).getProfileId();

        UUID requesterUserUUID = uuidService.getUuidByUserId(appointment.getAppointmentRequesterUserId());
        simpMessagingTemplate.convertAndSend("/topic/appointment/"+ requesterUserUUID.toString(),
                AppointmentAcceptMessage.builder()
                        .receiverProfileId(receiverProfileId)
                        .messageType(AppointmentSocketMessageType.APPOINTMENT_ACCEPTED)
                        .build());

        // TODO : 프로필 카드 리스트에 노출되도록 하기 위해 profile_active_flag 값을 변경하지 않는 것으로 기획 변경. 더 나은 대안이 있기 전 까지 주석처리.
//        updateProfileActiveFlagIfPossibleDateNoExistAnymore(appointment);
        return response;
    }

    /**
     * 수락된 밥약의 수락자의 활성화된 시간이 더이상 없으면, 해당 사용자의 profile_active_flag 값을 0으로 변경
     * @param appointment
     */
    private void updateProfileActiveFlagIfPossibleDateNoExistAnymore(Appointment appointment) {
        // 해당 사용자(수락자)의 활성화된 시간을 확인 후, 활성화된 시간이 없으면
        if(appointmentRepository.findAppointmentPossibleDateTimeByProfileId(appointment.getAppointmentReceiverUserId()).isEmpty()){
            // 해당 사용자의 profile_active_flag 값을 0으로 변경
            int updatedRows = profileRepository.updateProfileActiveFlag(appointment.getAppointmentReceiverUserId(), false);
            if(updatedRows == 0){
                throw new AppointmentException(AppointmentErrorCode.PROFILE_ACTIVE_FLAG_UPDATE_FAILED, "프로필 활성화 상태 변경에 실패했습니다.");
            }
        }
    }

    @Override
    public AppointmentDetailResponse getAppointmentDetail(Long userId, Long appointmentId) {
        Appointment appointment = appointmentRepository.findByAppointmentId(appointmentId);
        MyPageUserDto requesterData = userRepository.findMyProfile(appointment.getAppointmentRequesterUserId());
        MyPageUserDto receiverData = userRepository.findMyProfile(appointment.getAppointmentReceiverUserId());

        //만료까지 남은 시간 계산
        Map<String, Long> lastingTime = getLastingTime(appointment);
        //가능한 시간대 정보
        List<AppointmentRequesterPossibleDateTimeResponse> requesterPossibleTime = appointmentRepository.findRequesterPossibleTime(appointment);
        //질문 정보
        String question = appointmentRepository.findQuestion(appointment);

        //대기 중인 보낸 밥약 - 만료 시간, 연락처
        if(appointment.getAppointmentStatus().equals("WAITING") && appointment.getAppointmentRequesterUserId()==userId){
            receiverData.setContactPhone(null);
            receiverData.setContactChat(null);
            return new AppointmentSendWaitingDetailResponse(receiverData,lastingTime,requesterPossibleTime,question);
        }
        //대기 중인 받은 밥약 - 만료 시간
        if(appointment.getAppointmentStatus().equals("WAITING") && appointment.getAppointmentReceiverUserId()==userId){
            requesterData.setContactPhone(null);
            requesterData.setContactChat(null);
            return new AppointmentReceiveWaitingDetailResponse(requesterData,lastingTime,requesterPossibleTime,question);
        }
        //수락된 받은 밥약 - 연락처
        if(appointment.getAppointmentStatus().equals("ACCEPT") && appointment.getAppointmentReceiverUserId()==userId){
            return new AppointmentAcceptDetailResponse(requesterData,requesterPossibleTime,question,appointment.getAppointmentFixTimeId());
        }
        //수락된 보낸 밥약 - 연락처
        if(appointment.getAppointmentStatus().equals("ACCEPT") && appointment.getAppointmentRequesterUserId()==userId){
            return new AppointmentAcceptDetailResponse(receiverData,requesterPossibleTime,question,appointment.getAppointmentFixTimeId());
        }
        throw new AppointmentException(AppointmentErrorCode.APPOINTMENT_DETAIL_ERROR,"대기중 혹은 수락된 밥약이 아닙니다.");
    }

    @Override
    public AppointmentCancelResponse cancelAppointmentRequested(Long userId, Long appointmentId) {
        Appointment appointment = appointmentRepository.findByAppointmentId(appointmentId);

        validateRequester(userId, appointment);
        validateAppointmentStatus(appointment);

        int updatedRows = appointmentRepository.updateAppointmentCancel(appointmentId);
        if(updatedRows != 1){
            throw new AppointmentException(AppointmentErrorCode.APPOINTMENT_CANCEL_FAILED, "밥약 요청 취소에 실패하였습니다.");
        }
        return AppointmentCancelResponse.builder()
                .appointmentId(appointmentId)
                .appointmentCancelResult("밥약 요청이 취소되었습니다.")
                .build();
    }

    @Override
    public AppointmentRefuseDetailResponse getRefuseAppointmentDetail(Long userId, Long appointmentId) {
        Appointment appointment = appointmentRepository.findByAppointmentId(appointmentId);

        if(appointment.getAppointmentRequesterUserId()!=userId)
            throw new AppointmentException(AppointmentErrorCode.APPOINTMENT_REFUSE_DETAIL_NOT_ALLOW,
                    "밥약 요청자가 아닙니다.");
        if(appointment.getAppointmentStatus().equals("REJECT")){
            return appointmentRepository.findRejectAppointmentDetail(appointmentId,userId);
        }
        if(appointment.getAppointmentStatus().equals("EXPIRE")){
            return appointmentRepository.findExpireAppointmentDetail(appointmentId,userId);
        }

        throw new AppointmentException(AppointmentErrorCode.APPOINTMENT_STATUS_IS_NOT_REFUSE,
                "거절된 밥약이 아닙니다.");
    }

    private Map<String, Long> getLastingTime(Appointment appointment) {
        LocalDateTime expireDate = appointment.getAppointmentCreateDate().plusDays(1);
        Duration duration = Duration.between(LocalDateTime.now(), expireDate);

        Map<String,Long> lastingTime = new HashMap<>();
        lastingTime.put("hour", duration.toHours());
        lastingTime.put("minute", duration.toMinutes()-duration.toHours()*60);

        return lastingTime;
    }

    private void validateAppointmentStatus(Appointment appointment) {
        if(!appointmentRepository.findByAppointmentId(appointment.getAppointmentId()).getAppointmentStatus()
                .equals("WAITING")){
            throw new AppointmentException(AppointmentErrorCode.APPOINTMENT_IS_NOT_WAITING,"" +
                    "밥약 요청 상태가 WAITING이 아닙니다.");
        }
    }

    private void validateReceiver(Long userId, Appointment appointment) {
        if(appointment.getAppointmentReceiverUserId()!= userId){
            throw new AppointmentException(AppointmentErrorCode.APPOINTMENT_NOT_RECEIVER,
                    "밥약 수신자가 아니므로 거절을 할 수 없습니다.");
        }
    }

    private void validateRequester(Long userId, Appointment appointment) {
        if(appointment.getAppointmentRequesterUserId()!= userId){
            throw new AppointmentException(AppointmentErrorCode.APPOINTMENT_NOT_REQUESTER,
                    "밥약 요청자가 아니므로 취소를 할 수 없습니다.");
        }
    }


}