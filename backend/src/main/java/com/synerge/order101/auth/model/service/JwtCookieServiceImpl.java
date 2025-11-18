package com.synerge.order101.auth.model.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class JwtCookieServiceImpl implements JwtCookieService {
    @Override
    public HttpHeaders createRefreshTokenCookieHeaders(ResponseCookie cookie) {

        HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());

        return headers;
    }

    @Override
    public ResponseCookie createRefreshTokenCookie(String refreshToken, Duration duration) {

        return ResponseCookie
                .from("refresh_token",refreshToken)
                .httpOnly(true)
                .path("/")
                .maxAge(duration)
                .build();

    }

    @Override
    public ResponseCookie deleteRefreshTokenCookie() {
        // 토큰을 바로 만료 처리한다.
        return createRefreshTokenCookie("", Duration.ofSeconds(0));
    }
}
