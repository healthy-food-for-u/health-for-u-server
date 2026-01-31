package com.healthforu.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class JwtProvider {

    private final String secretKeyStr;
    private final long expireTime;
    private SecretKey key; // 주입받은 문자열을 Key 객체로 변환해서 보관

    public JwtProvider(@Value("${jwt.token.secret-key}") String secretKey,
                       @Value("${jwt.token.expire-time}") long expireTime) {
        this.secretKeyStr = secretKey;
        this.expireTime = expireTime;
    }

    @PostConstruct
    protected void init() {
        // 비밀키를 HS256 알고리즘에 적합한 Key 객체로 변환
        this.key = Keys.hmacShaKeyFor(secretKeyStr.getBytes(StandardCharsets.UTF_8));
    }

    // 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        String authHeader = claims.get("auth").toString();

        return new UsernamePasswordAuthenticationToken(
                claims.getSubject(),
                "",
                AuthorityUtils.commaSeparatedStringToAuthorityList(authHeader)
        );
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}