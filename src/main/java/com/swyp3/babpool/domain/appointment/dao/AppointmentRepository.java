package com.swyp3.babpool.domain.appointment.dao;

import com.swyp3.babpool.domain.appointment.api.request.AppointmentAcceptRequest;
import com.swyp3.babpool.domain.appointment.api.request.AppointmentCreateRequest;
import com.swyp3.babpool.domain.appointment.api.request.AppointmentRejectRequest;
import com.swyp3.babpool.domain.appointment.application.response.*;
import com.swyp3.babpool.domain.appointment.domain.Appointment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AppointmentRepository {

    int saveAppointment(AppointmentCreateRequest appointmentCreateRequest);

    List<AppointmentSendResponse> findAppointmentListByRequesterId(Long requesterUserId);

    List<AppointmentReceiveResponse> findAppointmentListByReceiverId(Long receiverUserId);

    Appointment findByAppointmentId(Long appointmentId);

    List<AppointmentHistoryDoneResponse> findDoneAppointmentListByRequesterId(Long requesterUserId);

    List<AppointmentHistoryRefuseResponse> findRefuseAppointmentListByReceiverId(Long receiverUserId);

    List<AppointmentPossibleDateTimeResponse> findAppointmentPossibleDateTimeByProfileId(Long profileId);

    /**
     * 특정 사용자의 특정 시간에 확정된 약속이 있는지 조회
     * @param targetReceiverUserId
     * @param possibleTimeIdList
     * @return
     */
    Integer countAcceptedAppointmentByReceiverIdAndPossibleTimeId(@Param("targetReceiverUserId") Long targetReceiverUserId,
                                                                  @Param("possibleTimeIdList") List<Long> possibleTimeIdList);

    int saveAppointmentRequest(AppointmentCreateRequest appointmentCreateRequest);

    int saveAppointmentRequestTime(AppointmentCreateRequest appointmentCreateRequest);

    void updateAppointmentReject(AppointmentRejectRequest appointmentRejectRequest);
    int updateExpiredStatus();
    void saveRejectData(AppointmentRejectRequest appointmentRejectRequest);


    void updateAppointment(AppointmentAcceptRequest request);

    List<AppointmentRequesterPossibleDateTimeResponse> findRequesterPossibleTime(Appointment appointment);
    String findQuestion(Appointment appointment);

    AppointmentAcceptResponse findAcceptAppointment(Long appointmentId);
}
