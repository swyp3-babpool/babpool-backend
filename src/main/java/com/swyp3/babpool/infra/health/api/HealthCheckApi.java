package com.swyp3.babpool.infra.health.api;

import com.swyp3.babpool.domain.user.dao.UserRepository;
import com.swyp3.babpool.domain.user.domain.UserRole;
import com.swyp3.babpool.global.common.response.ApiResponse;
import com.swyp3.babpool.global.common.response.CookieProvider;
import com.swyp3.babpool.global.jwt.application.JwtService;
import com.swyp3.babpool.global.jwt.application.response.JwtPairDto;
import com.swyp3.babpool.global.uuid.application.UuidService;
import com.swyp3.babpool.infra.health.application.HealthCheckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HealthCheckApi {

    private final HealthCheckService healthCheckService;
    private final JwtService jwtService;
    private final UuidService uuidService;
    private final UserRepository userRepository;

    @GetMapping("/api/test/connection")
    public ResponseEntity<Integer> testConnection() {
        return ResponseEntity.ok(healthCheckService.testConnection());
    }

    /**
     * Test for JWT token validation - require valid token
     * @param userId : user id 가 필요한 API 인 경우, request attribute 으로 부터 userId를 가져와서 사용.
     */
    @GetMapping("/api/test/jwt/require")
    public ResponseEntity<String> testJwtRequire(@RequestAttribute(value = "userId", required = false) Long userId) {
        log.info("userId from request header : {}", userId);
        return ResponseEntity.ok("success");
    }

    /**
     * Test for JWT token validation - permitted
     * @apiNote : token 검증이 필요 없는 예외 API 인 경우, /global/config/WebMvcInterceptorJwtConfig 에서 URL 추가.
     */
    @GetMapping("/api/test/jwt/permitted")
    public ResponseEntity<String> testJwtPermitted() {
        return ResponseEntity.ok("success");
    }

    /**
     * Test for generating JWT tokens
     * @apiNote : JWT token 생성 테스트
     */
    @GetMapping("/api/test/jwt/tokens/{userId}")
    public ResponseEntity<ApiResponse<JwtPairDto>> testGenerateJwtTokens(@PathVariable Long userId) {
        UserRole userRole = userRepository.findById(userId).getUserRole();
        JwtPairDto jwtPair;
        if(userRole == UserRole.ADMIN) {
            jwtPair = jwtService.createJwtPairAdmin(userId, List.of(UserRole.ADMIN));
        } else {
            jwtPair = jwtService.createJwtPair(userId, List.of(UserRole.USER));
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, CookieProvider.ofRefreshToken(jwtPair.getRefreshToken()).toString())
                .body(ApiResponse.ok(jwtPair));
//        return ResponseEntity.ok(jwtService.createJwtPair(Long.valueOf(requestBody.get("userId")), List.of(UserRole.USER)));
    }


    /**
     * Test for generating UUID
     * @apiNote : UUID 생성 테스트
     * @return UUID string type
     */
    @PostMapping("/api/test/uuid")
    public ResponseEntity<String> testGenerateUuid(@RequestBody Map<String, Long> requestBody) {
        return ResponseEntity.ok(String.valueOf(uuidService.createUuid(requestBody.get("userId"))));
    }

    @GetMapping("/api/test/from/uuid/to/id")
    public ResponseEntity<Long> testGetUserIdByUuid(@RequestParam("userUuid") String userUuid) {
        return ResponseEntity.ok(uuidService.getUserIdByUuid(userUuid));
    }

    @GetMapping("/api/test/cookie")
    public ResponseEntity<ApiResponse<String>> testCookie() {
//        return ApiResponseWithCookie.ofRefreshToken(HttpStatus.OK, "success", "data", "refreshToken1234");
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, CookieProvider.ofRefreshToken("refreshToken1234", 1).toString())
                .body(ApiResponse.ok("data"));
    }
    
    @PostMapping("/api/test/mdc")
    public ResponseEntity<String> testMdc(@RequestBody Map<String, String> requestBody) {
        switch (requestBody.get("logLevel")) {
            case "info":
                testMdcInfo();
                break;
            case "debug":
                testMdcDebug();
                break;
            case "warn":
                testMdcWarn();
                break;
            case "error":
                testMdcError();
                break;
            default:
                break;
        }
        return ResponseEntity.ok("success");
    }

    private void testMdcInfo() {
        log.info("testMdcInfo");
    }

    private void testMdcDebug() {
        log.debug("testMdcDebug");
    }

    private void testMdcWarn() {
        log.warn("testMdcWarn");
    }

    private void testMdcError() {
        log.error("testMdcError");
    }

}
