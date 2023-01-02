package com.cydeo.accountingsimplified.service.implementation;

import com.cydeo.accountingsimplified.TestDocumentInitializer;
import com.cydeo.accountingsimplified.dto.UserDto;
import com.cydeo.accountingsimplified.entity.User;
import com.cydeo.accountingsimplified.mapper.MapperUtil;
import com.cydeo.accountingsimplified.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl service;
    @Mock
    UserRepository repository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    MapperUtil mapperUtil;

    @Test
    @DisplayName("When_find_by_id_then_success")
    public void GIVEN_ID_WHEN_FIND_BY_ID_THEN_SUCCESS(){
        // Given
        UserDto userDto = TestDocumentInitializer.getUser("Admin");
        // When
        when(repository.findUserById(userDto.getId())).thenReturn(new User());
        when(mapperUtil.convert(any(User.class), any(UserDto.class))).thenReturn(userDto);
        // Then
        var user = service.findUserById(userDto.getId());
        assertThat(user.getCompany().getTitle().equals(userDto.getCompany().getTitle()));
    }

    @Test
    @DisplayName("When_find_by_user_name_then_success")
    public void GIVEN_USERNAME_WHEN_FIND_BY_USERNAME_THEN_SUCCESS(){
        // Given
        UserDto userDto = TestDocumentInitializer.getUser("Admin");
        // When
        when(repository.findByUsername(userDto.getUsername())).thenReturn(new User());
        when(mapperUtil.convert(any(User.class), any(UserDto.class))).thenReturn(userDto);
        // Then
        var user = service.findByUsername(userDto.getUsername());
        assertThat(user.getCompany().getTitle().equals(userDto.getCompany().getTitle()));
    }


}