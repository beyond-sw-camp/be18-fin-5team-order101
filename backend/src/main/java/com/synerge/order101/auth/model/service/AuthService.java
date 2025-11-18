package com.synerge.order101.auth.model.service;

import com.synerge.order101.auth.model.dto.LoginResponse;

public interface AuthService {
    LoginResponse login(String email, String password);

    String createRefreshToken(Long userId);

    void logout(String bearerToken);

    LoginResponse refreshAccessToken(String refreshToken);
}
