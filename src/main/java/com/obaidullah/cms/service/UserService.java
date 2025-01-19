package com.obaidullah.cms.service;

import com.obaidullah.cms.dto.UserDto;
import com.obaidullah.cms.dto.UserResponse;
import com.obaidullah.cms.model.Users;

public interface UserService {
    UserDto getUserById(long id);

    UserDto getUserByEmail(String email);

    UserResponse getAllUsers(int pageNo, int pageSize, String sortBy, String sortDir);

    UserDto createUser(UserDto userDto);

    UserDto updateUser(long id, UserDto userDto);

    void deleteUser(long id);

    UserDto mapToDTO(Users users);

    Users mapToEntity(UserDto userDto);
}
