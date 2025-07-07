package com.obaidullah.cms.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&\\-_])[A-Za-z\\d@$!%*?&\\-_]{8,}$",
            message = "Password must be at least 8 characters, with uppercase, lowercase, digit, and special character"
    )
    @NotBlank(message = "New password is required")
    private String newPassword;

    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&\\-_])[A-Za-z\\d@$!%*?&\\-_]{8,}$",
            message = "Password must be at least 8 characters, with uppercase, lowercase, digit, and special character"
    )
    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;

}