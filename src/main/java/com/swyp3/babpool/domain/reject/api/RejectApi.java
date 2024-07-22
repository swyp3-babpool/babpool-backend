package com.swyp3.babpool.domain.reject.api;

import com.swyp3.babpool.domain.appointment.application.response.AppointmentRefuseDetailResponse;
import com.swyp3.babpool.domain.reject.application.RejectService;
import com.swyp3.babpool.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class RejectApi {

    private final RejectService rejectService;


}
