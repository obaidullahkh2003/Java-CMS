package com.obaidullah.cms.controller;

import com.obaidullah.cms.auth.entities.RefreshToken;
import com.obaidullah.cms.auth.entities.User;
import com.obaidullah.cms.auth.services.AuthService;
import com.obaidullah.cms.auth.services.JwtService;
import com.obaidullah.cms.auth.services.RefreshTokenService;
import com.obaidullah.cms.auth.utils.AuthResponse;
import com.obaidullah.cms.auth.utils.LoginRequest;
import com.obaidullah.cms.auth.utils.RefreshTokenRequest;
import com.obaidullah.cms.auth.utils.RegisterRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, RefreshTokenService refreshTokenService, JwtService jwtService) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") RegisterRequest registerRequest, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "register";
        }
        try {
            authService.register(registerRequest);
            return "redirect:/api/auth/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @PostMapping("/login")
    public String login(
            @Valid @ModelAttribute("loginRequest") LoginRequest loginRequest,
            BindingResult bindingResult,
            HttpServletResponse response,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "login";
        }

        try {
            AuthResponse authResponse = authService.login(loginRequest);

            Cookie accessTokenCookie = new Cookie("access_token", authResponse.getAccessToken());
            accessTokenCookie.setHttpOnly(true);
            accessTokenCookie.setMaxAge(60 * 60);
            accessTokenCookie.setPath("/");
            response.addCookie(accessTokenCookie);


            Cookie refreshTokenCookie = new Cookie("refresh_token", authResponse.getRefreshToken());
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setMaxAge(60 * 60 * 24 * 30);
            refreshTokenCookie.setPath("/");
            response.addCookie(refreshTokenCookie);

            return "redirect:/index";

        } catch (Exception e) {
            model.addAttribute("error", "Invalid credentials");
            return "login";
        }
    }


    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie accessTokenCookie = new Cookie("access_token", null);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(0);

        Cookie refreshTokenCookie = new Cookie("refresh_token", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        SecurityContextHolder.clearContext();
        return "redirect:/api/auth/login";
    }



    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {

        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(refreshTokenRequest.getRefreshToken());
        User user = refreshToken.getUser();

        String accessToken = jwtService.generateToken(user);

        return ResponseEntity.ok(AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .build());
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("user", new RegisterRequest());
        return "register";
    }

    @GetMapping("/forgot")
    public String forgotPassword(){
        return "password";
    }
}
