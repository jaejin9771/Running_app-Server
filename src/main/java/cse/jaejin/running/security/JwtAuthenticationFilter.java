package cse.jaejin.running.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 🔽 요청 URL 및 헤더 로그 출력
        String requestUri = request.getRequestURI();
        String method = request.getMethod();
        String authHeader = request.getHeader("Authorization");

        log.info("요청 URL: {} [{}], Authorization: {}", requestUri, method, authHeader);

        // String authHeader = request.getHeader("Authorization");

        // Bearer 토큰 존재 여부 확인
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // 다음 필터로 진행
            return;
        }

        String token = authHeader.substring(7); // "Bearer " 이후 부분
        String username = jwtUtil.extractUsername(token); // 토큰에서 username 추출

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 유저 정보 조회
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 토큰 유효성 검사
            if (jwtUtil.validateToken(token)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken); //  인증 완료
                log.info("JWT 인증 성공, 사용자: {}", username);

                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                System.out.println(auth.getAuthorities()); // ROLE_USER 가 포함돼야 함

            }
        }

        filterChain.doFilter(request, response); // 다음 필터로 진행
    }
}