package com.swyp3.babpool.domain.appointment.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swyp3.babpool.domain.appointment.api.request.AppointmentCreateRequest;
import com.swyp3.babpool.domain.appointment.application.AppointmentService;
import com.swyp3.babpool.global.common.exception.handler.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AppointmentApiTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    @InjectMocks
    private AppointmentApi appointmentApi;
    @Mock
    private AppointmentService appointmentService;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(appointmentApi)
                .setControllerAdvice(GlobalExceptionHandler.class)
                .setValidator(new LocalValidatorFactoryBean())
                .build();
        objectMapper = new ObjectMapper();
    }

    @DisplayName("밥약 요청 API - 유효성 검증 실패")
    @Test
    void makeAppointmentValidationFail() throws Exception {
        String accessTokenFromRequestHeader = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwicm9sZXMiOiJ0ZXN0IiwidXNlcmlkIjoxfQ.eMKhy-XdJmhuS2QeH1fjycXLS4lucpSa0D56JFMr0fI";
        String dto = objectMapper.writeValueAsString(
                AppointmentCreateRequest.builder()
                        .targetProfileId(2L)
                        .possibleTimeIdList(List.of(1L, 2L, 3L))
                        .questionContents("").build()
        );

        mockMvc.perform(post("/api/appointment")
                .header("userId", 1L)
                .header("Authorization", "Bearer " + accessTokenFromRequestHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON)
                .content(dto))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.statusDescription").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("Validation error: questionContents: 질문 내용이 비어있습니다."))
                .andDo(print());
    }

    @DisplayName("밥약 요청 가능 일정/시간 조회 API - 유효성 검증 실패")
    @Test
    void getAvailableTimeValidationFail() throws Exception {
        String accessTokenFromRequestHeader = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwicm9sZXMiOiJ0ZXN0IiwidXNlcmlkIjoxfQ.eMKhy-XdJmhuS2QeH1fjycXLS4lucpSa0D56JFMr0fI";

        mockMvc.perform(get("/api/appointment/{profileId}/datetime", "abc")
                        .header("Authorization", "Bearer " + accessTokenFromRequestHeader)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.statusDescription").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("Validation error: profileId: Must be positive"))
                .andDo(print());
    }
}