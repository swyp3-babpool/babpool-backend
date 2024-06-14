package com.swyp3.babpool.global.logging;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class CustomHttpLogMessageTest {

    HttpStatus httpStatus;
    String originStr = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur non dui justo. Pellentesque interdum iaculis dolor ac faucibus. Etiam orci elit, tristique at faucibus eu, aliquet porta nibh. Quisque ut arcu turpis. Vivamus rutrum facilisis blandit. Duis dui tortor, lacinia ac ornare non, laoreet quis erat. Integer dignissim in tellus vel convallis. Phasellus convallis ipsum quis eros pulvinar, vel maximus ligula ultricies. Proin eu egestas tortor, vel pretium nibh. Proin non iaculis justo, id sodales augue. Aliquam eu sem eu erat feugiat porta.\n" +
            "\n" + "Cras volutpat ac neque et venenatis. Donec porttitor mi a erat viverra, a pulvinar arcu fermentum. Mauris nibh magna, vulputate at volutpat sit amet, luctus eu ipsum. Pellentesque in imperdiet dolor. Praesent vitae varius mauris. Suspendisse finibus eu justo eget aliquam. Nullam consequat ligula nulla, sit amet pellentesque augue pulvinar in. Phasellus malesuada a sapien in dignissim. Proin ut massa eget magna ornare dignissim eget in accumsan.";


    @DisplayName("응답 상태코드가 200이고, responseBody의 길이가 1000자를 넘어가면 1000자까지만 출력한다.")
    @Test
    void truncatedWhenResponseBodyLengthOver1000() {
        // given
        this.httpStatus = HttpStatus.OK;

        // when
        String truncated = truncate(originStr, 1000);

        // then
        assertThat(truncated.length()).isLessThanOrEqualTo(1015);
    }

    @DisplayName("응답 상태코드가 400이고, responseBody의 길이가 1000자를 넘어가면 전체를 출력한다.")
    @Test
    void notTruncatedWhenResponseBodyLengthOver1000() {
        // given
        this.httpStatus = HttpStatus.BAD_REQUEST;

        // when
        String truncated = truncate(originStr, 1000);

        // then
        assertThat(truncated.length()).isEqualTo(originStr.length());
    }

    private String truncate(String str, int length) {
        if (this.httpStatus.is4xxClientError() || this.httpStatus.is5xxServerError()) {
            return str;
        }
        if (!str.isBlank() && str.length() > length) {
            return str.substring(0, length) + "... (truncated)";
        }
        return str;
    }


}