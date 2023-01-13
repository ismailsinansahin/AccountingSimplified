package com.cydeo.unitTest.service.impl;

import com.cydeo.TestDocumentInitializer;
import com.cydeo.dto.CompanyDto;
import com.cydeo.dto.RoleDto;
import com.cydeo.dto.UserDto;
import com.cydeo.entity.Company;
import com.cydeo.entity.Role;
import com.cydeo.entity.User;
import com.cydeo.exception.AccountingException;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.SecurityService;
import com.cydeo.service.implementation.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImpl_UnitTest {

    @Mock
    UserRepository userRepository;

    @Mock
    SecurityService securityService;

    @Spy
    MapperUtil mapperUtil = new MapperUtil(new ModelMapper());

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    void should_find_user_by_id_happy_path() {
        // given
        UserDto userDto = TestDocumentInitializer.getUser("Admin");
        User user = mapperUtil.convert(userDto, new User());
        // when
        when(userRepository.findById(userDto.getId())).thenReturn(Optional.of(user));
//        when(mapperUtil.convert(any(User.class), any(UserDto.class))).thenReturn(userDto);
//        when(mapperUtil.convert(any(User.class), ArgumentMatchers.<Class<UserDto>>any())).thenReturn(UserDto.class);
//        when(mapperUtil.convert(user, any(UserDto.class))).thenReturn(userDto); // exceptions.misusing.InvalidUseOfMatchersException
//        when(mapperUtil.convert(user, new UserDto())).thenReturn(userDto); // mockito.exceptions.misusing.PotentialStubbingProblem
//        when(userMapper.convertToDto(user)).thenReturn(userDto);

        UserDto actualUser = userService.findUserById(1L);

        // then
        assertThat(actualUser).usingRecursiveComparison()
                .ignoringFields("password", "confirmPassword")
                .isEqualTo(userDto);
    }

    @Test
    void findUserById_should_throw_accounting_exception_with_user_not_found_text() {
        // when
        // it throws exception since no mock of userRepository and userRepository.findById(1L) returns null
        Throwable throwable = catchThrowable(() -> userService.findUserById(1L));
        // assertThrows also works, but we can also assert message with catchThrowable
        // assertThrows(AccountingException.class, ()-> userService.findUserById(1L));

        // then
        assertInstanceOf(AccountingException.class, throwable);
        assertEquals("User not found", throwable.getMessage());
    }

    @Test
    void should_find_by_username_happy_path() {
        // given
        UserDto userDto = TestDocumentInitializer.getUser("Admin");
        User user = mapperUtil.convert(userDto, new User());
        // when
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        UserDto actualUser = userService.findByUsername(userDto.getUsername());
        // then
        assertThat(actualUser).usingRecursiveComparison()
                .ignoringFields("password", "confirmPassword")
                .isEqualTo(userDto);
    }

    @Test
    void findByUsername_should_throw_accounting_exception_with_user_not_found_text() {
        // when
        Throwable throwable = catchThrowable(() -> userService.findByUsername(anyString()));
        // then
        assertInstanceOf(AccountingException.class, throwable);
        assertEquals("User not found", throwable.getMessage());
    }

    @Test
    void getFilteredUsers_given_logged_in_as_Root_and_all_users_onlyAdmi_sorts_by_CompanyTitle_then_RoleDescription() {
        // given
        List<UserDto> userDtos = Arrays.asList(
                TestDocumentInitializer.getUser("Admin"),
                TestDocumentInitializer.getUser("Admin"),
                TestDocumentInitializer.getUser("Admin"));
        userDtos.get(0).getCompany().setTitle("Zet");
        userDtos.get(1).getCompany().setTitle("Abc");
        userDtos.get(2).getCompany().setTitle("Ower");
        List<User> userList = userDtos.stream()
                .map(userDto -> mapperUtil.convert(userDto, new User()))
                .collect(Collectors.toList());
        List<UserDto> expectedList = userDtos.stream()
                .sorted(Comparator.comparing((UserDto u) -> u.getCompany().getTitle())
                        .thenComparing(u -> u.getRole().getDescription()))
                        .collect(Collectors.toList());

        // when
        when(securityService.getLoggedInUser()).thenReturn(userDtos.get(0));
        when(userRepository.findAllByCompany(any())).thenReturn(userList);
        when(userRepository.countAllByCompany_TitleAndRole_Description(anyString(), anyString())).thenReturn(1);
        List<UserDto> actualList = userService.getFilteredUsers();

        // then
        assertThat(actualList).usingRecursiveComparison()
                .ignoringFields("password", "confirmPassword", "isOnlyAdmin")
                .isEqualTo(expectedList);
        actualList.forEach(
                userDto -> assertTrue(userDto.getIsOnlyAdmin())
        );
    }

    @Test
    void getFilteredUsers_given_logged_in_as_Admin_and_all_user_not_only_admin_sorts_by_CompanyTitle_then_RoleDescription() {
        // given
        List<UserDto> userDtos = Arrays.asList(
                TestDocumentInitializer.getUser("Admin"),
                TestDocumentInitializer.getUser("Manager"),
                TestDocumentInitializer.getUser("Employee"));
        userDtos.get(0).getCompany().setTitle("Zet");
        userDtos.get(1).getCompany().setTitle("Abc");
        userDtos.get(2).getCompany().setTitle("Ower");
        List<User> userList = userDtos.stream()
                .map(userDto -> mapperUtil.convert(userDto, new User()))
                .collect(Collectors.toList());
        List<UserDto> expectedList = userDtos.stream()
                .sorted(Comparator.comparing((UserDto u) -> u.getCompany().getTitle())
                        .thenComparing(u -> u.getRole().getDescription()))
                .collect(Collectors.toList());

        // when
        when(securityService.getLoggedInUser()).thenReturn(userDtos.get(0));
        when(userRepository.findAllByCompany(any(Company.class))).thenReturn(userList);
        when(userRepository.countAllByCompany_TitleAndRole_Description(anyString(), anyString())).thenReturn(2);
        List<UserDto> actualList = userService.getFilteredUsers();

        // then
        assertThat(actualList).usingRecursiveComparison()
                .ignoringFields("password", "confirmPassword")
                .isEqualTo(expectedList);
    }

    @Test
    void save_happyPath() {
        // given
        UserDto userDto = TestDocumentInitializer.getUser("Admin");
        User user = mapperUtil.convert(userDto, new User());
        // when
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto actualUser = userService.save(userDto);
        // then
        verify(passwordEncoder).encode(anyString());
        assertThat(actualUser).usingRecursiveComparison()
                .ignoringFields("password", "confirmPassword")
                .isEqualTo(userDto);
        // we cannot test implementation of setEnabled() since user is not mocked and UserDto does not have this field.
    }

    //todo should we test this and update null
    @Test
    void save_null_dto_throws_exception() {
        // option 1:
        // only checks exception which comes from mapperUtil
        assertThrows(IllegalArgumentException.class, () -> userService.save(null));

        // option 2 : better - check exception and message
        Throwable throwable = catchThrowable(() -> userService.save(null));
        assertInstanceOf(IllegalArgumentException.class, throwable);
        assertEquals("source cannot be null", throwable.getMessage());

        //validation of username, password, etc. should be done on controller side. that's why no test case is written here
    }

    @Test
    void update_happyPath() {
        // given
        UserDto userDto = TestDocumentInitializer.getUser("Admin");
        User user = mapperUtil.convert(userDto, new User());
        // when
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto actualUser = userService.update(userDto);
        // then
        verify(passwordEncoder).encode(anyString());
        assertThat(actualUser).usingRecursiveComparison()
                .ignoringFields("password", "confirmPassword")
                .isEqualTo(userDto);
    }

    @Test
    void update_null_dto_throws_exception() {
        // only checks exception which comes from mapperUtil
        assertThrows(IllegalArgumentException.class, () -> userService.update(null));

        // check exception and message
        Throwable throwable = catchThrowable(() -> userService.update(null));
        assertInstanceOf(IllegalArgumentException.class, throwable);
        assertEquals("source cannot be null", throwable.getMessage());

        //validation of username, password, etc. should be done on controller side. that's why no test case is written here
    }

    @Test
    void delete_happyPath() {
        // given
        UserDto userDto = TestDocumentInitializer.getUser("Admin");
        User user = mapperUtil.convert(userDto, new User());
        user.setIsDeleted(false);
        // when
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        Throwable throwable = catchThrowable(() -> {
            userService.delete(user.getId());
        });
        assertTrue(user.getIsDeleted());
        assertNotEquals(userDto.getUsername(), user.getUsername());
        assertNull(throwable);
    }

    @ParameterizedTest
    @MethodSource(value = "email")
    void isEmailExist(UserDto userDto, User user, boolean expected) {
        // when
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.ofNullable(user));
        // then
        assertEquals(expected, userService.isEmailExist(userDto));
    }

    static Stream<Arguments> email(){
        // given
        UserDto userDto = TestDocumentInitializer.getUser("Admin");
        User user = new MapperUtil(new ModelMapper())
                .convert(userDto, new User());
        user.setId(2L);
        return Stream.of(
                arguments(userDto, user, true),
                arguments(userDto, null, false)
        );
    }


}