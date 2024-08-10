package com.sparta.msa_exam.gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;

@Component
public class JwtAuthFilter implements GlobalFilter {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final String secretKey;
    private Key key;

    public JwtAuthFilter(@Value("${jwt.secret-key}") String tokenSecret) {
        byte[] bytes = Base64.getDecoder().decode(tokenSecret);
        key = Keys.hmacShaKeyFor(bytes);
        secretKey = tokenSecret;
    }

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // auth로 가는 요청은 막지 않는다.
        if (exchange.getRequest().getURI().getPath().startsWith("/auth")) {
            return chain.filter(exchange);
        }

        String token = extractToken(exchange);

        // 비정상적인 token
        if (token == null || !validateToken(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        Claims claimInfoFromToken = getClaimInfoFromToken(token);
        String user = claimInfoFromToken.getSubject();

        ServerHttpRequest requestWithUser = exchange.getRequest().mutate().header("userId", user).build();
        ServerWebExchange exchangeWithUser = exchange.mutate().request(requestWithUser).build();

        return chain.filter(exchangeWithUser);
    }

    private String extractToken(ServerWebExchange exchange) {
        String authHeader =  exchange.getRequest().getHeaders().getFirst(AUTHORIZATION_HEADER);

        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            return authHeader.substring(7);
        }

        return null;
    }

    private boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            // 추가 검증 로직 가능
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
           return false;
        }
    }

    private Claims getClaimInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}
