package com.swyp3.babpool.domain.appointment.dao;

import com.swyp3.babpool.domain.appointment.api.request.AppointmentAcceptRequest;
import com.swyp3.babpool.domain.appointment.api.request.AppointmentRejectRequest;
import com.swyp3.babpool.domain.appointment.application.response.*;
import com.swyp3.babpool.domain.appointment.domain.Appointment;
import com.swyp3.babpool.domain.appointment.domain.AppointmentStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface AppointmentRepository {

    // 테스트 코드 작성 완료
    int saveAppointment(Appointment appointment);

    // 테스트 코드 작성 완료
    List<Appointment> findAllBySenderUserId(Long senderUserId);

    AppointmentAcceptResponse findAcceptAppointment(Long appointmentId); // 밥약 요청 수락 후 응답을 위해 4개 테이블 조인 조회

    AppointmentRefuseDetailResponse findRejectAppointmentDetail(@Param("appointmentId") Long appointmentId); // 거절당한 밥약 요청의 상세 정보 조회

    AppointmentRefuseDetailResponse findExpireAppointmentDetail(@Param("appointmentId") Long appointmentId); // 시간만료된 밥약 요청의 상세 정보 조회

    List<AppointmentSendResponse> findAppointmentListByRequesterId(Long requesterUserId);

    List<AppointmentReceiveResponse> findAppointmentListByReceiverId(Long receiverUserId);

    List<AppointmentHistoryDoneResponse> findDoneAppointmentListByRequesterId(Long requesterUserId);

    List<AppointmentHistoryRefuseResponse> findRefuseAppointmentListByRequesterId(Long requesterUserId);

    // 테스트 코드 작성 완료
    Optional<Appointment> findByAppointmentId(Long appointmentId);

//    List<AppointmentPossibleDateTimeResponse> findAppointmentPossibleDateTimeByProfileId(Long profileId);

    // 테스트 코드 작성 완료
    int updateStatusToExpiredWhereStatusIsWaitingAndAppointmentCreateDatePassedOneDay();

    // 테스트 코드 작성 완료
    int updateAppointmentStatus(@Param("targetAppointmentId") Long targetAppointmentId, @Param("status") AppointmentStatus status);

    // 테스트 코드 작성 완료
    int deleteAppointmentById(Long appointmentId);
}
