package com.cydeo.service.implementation;

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
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    SecurityService securityService;

    //    @Mock
    @Spy
    MapperUtil mapperUtil = new MapperUtil(new ModelMapper());

//    @Mock
//    UserMapper userMapper;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    void should_find_user_by_id_happy_path() {
        User user = getUser();
        UserDto userDto = getUserDto("Root User");
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
//        when(mapperUtil.convert(any(User.class), any(UserDto.class))).thenReturn(userDto);
//        when(mapperUtil.convert(any(User.class), ArgumentMatchers.<Class<UserDto>>any())).thenReturn(UserDto.class);
//        when(mapperUtil.convert(user, any(UserDto.class))).thenReturn(userDto); // exceptions.misusing.InvalidUseOfMatchersException
//        when(mapperUtil.convert(user, new UserDto())).thenReturn(userDto); // mockito.exceptions.misusing.PotentialStubbingProblem
//        when(userMapper.convertToDto(user)).thenReturn(userDto);

        Throwable throwable = catchThrowable(() -> {
            UserDto actualUser = userService.findUserById(anyLong());
            assertEquals("test@test.com", actualUser.getUsername());
        });
        assertNull(throwable);
    }

    @Test
    void findUserById_should_throw_accounting_exception_with_user_not_found_text() {
        // it throws exception since no mock of userRepository and userRepository.findById(1L) returns null
        Throwable throwable = catchThrowable(() -> userService.findUserById(1L));
        // assertThrows also works, but we can also assert message with catchThrowable
        // assertThrows(AccountingException.class, ()-> userService.findUserById(1L));
        assertInstanceOf(AccountingException.class, throwable);
        assertEquals("User not found", throwable.getMessage());
    }

    @Test
    void should_find_by_username_happy_path() {
        User user = getUser();
        UserDto userDto = getUserDto("Root User");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
//        when(mapperUtil.convert(any(User.class), any(UserDto.class))).thenReturn(userDto);

        Throwable throwable = catchThrowable(() -> {
            UserDto actualUser = userService.findByUsername(anyString());
            assertEquals(user.getUsername(), actualUser.getUsername());
        });
        assertNull(throwable);
    }

    @Test
    void findByUsername_should_throw_accounting_exception_with_user_not_found_text() {
        Throwable throwable = catchThrowable(() -> userService.findByUsername(anyString()));
        assertInstanceOf(AccountingException.class, throwable);
        assertEquals("User not found", throwable.getMessage());
    }

    @Test
    void getFilteredUsers_forAnyUserExceptRootUser() {
        List<User> userList = getUsers();
        List<UserDto> userDtos = getUserDTOs();
//        when(userRepository.findAllByRole_Description(anyString())).thenReturn(userList);
//        when(userRepository.findAllByCompany_Title(anyString())).thenReturn(userList);
        when(securityService.getLoggedInUser()).thenReturn(userDtos.get(0));
//        when(mapperUtil.convert(userList.get(0), new UserDto())).thenReturn(userDtos.get(0));
        when(userRepository.findAllByCompany(any(Company.class))).thenReturn(userList);

        //    when(userRepository.findByUsername(userDtos.get(0).getUsername())).thenReturn(Optional.of(userList.get(0)));
//        when(mapperUtil.convert(any(User.class), any(UserDto.class))).thenReturn(userDtos.get(0));

        // checkIfOnlyAdminForCompany :
//        when(mapperUtil.convert(userDtos.get(0).getCompany(), new Company())).thenReturn(userList.get(0).getCompany());
//        when(userRepository.countAllByCompanyAndRole_Description(userList.get(0).getCompany(),"admin"))
//                .thenReturn(2);

        Throwable throwable = catchThrowable(() -> {
            List<UserDto> actualUsers = userService.getFilteredUsers();
            assertEquals("admin@bluetech.com", actualUsers.get(0).getUsername());
        });
        assertNull(throwable);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Root User", "admin"})
    void getFilteredUsers_happyPath(String role) {
        List<User> userList = getUsers();
        when(securityService.getLoggedInUser()).thenReturn(getUserDto(role));
        lenient().when(userRepository.findAllByRole_Description("Admin")).thenReturn(userList);
        lenient().when(userRepository.findAllByCompany(any(Company.class))).thenReturn(userList);

        Throwable throwable = catchThrowable(() -> {
            List<UserDto> actualUsers = userService.getFilteredUsers();
            assertEquals("Active Tech", actualUsers.get(0).getCompany().getTitle());
        });

        if (role.equals("Root User")) verify(userRepository).findAllByRole_Description("Admin");
        else verify(userRepository).findAllByCompany(any());

        assertNull(throwable);
    }

    @ParameterizedTest
    @MethodSource(value = "input")
    void checkIfOnlyAdminForCompany_happyPath(int number, boolean expected) {
        when(userRepository.countAllByCompanyAndRole_Description(any(Company.class), any())).thenReturn(number);
        assertEquals(expected, userService.checkIfOnlyAdminForCompany(getUserDto("Admin")));
    }

    static Stream<Arguments> input(){
        return Stream.of(
          Arguments.of(1, true),
                Arguments.of(2, false),
                Arguments.of(3, false)
        );
    }

//    @Test
//    void getFilteredUsers_forRootUser() {
//        List<User> userList = getUsers();
//        userList.get(0).getRole().setDescription("root user");
//        List<UserDto> userDtos = getUserDTOs();
//        userDtos.get(0).getRole().setDescription("root user");
//        when(userRepository.findAllByRole_Description(anyString())).thenReturn(userList);
//        when(mapperUtil.convert(any(User.class), any(UserDto.class)))
//                .thenReturn(userDtos.get(0));
//        when(securityService.getLoggedInUser()).thenReturn(userDtos.get(0));
//        when(userRepository.countAllByCompanyAndRole_Description(
//                Company.builder().title("Blue Tech").build(),"root user")).thenReturn(2);
//        when(mapperUtil.convert(any(CompanyDto.class), any(Company.class)))
//                .thenReturn(new Company());
//
//        Throwable throwable = catchThrowable(()-> userService.getFilteredUsers());
//        assertNull(throwable);
//        List<UserDto> result = userService.getFilteredUsers();
//        assertEquals("admin@bluetech.com", result.get(0).getUsername());
//    }

    // birleştirince hata veriyor:
    // Unnecessary stubbings detected.
    // Clean & maintainable test code requires zero unnecessary code.
    // Following stubbings are unnecessary (click to navigate to relevant line of code):
    //   1. -> at com.cydeo.service.implementation.UserServiceImplTest.getFilteredUsers_forRootUser(UserServiceImplTest.java:144)
    //   2. -> at com.cydeo.service.implementation.UserServiceImplTest.getFilteredUsers_forRootUser(UserServiceImplTest.java:145)
//    @ParameterizedTest
//    @ValueSource(strings = {"root user", "Admin"})
//    void get_filtered_users_for_root_user(String role) {
//        List<User> userList = getUsers();
//        userList.get(0).getRole().setDescription(role);
//        List<UserDto> userDtos = getUserDTOs();
//        userDtos.get(0).getRole().setDescription(role);
//        when(userRepository.findAllByRole_Description(anyString())).thenReturn(userList);
//        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userList.get(0)));
//        when(mapperUtil.convert(any(User.class), any(UserDto.class)))
//                .thenReturn(userDtos.get(0));
//        when(userRepository.countAllByCompanyAndRole_Description(new Company(),anyString())).thenReturn(2);
//        when(mapperUtil.convert(any(CompanyDto.class), any(Company.class)))
//                .thenReturn(new Company());
//        when(securityService.getLoggedInUser()).thenReturn(userDtos.get(0));
//
//        Throwable throwable = catchThrowable(()-> userService.getFilteredUsers());
//        assertNull(throwable);
//        List<UserDto> result = userService.getFilteredUsers();
//        assertEquals("admin@bluetech.com", result.get(0).getUsername());
//    }

    private User getUser() {
        return User.builder()
                .id(1L)
                .username("test@test.com")
                .firstname("Tom")
                .lastname("Hanks")
                .company(Company.builder().title("Blue Tech").id(1L).build())
                .role(new Role("Admin"))
                .build();
    }

    private UserDto getUserDto(String role) {
        return UserDto.builder()
                .id(1L)
                .username("test@test.com")
                .firstname("Tom")
                .lastname("Hanks")
                .company(CompanyDto.builder().title("Blue Tech").id(1L).build())
                .role(new RoleDto(1L, role))
                .company(CompanyDto.builder().build())
                .build();
    }

    private static List<User> getUsers() {
        return List.of(
                User.builder()
                        .company(Company.builder().title("Blue Tech").id(2L).build())
                        .role(new Role("Admin"))
                        .firstname("Chris")
                        .lastname("Brown")
                        .username("admin@bluetech.com")
                        .phone("123456789")
                        .build(),
                User.builder()
                        .company(Company.builder().title("Active Tech").id(1L).build())
                        .role(new Role("Manager"))
                        .firstname("Mary")
                        .lastname("Grant")
                        .username("manager@activetech.com")
                        .phone("123456789")
                        .build()
        );
    }

    private static List<UserDto> getUserDTOs() {
        return List.of(
                UserDto.builder()
                        .company(CompanyDto.builder().title("Blue Tech").id(2L).build())
                        .role(new RoleDto(1L, "Admin"))
                        .firstname("Chris")
                        .lastname("Brown")
                        .username("admin@bluetech.com")
                        .phone("123456789")
                        .build(),
                UserDto.builder()
                        .company(CompanyDto.builder().title("Active Tech").id(1L).build())
                        .role(new RoleDto(1L, "Manager"))
                        .firstname("Mary")
                        .lastname("Grant")
                        .username("manager@activetech.com")
                        .phone("123456789")
                        .build()
        );
    }
}