package com.cydeo.accountingsimplified.service;

import com.cydeo.accountingsimplified.dto.CompanyDto;
import com.cydeo.accountingsimplified.dto.RoleDto;
import com.cydeo.accountingsimplified.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto findUserById(Long id);
    List<UserDto> getAllUsers();
    UserDto create(UserDto userDto);
    List<RoleDto> getAllRoles();
    CompanyDto getCompanyOfNewUser();
    UserDto update(Long id, UserDto userDto);
    void delete(Long id);

}
