package com.swyp3.babpool.domain.reject.dao;

import com.swyp3.babpool.domain.appointment.api.request.AppointmentRejectRequest;
import com.swyp3.babpool.domain.appointment.domain.AppointmentStatus;
import com.swyp3.babpool.domain.reject.application.response.RefuseDetailResponse;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RejectRepository {

    int saveRejectData(AppointmentRejectRequest appointmentRejectRequest);

    RefuseDetailResponse findByRefuseIdAndStatus(Long refuseId);

}
