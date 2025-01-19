package com.obaidullah.cms.service;

import com.obaidullah.cms.dto.UserDto;

public interface AuthinticationService {
    UserDto createUser(UserDto userDto);

    boolean isEmailTaken(String email);
}
