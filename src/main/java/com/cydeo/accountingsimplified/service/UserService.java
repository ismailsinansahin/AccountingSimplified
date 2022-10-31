package com.cydeo.accountingsimplified.service;

import com.cydeo.accountingsimplified.dto.RoleDto;
import com.cydeo.accountingsimplified.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto findUserById(Long id);

    UserDto findByUsername(String username);
    List<UserDto> getAllUsers() throws Exception;
    UserDto create(UserDto userDto) throws Exception;
    UserDto update(Long id, UserDto userDto);
    void delete(Long id);
    List<RoleDto> getAllRoles() throws Exception;

}
