package com.swyp3.babpool.domain.appointment.dao;

import com.swyp3.babpool.domain.appointment.api.request.AppointmentCreateRequest;
import com.swyp3.babpool.domain.appointment.application.response.*;
import com.swyp3.babpool.domain.appointment.domain.Appointment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface AppointmentRepository {

    int saveAppointmentInit(AppointmentCreateRequest appointmentCreateRequest);

    Optional<List<AppointmentSendResponse>> findAppointmentListByRequesterId(Long requesterUserId);

    Optional<List<AppointmentReceiveResponse>> findAppointmentListByReceiverId(Long receiverUserId);

    Appointment findByAppointmentId(Long appointmentId);

    Optional<List<AppointmentHistoryDoneResponse>> findDoneAppointmentListByRequesterId(Long requesterUserId);

    Optional<List<AppointmentHistoryRefuseResponse>> findRefuseAppointmentListByReceiverId(Long receiverUserId);

    Optional<List<AppointmentPossibleDateTimeResponse>> findAppointmentPossibleDateTimeByProfileId(Long profileId);
}
