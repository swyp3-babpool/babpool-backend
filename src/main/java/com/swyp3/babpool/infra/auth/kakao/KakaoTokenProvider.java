package com.swyp3.babpool.infra.auth.kakao;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.swyp3.babpool.infra.auth.exception.AuthException;
import com.swyp3.babpool.infra.auth.exception.errorcode.AuthExceptionErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoTokenProvider {
    private String reqURL = "https://kauth.kakao.com/oauth/token";
    @Value("${property.oauth.kakao.client-id}")
    private String clientId;
    @Value("${property.oauth.kakao.redirect-uri}")
    private String redirectUri;
    @Value("${property.oauth.kakao.client-secret}")
    private String clientSecret;

    public String getIdTokenFromKakao(String code) {
        String idToken;

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();

            sb.append("grant_type=authorization_code");
            sb.append("&client_id="+clientId);
            sb.append("&redirect_uri="+redirectUri);
            sb.append("&client_secret="+clientSecret);
            sb.append("&code=" + code);

            bw.write(sb.toString());
            bw.flush();

            BufferedReader br;

            if (conn.getResponseCode() >= 400) {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            }

            String line;
            StringBuilder responseSb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                responseSb.append(line);
            }

            String result = responseSb.toString();
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);


            // 에러 응답이면 에러 메시지를 가져옴
            if (conn.getResponseCode() >= 400) {
                String errorMessage = element.getAsJsonObject().get("error_description").getAsString();
                throw new AuthException(AuthExceptionErrorCode.AUTH_ERROR_CONNECT_WITH_KAKAO,
                        errorMessage);
            }
            // 정상 응답이면 id Token을 가져옴
            else{
                idToken = element.getAsJsonObject().get("id_token").getAsString();
                log.info("카카오로부터 Token 성공적으로 조회");
                br.close();

                return idToken;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new AuthException(AuthExceptionErrorCode.AUTH_ERROR_CONNECT_WITH_KAKAO,
                    e.getMessage().toString());
        }
    }
}
