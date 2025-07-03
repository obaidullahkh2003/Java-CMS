package com.obaidullah.cms.auth.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.obaidullah.cms.auth.utils.AuthResponse;
import com.obaidullah.cms.auth.utils.RefreshTokenRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class AuthFilterService extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AuthFilterService(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String jwt = null;
        String refreshToken = null;

        // Extract cookies
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                switch (cookie.getName()) {
                    case "access_token" -> jwt = cookie.getValue();
                    case "refresh_token" -> refreshToken = cookie.getValue();
                }
            }
        }

        String username = null;

        // If access token exists
        if (jwt != null) {
            try {
                username = jwtService.extractUsername(jwt);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    if (jwtService.isTokenValid(jwt, userDetails)) {
                        authenticate(userDetails, request);
                    } else if (jwtService.isTokenExpired(jwt) && refreshToken != null) {
                        // Try refreshing token
                        AuthResponse newTokens = tryRefreshToken(refreshToken);
                        if (newTokens != null) {
                            // Set new access token cookie
                            Cookie newAccessTokenCookie = new Cookie("access_token", newTokens.getAccessToken());
                            newAccessTokenCookie.setHttpOnly(true);
                            newAccessTokenCookie.setPath("/");
                            newAccessTokenCookie.setMaxAge(60 * 60); // 1 hour
                            response.addCookie(newAccessTokenCookie);

                            // Re-extract username from new token and authenticate
                            String refreshedUsername = jwtService.extractUsername(newTokens.getAccessToken());
                            UserDetails refreshedUserDetails = userDetailsService.loadUserByUsername(refreshedUsername);
                            authenticate(refreshedUserDetails, request);
                        } else {
                            response.sendRedirect("/api/auth/login");
                            return;
                        }
                    }
                }

            } catch (Exception e) {
                // Token is corrupted or tampered with
                response.sendRedirect("/api/auth/login");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void authenticate(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private AuthResponse tryRefreshToken(String refreshToken) {
        try {
            URL url = new URL("http://localhost:8080/api/auth/refresh");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest(refreshToken);
            objectMapper.writeValue(connection.getOutputStream(), refreshTokenRequest);

            if (connection.getResponseCode() == 200) {
                return objectMapper.readValue(connection.getInputStream(), AuthResponse.class);
            } else {
                return null;
            }

        } catch (IOException e) {
            return null;
        }
    }
}
