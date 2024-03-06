package com.swyp3.babpool.domain.appointment.dao;

import com.swyp3.babpool.domain.appointment.api.request.AppointmentCreateRequest;
import com.swyp3.babpool.domain.appointment.application.response.AppointmentReceiveResponse;
import com.swyp3.babpool.domain.appointment.application.response.AppointmentSendResponse;
import com.swyp3.babpool.domain.appointment.domain.Appointment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AppointmentRepository {

    int saveAppointmentInit(AppointmentCreateRequest appointmentCreateRequest);

    List<AppointmentSendResponse> findAppointmentListByRequesterId(Long requesterUserId);

    List<AppointmentReceiveResponse> findAppointmentListByReceiverId(Long receiverUserId);

    Appointment findByAppointmentId(Long appointmentId);
}
