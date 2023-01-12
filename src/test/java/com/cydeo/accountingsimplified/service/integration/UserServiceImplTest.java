package com.cydeo.accountingsimplified.service.integration;

import com.cydeo.accountingsimplified.TestDocumentInitializer;
import com.cydeo.accountingsimplified.dto.UserDto;
import com.cydeo.accountingsimplified.entity.User;
import com.cydeo.accountingsimplified.mapper.MapperUtil;
import com.cydeo.accountingsimplified.repository.UserRepository;
import com.cydeo.accountingsimplified.service.SecurityService;
import com.cydeo.accountingsimplified.service.UserService;
import com.cydeo.accountingsimplified.service.implementation.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class UserServiceImplTest {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository repository;
    @Autowired
    MapperUtil mapperUtil;

    User user;

    @BeforeEach
    public void initTest(){
        UserDto userDto = TestDocumentInitializer.getUser("Admin");
        user = mapperUtil.convert(userDto, new User());
        user.setEnabled(true);
        user.setInsertUserId(1L);
        user.setLastUpdateUserId(1L);
        user.setInsertDateTime(LocalDateTime.now());
        user.setLastUpdateDateTime(LocalDateTime.now());
        user = repository.save(user);
    }


    @Test
    @DisplayName("When_find_by_id_then_success")
    public void GIVEN_ID_WHEN_FIND_BY_ID_THEN_SUCCESS(){
        // When
        var returnedUser = userService.findUserById(user.getId());
        // Then
        assertThat(returnedUser.getFirstname().equals(user.getFirstname()));
    }

    @Test
    @DisplayName("When_given_non_existing_id_then_fail")
    public void GIVEN_NON_EXISTING_ID_WHEN_FIND_BY_ID_THEN_FAIL(){
        assertThrows(NoSuchElementException.class, () -> userService.findUserById(null));
    }

    @Test
    @DisplayName("When_find_by_user_name_then_success")
    public void GIVEN_USERNAME_WHEN_FIND_BY_USERNAME_THEN_SUCCESS(){
        // When
        var returedUser = userService.findByUsername(user.getUsername());
        // Then
        assertThat(returedUser.getFirstname().equals(user.getFirstname()));
    }
//
//    @Test
//    @DisplayName("When_get_filtered_users_then_success")
//    public void GIVEN_ROOT_USER_WHEN_GET_FILTERED_USERS_THEN_SUCCESS(){
//        // Given
//        UserDto adminUserDto = TestDocumentInitializer.getUser("Admin");
//        User adminUser = mapperUtil.convert(adminUserDto, new User());
//        UserDto rootUser = TestDocumentInitializer.getUser("Root User");
//        // When
//        doReturn(Arrays.asList(adminUser)).when(repository).findAllByRole_Description("Admin");
//        doReturn(rootUser).when(securityService).getLoggedInUser();
//        var users = service.getFilteredUsers();
//        // Then
//        assertThat(users.size() > 0);
//        assertThat(users.get(0).getRole().getDescription().equals("Admin"));
//    }
//
//    @Test
//    @DisplayName("Given UserDto when save then success")
//    public void GIVEN_USER_DTO_WHEN_SAVE_THEN_SUCCESS(){
//        // Given
//        String testPassword = "$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK";
//        UserDto userDto = TestDocumentInitializer.getUser("Admin");
//        doReturn(testPassword).when(passwordEncoder).encode(anyString());
//        // When
//        var resultUser = service.save(userDto);
//        // Then
//        assertThat(resultUser.getPassword().equals(testPassword));
//
//    }
//
//    @Test
//    @DisplayName("Given UserDto when update then success")
//    public void GIVEN_USER_DTO_WHEN_UPDATE_THEN_SUCCESS(){
//        // Given
//        String testPassword = "$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK";
//        UserDto userDto = TestDocumentInitializer.getUser("Admin");
//        User user = mapperUtil.convert(userDto, new User());
//        UserDto updateUserDto = TestDocumentInitializer.getUser("Manager");
//        User updateUser = mapperUtil.convert(updateUserDto, new User());
//        // When
//        doReturn(testPassword).when(passwordEncoder).encode(anyString());
//        doReturn(user).when(repository).findUserById(anyLong());
//        doReturn(updateUser).when(repository).save(any(User.class));
//
//        var resultUser = service.update(updateUserDto);
//        // Then
//        assertThat(resultUser.getRole().getDescription().equals("Manager"));
//
//    }
//
//    @Test
//    @DisplayName("Given id when delete then success")
//    public void GIVEN_ID_WHEN_DELETE_THEN_SUCCESS(){
//        // Given
//        UserDto userDto = TestDocumentInitializer.getUser("Admin");
//        User user = mapperUtil.convert(userDto, new User());
//        // When
//        doReturn(user).when(repository).findUserById(anyLong());
//
//        service.delete(anyLong());
//        // Then
//        verify(repository).save(any(User.class));
//    }


}