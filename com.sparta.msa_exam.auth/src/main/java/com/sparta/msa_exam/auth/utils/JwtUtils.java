package com.sparta.msa_exam.auth.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtils {

    private final Logger log = LoggerFactory.getLogger(getClass());

    public static final String AUTHORIZATION_HEADER = "Authorization";

    public static final String BEARER_PREFIX = "Bearer ";

    private Key key;
    private final int TOKEN_EXPIRE_TIME;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;

    public JwtUtils(
            @Value("${jwt.secret-key}") String secretKey,
            @Value("${jwt.access-expiration}") String time
    ) {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        this.key = Keys.hmacShaKeyFor(bytes);
        this.TOKEN_EXPIRE_TIME = Integer.parseInt(time);
    }

    // 토큰 생성
    public String createToken(String username) {
        Date date = new Date();
        return BEARER_PREFIX +
                Jwts.builder()
                    .setSubject(username)
                    .claim("test", "user")
                    .setExpiration(new Date(date.getTime() + TOKEN_EXPIRE_TIME))
                    .setIssuedAt(date)
                    .signWith(key, signatureAlgorithm)
                    .compact();

    }

    // header 에서 jwt 토큰 갖고 오기
    public String getJwtTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰 검증
    public boolean checkValidateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // 토큰에서 body 정보 갖고 오기
    public Claims getClaimInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}
