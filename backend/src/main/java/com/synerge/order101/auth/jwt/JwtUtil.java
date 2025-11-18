package com.synerge.order101.auth.jwt;

import com.synerge.order101.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class JwtUtil {
    private final String issuer;
    private final SecretKey secretKey;

    public JwtUtil(JwtProperties jwtProperties) {

        this.issuer = jwtProperties.getIssuer();
        this.secretKey = new SecretKeySpec(
                jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm()
        );
    }


    public String createJwtToken(Map<String, Object> claims, long expiration) {

        return Jwts.builder()
                .header().add("typ","JWT").and()
                .claims(claims)
                .id(Long.toHexString(System.nanoTime()))
                .issuer(issuer) // 발급 주체
                .issuedAt(new Date()) // 발급 시간
                .expiration(new Date(System.currentTimeMillis() + expiration)) // 만료 시간
                .signWith(secretKey) // 서명을 생성
                .compact();

    }

    // 클래임에서 username을 추출하는 메소드
    public String getUserId(String token) {
        return String.valueOf(getClaims(token).get("userId", Long.class));
    }

    // 클래임에서 토큰 타입(Token Type)을 추출하는 메소드
    public String getTokenType(String token) {

        return getClaims(token).get("token_type", String.class);
    }

    // 클래임에서 JTI(JWT ID)를 추출하는 메소드
    public String getJti(String token) {

        return getClaims(token).getId();
    }

    // 클래임에서 발급 시간(IssuedAt)을 추출하는 메소드
    public long getIssuedAt(String token) {

        return getClaims(token).getIssuedAt().getTime();
    }

    // 클래임에서 만료 시간(Expiration)을 추출하는 메소드
    public long getExpiration(String token) {

        return getClaims(token).getExpiration().getTime();
    }

    // 토큰이 유효한지 확인하는 메소드 (토큰이 유효하면 true, 만료되었으면 false 반환)
    public boolean validateToken(String token) {

        // JWT의 만료 시간(Expiration)을 가져와서 현재 시간과 비교하여 토큰이 만료되었는지 확인
        return !getClaims(token).getExpiration().before(new Date());
    }


    // JWT(JSON Web Token)에서 클래임을 추출하는 메소드
    private Claims getClaims(String token) {
        // 토큰이 만료되면 parseSignedClaims() 메소드에서
        // ExpiredJwtException 예외가 발생하여 코드가 실행되지 않기 때문에 아래와 같이 예외 처리를 한다.
        try {
            return Jwts
                    .parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
