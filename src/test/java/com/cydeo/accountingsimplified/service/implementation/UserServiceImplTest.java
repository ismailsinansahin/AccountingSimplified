package com.cydeo.accountingsimplified.service.implementation;

import com.cydeo.accountingsimplified.TestDocumentInitializer;
import com.cydeo.accountingsimplified.dto.UserDto;
import com.cydeo.accountingsimplified.entity.User;
import com.cydeo.accountingsimplified.mapper.MapperUtil;
import com.cydeo.accountingsimplified.repository.UserRepository;
import com.cydeo.accountingsimplified.service.SecurityService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;

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
    SecurityService securityService;
    @Mock
    PasswordEncoder passwordEncoder;
    @Spy
    private MapperUtil mapperUtil = new MapperUtil(new ModelMapper());

    @Test
    @DisplayName("When_find_by_id_then_success")
    public void GIVEN_ID_WHEN_FIND_BY_ID_THEN_SUCCESS(){
        // Given
        UserDto userDto = TestDocumentInitializer.getUser("Admin");
        // When
        when(repository.findUserById(userDto.getId())).thenReturn(new User());
        //when(mapperUtil.convert(any(User.class), any(UserDto.class))).thenReturn(userDto);
        var user = service.findUserById(userDto.getId());
        // Then
        assertThat(user.getCompany().getTitle().equals(userDto.getCompany().getTitle()));
    }


    @Test
    @DisplayName("When_find_by_user_name_then_success")
    public void GIVEN_USERNAME_WHEN_FIND_BY_USERNAME_THEN_SUCCESS(){
        // Given
        UserDto userDto = TestDocumentInitializer.getUser("Admin");
        // When
        when(repository.findByUsername(userDto.getUsername())).thenReturn(new User());
        //when(mapperUtil.convert(any(User.class), any(UserDto.class))).thenReturn(userDto);
        var user = service.findByUsername(userDto.getUsername());
        // Then
        assertThat(user.getCompany().getTitle().equals(userDto.getCompany().getTitle()));
    }

    @Test
    @DisplayName("When_get_filtered_users_then_success")
    public void GIVEN_ROOT_USER_WHEN_GET_FILTERED_USERS_THEN_SUCCESS(){
        // Given
        UserDto adminUserDto = TestDocumentInitializer.getUser("Admin");
        User adminUser = mapperUtil.convert(adminUserDto, new User());
        UserDto rootUser = TestDocumentInitializer.getUser("Root User");
        // When
        doReturn(Arrays.asList(adminUser)).when(repository).findAllByRole_Description("Admin");
        doReturn(rootUser).when(securityService).getLoggedInUser();
        var users = service.getFilteredUsers();
        // Then
        assertThat(users.size() > 0);
        assertThat(users.get(0).getRole().getDescription().equals("Admin"));
    }

    @Test
    @DisplayName("Given UserDto when save then success")
    public void GIVEN_USER_DTO_WHEN_SAVE_THEN_SUCCESS(){
        // Given
        String testPassword = "$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK";
        UserDto userDto = TestDocumentInitializer.getUser("Admin");
        User user = mapperUtil.convert(userDto, new User());
        doReturn(testPassword).when(passwordEncoder).encode(anyString());
        // When
        var resultUser = service.save(userDto);
        // Then
       assertThat(resultUser.getPassword().equals(testPassword));

    }


}