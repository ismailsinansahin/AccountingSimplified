package com.cydeo.accountingsimplified.service;

import com.cydeo.accountingsimplified.dto.RoleDto;

import java.util.List;

public interface RoleService {

    RoleDto findRoleById(Long id);
    List<RoleDto> getFilteredRolesForCurrentUser();

}
