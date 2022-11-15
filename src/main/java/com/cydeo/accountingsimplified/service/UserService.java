package com.cydeo.accountingsimplified.service;

import com.cydeo.accountingsimplified.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto findUserById(Long id);
    UserDto findByUsername(String username);
    List<UserDto> getAllUsers() throws Exception;
    UserDto create(UserDto userDto);
    UserDto update(Long id, UserDto userDto);
    void delete(Long id);
    String getCurrentUserRoleDescription();
    Boolean validateIfEmailUnique(String email);

}
