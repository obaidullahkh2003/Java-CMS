package com.obaidullah.cms.service.impl;

import com.obaidullah.cms.dto.UserDto;
import com.obaidullah.cms.dto.UserResponse;
import com.obaidullah.cms.exception.ResourceNotFoundException;
import com.obaidullah.cms.model.Users;
import com.obaidullah.cms.repository.UserRepository;
import com.obaidullah.cms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public UserDto getUserById(long id) {
        Users users = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return mapToDTO(users);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        Users user = userRepository.findFirstByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return mapToDTO(user);
    }

    @Override
    public UserResponse getAllUsers(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Users> users = userRepository.findAll(pageable);

        List<UserDto> content = users.getContent().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        UserResponse userResponse = new UserResponse();
        userResponse.setContent(content);
        userResponse.setPageNo(users.getNumber());
        userResponse.setPageSize(users.getSize());
        userResponse.setTotalElements(users.getTotalElements());
        userResponse.setTotalPages(users.getTotalPages());
        userResponse.setLast(users.isLast());

        return userResponse;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        Users user = mapToEntity(userDto);
        Users savedUser = userRepository.save(user);
        return mapToDTO(savedUser);
    }

    @Override
    public UserDto updateUser(long id, UserDto userDto) {
        Users existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        existingUser.setFirstname(userDto.getFirstName());
        existingUser.setLastname(userDto.getLastName());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setActive(userDto.isActive());
        existingUser.setRoles(userDto.getRoles());

        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        } else {
            existingUser.setPassword(existingUser.getPassword());
        }

        // Save the updated user
        Users updatedUser = userRepository.save(existingUser);
        return mapToDTO(updatedUser);
    }



    @Override
    public void deleteUser(long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        userRepository.delete(user);
    }

    @Override
    public UserDto mapToDTO(Users users) {
        UserDto userDto = new UserDto();
        userDto.setId(users.getId());
        userDto.setActive(users.isActive());
        userDto.setEmail(users.getEmail());
        userDto.setLastName(users.getLastname());
        userDto.setFirstName(users.getFirstname());
        userDto.setPassword(users.getPassword());
        userDto.setRoles(users.getRoles());
        return userDto;
    }

    @Override
    public Users mapToEntity(UserDto userDto) {
        Users users = new Users();
        users.setId(userDto.getId());
        users.setEmail(userDto.getEmail());
        users.setFirstname(userDto.getFirstName());
        users.setLastname(userDto.getLastName());
        users.setActive(userDto.isActive());
        users.setRoles(userDto.getRoles());

        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            users.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        return users;
    }
}
