package com.swyp3.babpool.global.logging;

import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(OutputCaptureExtension.class)
@Slf4j
class MdcLoggingFilterTest {

    private MdcLoggingFilter mdcLoggingFilter;
    private MockHttpServletRequest request;
    private ContentCachingRequestWrapper requestWrapper;

    @BeforeEach
    public void setUp() {
        mdcLoggingFilter = new MdcLoggingFilter();
        request = Mockito.mock(MockHttpServletRequest.class);
    }

    @DisplayName("UUID 생성 테스트_getMostSignificantBits()")
    @RepeatedTest(10)
    void generateUUIDAndParseMostSignificantBits() {
        // given
        // when
        String uuidVersion4 = UUID.randomUUID().toString().substring(0, 8);
        // then
        System.out.println("uuidVersion4 = " + uuidVersion4);
    }

    @DisplayName("허용된 IP 에서 모니터링 API 호출 시, MDC 로깅에서 제외된다.")
    @Test
    void shouldNotLogWhenRequestFromAllowedIPs() throws Exception {
        // given
        request.addHeader("X-Forwarded-For", "34.168.207.216");
        requestWrapper = new ContentCachingRequestWrapper(request);
        when(request.getRequestURI()).thenReturn("/api/actuator");

        // when
        boolean result = mdcLoggingFilter.shouldNotFilter(requestWrapper);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("허용되지 않은 IP 에서 모니터링 API 호출 시, MDC 로깅에서는 제외되지만, 에러 로깅이 되어야 한다.")
    @Test
    void shouldNotFilterReturnTrueWhenRequestMonitoringFromAllowedIPs(CapturedOutput output) throws Exception {
        // given
        request.addHeader("X-Forwarded-For", "127.0.0.2");
        requestWrapper = new ContentCachingRequestWrapper(request);
        when(request.getRequestURI()).thenReturn("/api/actuator");

        // when
        boolean result = mdcLoggingFilter.shouldNotFilter(requestWrapper);

        // then
        assertThat(result).isTrue();
        assertThat(output.getAll()).isNotEmpty();
        log.info("output >> {}", output.getAll());
    }

    @DisplayName("모니터링 API가 아닌 경우, MDC 로깅을 위해 필터링 제외 되지 않아야 한다.")
    @Test
    void shouldNotFilterReturnFalseWhenRequestNormal(CapturedOutput output) throws ServletException {
        // given
        request.addHeader("X-Forwarded-For", "127.0.0.2");
        requestWrapper = new ContentCachingRequestWrapper(request);
        when(request.getRequestURI()).thenReturn("/api/something");

        // when
        boolean result = mdcLoggingFilter.shouldNotFilter(requestWrapper);

        // then
        assertThat(result).isFalse();
        assertThat(output.getAll()).isEmpty();
        log.info("output >> {}", output.getAll());
    }

}