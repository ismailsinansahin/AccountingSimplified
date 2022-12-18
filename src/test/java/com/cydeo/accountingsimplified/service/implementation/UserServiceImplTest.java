package com.cydeo.accountingsimplified.service.implementation;

import com.cydeo.accountingsimplified.dto.UserDto;
import com.cydeo.accountingsimplified.entity.User;
import com.cydeo.accountingsimplified.exception.AccountingException;
import com.cydeo.accountingsimplified.mapper.MapperUtil;
import com.cydeo.accountingsimplified.repository.UserRepository;
import com.cydeo.accountingsimplified.service.SecurityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;
    @Mock
    SecurityService securityService;
    @Mock
    MapperUtil mapperUtil;
    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    void findUserById_HappyPath() {
        User user = getUser("test@test.com");
        UserDto userDto = getUserDto("test@test.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(mapperUtil.convert(user, new UserDto()))
                .thenReturn(userDto);

        Throwable throwable = catchThrowable(()-> userService.findUserById(1L));
        assertNull(throwable);
    }

    @Test
    void findUserById_Exception_UserNotFound() {
        Throwable throwable = catchThrowable(()-> userService.findUserById(1L));
        assertInstanceOf(AccountingException.class,throwable);
        assertEquals("User not found", throwable.getMessage());
    }

    @Test
    void findByUsername_HappyPath() {
        User user = getUser("test@test.com");
        UserDto userDto = getUserDto("test@test.com");
        when(userRepository.findByUsername("test@test.com")).thenReturn(Optional.of(user));
        when(mapperUtil.convert(user, new UserDto()))
                .thenReturn(userDto);

        Throwable throwable = catchThrowable(()-> userService.findByUsername("test@test.om"));
        assertNull(throwable);
    }

    @Test
    void findByUsername_Exception_UserNotFound() {
        Throwable throwable = catchThrowable(()-> userService.findByUsername("test@test.om"));
        assertInstanceOf(AccountingException.class,throwable);
        assertEquals("User not found", throwable.getMessage());
    }

    @Test
    void getFilteredUsers() {
    }

    private User getUser(String username){
        return User.builder()
                .id(1L)
                .username(username)
                .firstname("Tom")
                .lastname("Hanks")
                .build();
    }

    private UserDto getUserDto(String username){
        return UserDto.builder()
                .id(1L)
                .username(username)
                .firstname("Tom")
                .lastname("Hanks")
                .build();
    }
}