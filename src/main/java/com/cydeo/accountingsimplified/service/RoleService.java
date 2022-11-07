package com.cydeo.accountingsimplified.service;

import com.cydeo.accountingsimplified.dto.RoleDto;

import java.util.List;

public interface RoleService {

    RoleDto findRoleById(Long id);

    RoleDto findByDescription(String description);
    List<RoleDto> getAllRolesForCurrentUser() throws Exception;

}
