package com.sbtech.matching_system_test.filter;

import com.sbtech.matching_system_test.provider.JwtProvider;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class JwtFilter implements Filter {

    private final JwtProvider jwtProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String authHeader = httpRequest.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                jwtProvider.validateToken(token); // 유효성만 체크
                chain.doFilter(request, response); // 성공 → 그냥 통과
                return;
            } catch (Exception e) {
                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
                return;
            }
        }

        ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization header missing");
    }
}