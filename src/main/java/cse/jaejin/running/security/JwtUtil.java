package cse.jaejin.running.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtConfig jwtConfig; // 설정 클래스 주입
    private Key signingKey;

    @PostConstruct
    public void init() {
        // 한 번만 키 초기화 (매 요청마다 byte 변환 안 함)
        this.signingKey = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    /** JWT 토큰 생성 */
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpirationMs()))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /** JWT 토큰에서 사용자 이름 추출 */
    public String extractUsername(String token) {
        return parseToken(token).getBody().getSubject();
    }

    /** JWT 유효성 검사 */
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /** 내부: JWT 파싱 */
    private Jws<Claims> parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token);
    }
}

