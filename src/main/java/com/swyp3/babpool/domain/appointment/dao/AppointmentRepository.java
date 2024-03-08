package com.swyp3.babpool.domain.appointment.dao;

import com.swyp3.babpool.domain.appointment.api.request.AppointmentCreateRequest;
import com.swyp3.babpool.domain.appointment.application.response.*;
import com.swyp3.babpool.domain.appointment.domain.Appointment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface AppointmentRepository {

    int saveAppointmentInit(AppointmentCreateRequest appointmentCreateRequest);

    List<AppointmentSendResponse> findAppointmentListByRequesterId(Long requesterUserId);

    List<AppointmentReceiveResponse> findAppointmentListByReceiverId(Long receiverUserId);

    Appointment findByAppointmentId(@Param("appointmentId") Long appointmentId);

    List<AppointmentHistoryDoneResponse> findDoneAppointmentListByRequesterId(Long requesterUserId);

    List<AppointmentHistoryRefuseResponse> findRefuseAppointmentListByReceiverId(Long receiverUserId);

    List<AppointmentPossibleDateTimeResponse> findAppointmentPossibleDateTimeByProfileId(Long profileId);
}
