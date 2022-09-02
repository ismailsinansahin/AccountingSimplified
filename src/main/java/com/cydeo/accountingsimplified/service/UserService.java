package com.cydeo.accountingsimplified.service;

import com.cydeo.accountingsimplified.dto.CompanyDto;
import com.cydeo.accountingsimplified.dto.RoleDto;
import com.cydeo.accountingsimplified.dto.UserDto;
import com.cydeo.accountingsimplified.entity.User;

import java.util.List;

public interface UserService {

    UserDto findUserById(Long id);
    List<UserDto> getAllUsers() throws Exception;
    UserDto create(UserDto userDto) throws Exception;
    List<RoleDto> getAllRoles() throws Exception;
    CompanyDto getCompanyOfNewUser() throws Exception;
    UserDto update(Long id, UserDto userDto);
    void delete(Long id);

    UserDto getCurrentUserDto();

}
