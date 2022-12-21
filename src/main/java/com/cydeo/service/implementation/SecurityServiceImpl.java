package com.cydeo.service.implementation;

import com.cydeo.dto.UserDto;
import com.cydeo.entity.User;
import com.cydeo.entity.common.UserPrincipal;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.SecurityService;
import com.cydeo.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {

    private final UserService userService;
    private final UserRepository userRepository;


    public SecurityServiceImpl(UserRepository userRepository, UserService userService, UserRepository userRepository1, MapperUtil mapperUtil) {
        this.userRepository = userRepository1;
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow( ()-> new UsernameNotFoundException("Username not found"));
        return new UserPrincipal(user);
    }

    @Override
    public UserDto getLoggedInUser() {
        var currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.findByUsername(currentUsername);
    }

}
