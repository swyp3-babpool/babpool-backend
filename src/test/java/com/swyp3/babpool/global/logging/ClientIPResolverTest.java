package com.swyp3.babpool.global.logging;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

class ClientIPResolverTest {

    @DisplayName("xff 헤더에 IP가 1개 있을 때, 해당 IP를 반환한다.")
    @Test
    void returnUniqueXffIP() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Forwarded-For", "111.222.333.444");
        request.addHeader("X-Real-IP", "123.456.789.012");

        // when
        String clientIP = ClientIPResolver.getClientIP(request);

        // then
        assertThat(clientIP).isEqualTo("111.222.333.444");
    }

    @DisplayName("xff 헤더에 IP가 2개 이상 있을 때, 첫번 째 IP를 반환한다.")
    @Test
    void returnLastXffIP() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Forwarded-For", "111.222.333.444, 555.666.777.888, 999.000.111.222");
        request.addHeader("X-Real-IP", "123.456.789.012");

        // when
        String clientIP = ClientIPResolver.getClientIP(request);

        // then
        assertThat(clientIP).isEqualTo("111.222.333.444");

    }

    @DisplayName("xff 헤더에 비어 있을 때, x-real-ip 헤더값으로 IP를 반환한다.")
    @Test
    void returnXRealIP() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Forwarded-For", "");
        request.addHeader("X-Real-IP", "123.456.789.012");

        // when
        String clientIP = ClientIPResolver.getClientIP(request);

        // then
        assertThat(clientIP).isEqualTo("123.456.789.012");

    }

    @DisplayName("xff 헤더와 x-real-ip 가 모두 없을 때, unknown 문자열을 반환한다.")
    @Test
    void returnUnknownLiteral() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Forwarded-For", "");
        request.addHeader("X-Real-IP", "");

        // when
        String clientIP = ClientIPResolver.getClientIP(request);

        // then
        assertThat(clientIP).isEqualTo("unknown");
    }

}