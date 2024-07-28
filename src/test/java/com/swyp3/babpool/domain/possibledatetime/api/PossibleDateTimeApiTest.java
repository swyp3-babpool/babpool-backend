package com.swyp3.babpool.domain.possibledatetime.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.swyp3.babpool.domain.possibledatetime.application.PossibleDateTimeService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PossibleDateTimeApiTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    @InjectMocks
    private PossibleDateTimeApi possibleDateTimeApi;
    @Mock
    private PossibleDateTimeService possibleDateTimeService;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(possibleDateTimeApi)
                .setControllerAdvice(GlobalExceptionHandler.class)
                .setValidator(new LocalValidatorFactoryBean())
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @DisplayName("밥약 요청 가능 일정/시간 조회 API - 유효성 검증 실패")
    @Test
    void getAvailableTimeValidationFail() throws Exception {
        String accessTokenFromRequestHeader = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwicm9sZXMiOiJ0ZXN0IiwidXNlcmlkIjoxfQ.eMKhy-XdJmhuS2QeH1fjycXLS4lucpSa0D56JFMr0fI";

        mockMvc.perform(get("/api/possible/datetime/{profileId}", "abc")
                        .header("Authorization", "Bearer " + accessTokenFromRequestHeader)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.statusCode").value(400))
//                .andExpect(jsonPath("$.statusDescription").value("BAD_REQUEST"))
//                .andExpect(jsonPath("$.message").value("Validation error: profileId: Must be positive"))
                .andDo(print());
    }
}
