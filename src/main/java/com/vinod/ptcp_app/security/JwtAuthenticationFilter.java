package com.vinod.ptcp_app.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        // Skip JWT processing for login
        if (requestURI.startsWith("/api/auth/")) {
            logger.info("Skipping JWT filter for login endpoint.");
            filterChain.doFilter(request, response);
            return;
        }

        // Skip JWT processing for websocket
        if (requestURI.startsWith(("/ws/"))) {
            logger.info("Skipping JWT filter for websocket endpoint.");
            filterChain.doFilter(request, response);
            return;
        }

        // Extract token from request
        String token = getJwtFromRequest(request);

        // If token is missing or invalid, return 401 Unauthorized
        if (token == null || token.isEmpty()) {
            logger.error("Missing or empty JWT token. Blocking request.");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid token");
            return; // Stop processing
        }

        // Validate token and set authentication
        if (jwtUtil.validateToken(token, jwtUtil.extractUsername(token))) {
            String username = jwtUtil.extractUsername(token);
            String role = jwtUtil.extractRole(token);

            List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(role);
            Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            logger.error("Invalid JWT token. Blocking request.");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            return; // Stop processing
        }

        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }

    // Helper method to extract JWT from the Authorization header
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Return token without "Bearer " prefix
        }
        return null;
    }
}
