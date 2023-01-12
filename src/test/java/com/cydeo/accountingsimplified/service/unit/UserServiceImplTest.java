package com.cydeo.accountingsimplified.service.unit;

import com.cydeo.accountingsimplified.TestDocumentInitializer;
import com.cydeo.accountingsimplified.dto.UserDto;
import com.cydeo.accountingsimplified.entity.Company;
import com.cydeo.accountingsimplified.entity.User;
import com.cydeo.accountingsimplified.mapper.MapperUtil;
import com.cydeo.accountingsimplified.repository.UserRepository;
import com.cydeo.accountingsimplified.service.SecurityService;
import com.cydeo.accountingsimplified.service.implementation.UserServiceImpl;
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
import java.util.NoSuchElementException;

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
        User user = mapperUtil.convert(userDto, new User());
        // When
        when(repository.findUserById(userDto.getId())).thenReturn(user);
        var returnedUser = service.findUserById(userDto.getId());
        // Then
        assertThat(returnedUser.getFirstname().equals(user.getFirstname()));
    }

    @Test
    @DisplayName("Given_only_admin_When_find_by_id_then_success")
    public void GIVEN_ONLY_ADMIN_ID_WHEN_FIND_BY_ID_THEN_SUCCESS(){
        // Given
        UserDto userDto = TestDocumentInitializer.getUser("Admin");
        User user = mapperUtil.convert(userDto, new User());
        // When
        when(repository.findUserById(userDto.getId())).thenReturn(user);
        when(repository.countAllByCompanyAndRole_Description(any(Company.class), any())).thenReturn(1);
        var returnedUser = service.findUserById(userDto.getId());
        // Then
        assertThat(returnedUser.getIsOnlyAdmin());
    }

    @Test
    @DisplayName("When_given_non_existing_id_then_fail")
    public void GIVEN_NON_EXISTING_ID_WHEN_FIND_BY_ID_THEN_FAIL(){
        when(repository.findUserById(anyLong())).thenThrow(NoSuchElementException.class);
        assertThrows(NoSuchElementException.class, () -> service.findUserById(anyLong()));
    }


    @Test
    @DisplayName("When_find_by_user_name_then_success")
    public void GIVEN_USERNAME_WHEN_FIND_BY_USERNAME_THEN_SUCCESS(){
        // Given
        UserDto userDto = TestDocumentInitializer.getUser("Admin");
        User user = mapperUtil.convert(userDto, new User());
        // When
        when(repository.findByUsername(userDto.getUsername())).thenReturn(user);
        //when(mapperUtil.convert(any(User.class), any(UserDto.class))).thenReturn(userDto);
        var returedUser = service.findByUsername(userDto.getUsername());
        // Then
        assertThat(returedUser.getFirstname().equals(user.getFirstname()));
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
        doReturn(testPassword).when(passwordEncoder).encode(anyString());
        // When
        var resultUser = service.save(userDto);
        // Then
        assertThat(resultUser.getPassword().equals(testPassword));

    }

    @Test
    @DisplayName("Given UserDto when update then success")
    public void GIVEN_USER_DTO_WHEN_UPDATE_THEN_SUCCESS(){
        // Given
        String testPassword = "$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK";
        UserDto userDto = TestDocumentInitializer.getUser("Admin");
        User user = mapperUtil.convert(userDto, new User());
        UserDto updateUserDto = TestDocumentInitializer.getUser("Manager");
        User updateUser = mapperUtil.convert(updateUserDto, new User());
        // When
        doReturn(testPassword).when(passwordEncoder).encode(anyString());
        doReturn(user).when(repository).findUserById(anyLong());
        doReturn(updateUser).when(repository).save(any(User.class));

        var resultUser = service.update(updateUserDto);
        // Then
        assertThat(resultUser.getRole().getDescription().equals("Manager"));

    }

    @Test
    @DisplayName("Given id when delete then success")
    public void GIVEN_ID_WHEN_DELETE_THEN_SUCCESS(){
        // Given
        UserDto userDto = TestDocumentInitializer.getUser("Admin");
        User user = mapperUtil.convert(userDto, new User());
        // When
        doReturn(user).when(repository).findUserById(anyLong());

        service.delete(anyLong());
        // Then
        verify(repository).save(any(User.class));
    }


}