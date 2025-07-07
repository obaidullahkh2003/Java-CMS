package com.obaidullah.cms.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePassword(@NotBlank @Size(min = 6) String password,@NotBlank  String repeatPassword) {

    public boolean passwordsMatch() {
        return password.equals(repeatPassword);

    }
}