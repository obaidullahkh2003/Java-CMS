package com.obaidullah.cms.service.impl;

import com.obaidullah.cms.dto.RoleDto;
import com.obaidullah.cms.dto.RoleResponse;
import com.obaidullah.cms.exception.ResourceNotFoundException;
import com.obaidullah.cms.model.Roles;
import com.obaidullah.cms.model.Users;
import com.obaidullah.cms.repository.RoleRepository;
import com.obaidullah.cms.repository.UserRepository;
import com.obaidullah.cms.service.RoleService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository=userRepository;
    }

    @Override
    public RoleDto createRole(RoleDto roleDto) {
        if (roleRepository.existsByName(roleDto.getName())) {
            throw new IllegalArgumentException("Role name already exists.");
        }

        Roles role = maptoEntity(roleDto);
        Roles savedRole = roleRepository.save(role);
        return maptoDto(savedRole);
    }


    @Override
    public RoleDto getRoleById(long id) {
        Roles role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", id));
        return maptoDto(role);
    }

    @Override
    public RoleResponse getAllRolesList(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Roles> roles = roleRepository.findAll(pageable);

        List<RoleDto> content = roles.getContent().stream()
                .map(this::maptoDto)
                .collect(Collectors.toList());

        RoleResponse roleResponse = new RoleResponse();
        roleResponse.setContent(content);
        roleResponse.setPageNo(roles.getNumber());
        roleResponse.setPageSize(roles.getSize());
        roleResponse.setTotalElements(roles.getTotalElements());
        roleResponse.setTotalPages(roles.getTotalPages());
        roleResponse.setLast(roles.isLast());

        return roleResponse;
    }
    @Override
    public List<RoleDto> getAllRoles() {
        List<Roles> roles = roleRepository.findAll();
        return roles.stream().map(this::maptoDto).collect(Collectors.toList());
    }


    @Override
    public RoleDto updateRole(long id, RoleDto roleDto) {
        Roles role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", id));
        role.setName(roleDto.getName());
        Roles updatedRole = roleRepository.save(role);
        return maptoDto(updatedRole);
    }

    @Override
    @Transactional
    public void deleteRole(long id) {
        Roles role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", id));

        for (Users user : role.getUsers()) {
            user.getRoles().remove(role);
            userRepository.save(user);
        }

        roleRepository.delete(role);
    }



    public RoleDto maptoDto(Roles role) {
        RoleDto dto = new RoleDto();
        dto.setId(role.getId());
        dto.setName(role.getName());
        return dto;
    }

    public Roles maptoEntity(RoleDto dto) {
        Roles role = new Roles();
        role.setId(dto.getId());
        role.setName(dto.getName());
        return role;
    }


}
