package com.cydeo.service;

import com.cydeo.dto.RoleDto;

import java.util.List;

public interface RoleService {

    RoleDto findRoleById(Long id);
    List<RoleDto> getFilteredRolesForCurrentUser();

}
