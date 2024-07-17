package com.swyp3.babpool.domain.possibledatetime.api;

import com.swyp3.babpool.domain.possibledatetime.application.PossibleDateTimeService;
import com.swyp3.babpool.global.jwt.JwtTokenInterceptor;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PossibleDateTimeApiMockMvcTest {

    @MockBean
    private PossibleDateTimeService possibleDateTimeService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtTokenInterceptor jwtTokenInterceptor;

    @BeforeEach
    public void setup() throws ServletException {
        Mockito.when(jwtTokenInterceptor.preHandle(any(), any(), any())).thenReturn(true);
    }

    @Test
    public void whenGetAppointmentPossibleDateTimeWithInvalidProfileId_thenConstraintViolationException() throws Exception {
        String accessTokenFromRequestHeader = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwicm9sZXMiOiJ0ZXN0IiwidXNlcmlkIjoxfQ.eMKhy-XdJmhuS2QeH1fjycXLS4lucpSa0D56JFMr0fI";

        mockMvc.perform(get("/api/possible/datetime/{userId}", -1)
                        .header("Authorization", "Bearer " + accessTokenFromRequestHeader)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("Validation error: getPossibleDateTimeList.userId: Must be positive"));

    }

}

