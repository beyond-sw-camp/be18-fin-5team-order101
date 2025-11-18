package com.synerge.order101.auth.model.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import java.time.Duration;


public interface JwtCookieService {

    HttpHeaders createRefreshTokenCookieHeaders(ResponseCookie cookie);

    ResponseCookie createRefreshTokenCookie(String refreshToken, Duration duration);

    ResponseCookie deleteRefreshTokenCookie();
}
