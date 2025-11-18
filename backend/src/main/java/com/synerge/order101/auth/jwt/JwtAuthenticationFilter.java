package com.synerge.order101.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtUtil jwtUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 1. HttpServletRequest 객체에서 토큰을 추출
        String token = jwtTokenProvider.resolveToken(request.getHeader("Authorization"));

        // 2. 추출한 토큰의 무결성과 유효성을 검증 & 블랙리스트 확인 & 엑세스 토큰 확인
        if (jwtTokenProvider.isUsableAccessToken(token)) {
            // 3. Authentication 객체를 생성
            Authentication authentication = jwtTokenProvider.createAuthentication(token);

            // 4. Authentication 객체를 SecurityContext 객체에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}

