package com.obaidullah.cms.controller;

import com.obaidullah.cms.auth.entities.ForgotPassword;
import com.obaidullah.cms.auth.entities.User;
import com.obaidullah.cms.auth.repositories.ForgotPasswordRepository;
import com.obaidullah.cms.auth.repositories.UserRepository;
import com.obaidullah.cms.auth.services.JwtService;
import com.obaidullah.cms.auth.utils.ChangePassword;
import com.obaidullah.cms.auth.utils.ChangePasswordRequest;
import com.obaidullah.cms.dto.MailBody;
import com.obaidullah.cms.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/forgotPassword")
@CrossOrigin(origins = "*")
public class ForgotPasswordController {

    private final UserRepository userRepository;
    private final EmailService emailService;

    private final ForgotPasswordRepository forgotPasswordRepository;

    private final PasswordEncoder passwordEncoder;
    private  final JwtService jwtService;
    @Autowired
    public ForgotPasswordController(UserRepository userRepository, EmailService emailService, ForgotPasswordRepository forgotPasswordRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.forgotPasswordRepository = forgotPasswordRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }


    // send mail for email verification
    @PostMapping("/verifyMail/{email}")
    public ResponseEntity<Map<String, String>> verifyMail(@PathVariable String email) {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Email not found"));

            // Generate new OTP
            int otp = otpGenerator();
            Date expirationTime = new Date(System.currentTimeMillis() + 20 * 100000); // 20 minutes

            // Check for existing OTP record
            Optional<ForgotPassword> existingOtp = forgotPasswordRepository.findByUser(user);

            ForgotPassword fp;
            if (existingOtp.isPresent()) {
                // Update existing record
                fp = existingOtp.get();
                fp.setOtp(otp);
                fp.setExpirationTime(expirationTime);
            } else {
                // Create new record
                fp = ForgotPassword.builder()
                        .otp(otp)
                        .expirationTime(expirationTime)
                        .user(user)
                        .build();
            }

            // Save to database
            forgotPasswordRepository.save(fp);

            // Send email
            MailBody mailBody = MailBody.builder()
                    .to(email)
                    .text("Your new OTP is: " + otp)
                    .subject("New OTP for Password Reset")
                    .build();
            emailService.sendSimpleMessage(mailBody);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "New OTP sent successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }


    @PostMapping("/verifyOtp/{otp}/{email}")
    public ResponseEntity<Map<String, String>> verifyOtp(
            @PathVariable Integer otp,
            @PathVariable String email) {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Please provide a valid email!"));

            // First save the ForgotPassword entity if it's new
            ForgotPassword fp = forgotPasswordRepository.findByOtpAndUser(otp, user)
                    .orElseThrow(() -> new RuntimeException("Invalid OTP for email: " + email));

            if (fp.getExpirationTime().before(Date.from(Instant.now()))) {
                forgotPasswordRepository.deleteById(fp.getFpid());
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                        .body(Map.of("message", "OTP has expired!"));
            }

            // Now proceed with your logic
            String jwtToken = jwtService.generatePasswordResetToken(user);
            forgotPasswordRepository.deleteById(fp.getFpid());

            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", "success",
                            "message", "OTP verified!",
                            "email", email,
                            "token", jwtToken
                    ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "status", "error",
                            "message", e.getMessage()
                    ));
        }
    }


    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody @Valid ChangePasswordRequest request) {

        try {
            final String jwt = authHeader.substring(7);
            final String userEmail = jwtService.extractUsername(jwt);

            if (!jwtService.isPasswordResetToken(jwt)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("message", "Invalid token type"));
            }

            if (!jwtService.isTokenValid(jwt, userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found")))) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Invalid or expired token"));
            }

            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Passwords do not match"));
            }

            String encodedPassword = passwordEncoder.encode(request.getNewPassword());
            userRepository.updatePassword(userEmail, encodedPassword);

            return ResponseEntity.ok()
                    .body(Map.of(
                            "status", "success",
                            "message", "Password reset successfully"
                    ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "status", "error",
                            "message", e.getMessage()
                    ));
        }
    }

    private Integer otpGenerator() {
        Random random = new Random();
        return random.nextInt(100_000, 999_999);
    }
}
