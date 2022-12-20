package com.cydeo.service;

import com.cydeo.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface SecurityService extends UserDetailsService {

    UserDto getLoggedInUser();

}
