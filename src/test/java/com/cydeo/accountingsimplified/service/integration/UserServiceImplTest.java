package com.cydeo.accountingsimplified.service.integration;

import com.cydeo.accountingsimplified.TestDocumentInitializer;
import com.cydeo.accountingsimplified.dto.UserDto;
import com.cydeo.accountingsimplified.entity.User;
import com.cydeo.accountingsimplified.mapper.MapperUtil;
import com.cydeo.accountingsimplified.repository.UserRepository;
import com.cydeo.accountingsimplified.service.SecurityService;
import com.cydeo.accountingsimplified.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class UserServiceImplTest {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository repository;
    @Autowired
    MapperUtil mapperUtil;

    @Autowired
    SecurityService securityService;

    User user;
    Authentication authentication;

    @BeforeEach
    public void initTest() {
        UserDto userDto = TestDocumentInitializer.getUser("Admin");
        user = mapperUtil.convert(userDto, new User());
        user.setEnabled(true);
        user.setInsertUserId(1L);
        user.setLastUpdateUserId(1L);
        user.setInsertDateTime(LocalDateTime.now());
        user.setLastUpdateDateTime(LocalDateTime.now());
        user = repository.save(user);
        authentication = SecurityContextHolder.getContext().getAuthentication();
    }


    @Test
    @DisplayName("When_find_by_id_then_success")
    public void GIVEN_ID_WHEN_FIND_BY_ID_THEN_SUCCESS() {
        // When
        var returnedUser = userService.findUserById(user.getId());
        // Then
        assertThat(returnedUser.getFirstname().equals(user.getFirstname()));
    }

    @Test
    @DisplayName("When_given_null_id_then_fail")
    public void GIVEN_NULL_ID_WHEN_FIND_BY_ID_THEN_FAIL() {
        assertThrows(NoSuchElementException.class, () -> userService.findUserById(null));
    }

    @Test
    @DisplayName("When_given_non_existing_id_then_fail")
    public void GIVEN_NON_EXISTING_ID_WHEN_FIND_BY_ID_THEN_FAIL() {
        assertThrows(NoSuchElementException.class, () -> userService.findUserById(99L));
    }

    @Test
    @DisplayName("When_find_by_user_name_then_success")
    public void GIVEN_USERNAME_WHEN_FIND_BY_USERNAME_THEN_SUCCESS() {
        // When
        var returedUser = userService.findByUsername(user.getUsername());
        // Then
        assertThat(returedUser.getFirstname().equals(user.getFirstname()));
    }


    @Test
    @WithAnonymousUser
    @DisplayName("When create new User then success")
    public void GIVEN_USER_DTO_WHEN_SAVE_THEN_SUCCESS(){
        // When
        UserDto userDto = mapperUtil.convert(user, new UserDto());
        userDto.setId(null);
        userDto.setUsername("Test Username");
        var returnedUser = userService.save(userDto);
        // Then
        assertThat(returnedUser.getUsername().equals("Test Username"));

    }


}