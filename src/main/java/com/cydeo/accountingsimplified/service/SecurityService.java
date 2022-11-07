package com.cydeo.accountingsimplified.service;

import com.cydeo.accountingsimplified.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface SecurityService extends UserDetailsService {

    @Override
    UserDetails loadUserByUsername(String s) throws UsernameNotFoundException;

    String getCurrentUserUsername();

    UserDto getLoggedInUser();

}
