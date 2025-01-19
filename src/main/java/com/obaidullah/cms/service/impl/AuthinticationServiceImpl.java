package com.obaidullah.cms.service.impl;

import com.obaidullah.cms.dto.UserDto;
import com.obaidullah.cms.model.Users;
import com.obaidullah.cms.repository.UserRepository;
import com.obaidullah.cms.service.AuthinticationService;
import com.obaidullah.cms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthinticationServiceImpl implements AuthinticationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Override
    public UserDto createUser(UserDto userDto) {
        Users user = userService.mapToEntity(userDto);
        String hashedpassword = passwordEncoder.encode(userDto.getPassword());
        user.setPassword(hashedpassword);
        Users newuser = userRepository.save(user);
        return userService.mapToDTO(newuser);
    }
    public boolean isEmailTaken(String email) {
        return userRepository.existsByEmail(email);
    }

}
