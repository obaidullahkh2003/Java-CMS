package com.obaidullah.cms.service;

import com.obaidullah.cms.dto.RoleDto;
import com.obaidullah.cms.dto.RoleResponse;

import java.util.List;

public interface RoleService {
    RoleDto createRole(RoleDto roleDto);
    RoleDto getRoleById(long id);
    List<RoleDto> getAllRoles();
    RoleResponse getAllRolesList(int pageNo, int pageSize, String sortBy, String sortDir);
    RoleDto updateRole(long id, RoleDto roleDto);
    void deleteRole(long id);
}
