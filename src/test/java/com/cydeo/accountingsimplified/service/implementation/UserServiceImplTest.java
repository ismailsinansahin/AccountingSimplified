package com.cydeo.accountingsimplified.service.implementation;

import com.cydeo.accountingsimplified.dto.CompanyDto;
import com.cydeo.accountingsimplified.dto.RoleDto;
import com.cydeo.accountingsimplified.dto.UserDto;
import com.cydeo.accountingsimplified.entity.Company;
import com.cydeo.accountingsimplified.entity.Role;
import com.cydeo.accountingsimplified.entity.User;
import com.cydeo.accountingsimplified.exception.AccountingException;
import com.cydeo.accountingsimplified.mapper.MapperUtil;
import com.cydeo.accountingsimplified.mapper.UserMapper;
import com.cydeo.accountingsimplified.repository.UserRepository;
import com.cydeo.accountingsimplified.service.SecurityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.*;
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
    UserMapper userMapper;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    void findUserById_HappyPath() {
        User user = getUser("test@test.com");
        UserDto userDto = getUserDto("test@test.com");
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(mapperUtil.convert(any(User.class), any(UserDto.class))).thenReturn(userDto);
//        when(mapperUtil.convert(any(User.class), ArgumentMatchers.<Class<UserDto>>any())).thenReturn(UserDto.class);
//        when(mapperUtil.convert(user, any(UserDto.class))).thenReturn(userDto); // exceptions.misusing.InvalidUseOfMatchersException
//        when(mapperUtil.convert(user, new UserDto())).thenReturn(userDto); // mockito.exceptions.misusing.PotentialStubbingProblem
//        when(userMapper.convertToDto(user)).thenReturn(userDto);

        Throwable throwable = catchThrowable(()-> userService.findUserById(anyLong()));
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
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(mapperUtil.convert(any(User.class), any(UserDto.class)))
                .thenReturn(userDto);

        Throwable throwable = catchThrowable(()-> userService.findByUsername(anyString()));
        assertNull(throwable);
    }

    @Test
    void findByUsername_Exception_UserNotFound() {
        Throwable throwable = catchThrowable(()-> userService.findByUsername(anyString()));
        assertInstanceOf(AccountingException.class,throwable);
        assertEquals("User not found", throwable.getMessage());
    }

    @Test
    void getFilteredUsers_forAnyUserExceptRootUser() {
        List<User> userList = getUsers();
        List<UserDto> userDtos = getUserDTOs();
//        when(userRepository.findAllByRole_Description(anyString())).thenReturn(userList);
        when(userRepository.findAllByCompany_Title(anyString())).thenReturn(userList);
//        when(userRepository.findAllByCompany_Title(anyString())).then(returnsFirstArg());
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userList.get(0)));
        when(mapperUtil.convert(any(User.class), any(UserDto.class)))
                .thenReturn(userDtos.get(0));
        when(securityService.getLoggedInUser()).thenReturn(userDtos.get(0));

        Throwable throwable = catchThrowable(()-> userService.getFilteredUsers());
        assertNull(throwable);
        List<UserDto> result = userService.getFilteredUsers();
        assertEquals("admin@bluetech.com", result.get(0).getUsername());
    }

    @Test
    void getFilteredUsers_forRootUser() {
        List<User> userList = getUsers();
        userList.get(0).getRole().setDescription("root user");
        List<UserDto> userDtos = getUserDTOs();
        userDtos.get(0).getRole().setDescription("root user");
        when(userRepository.findAllByRole_Description(anyString())).thenReturn(userList);
        when(mapperUtil.convert(any(User.class), any(UserDto.class)))
                .thenReturn(userDtos.get(0));
        when(securityService.getLoggedInUser()).thenReturn(userDtos.get(0));

        Throwable throwable = catchThrowable(()-> userService.getFilteredUsers());
        assertNull(throwable);
        List<UserDto> result = userService.getFilteredUsers();
        assertEquals("admin@bluetech.com", result.get(0).getUsername());
    }

    // birleştirince hata veriyor:
    // Unnecessary stubbings detected.
    // Clean & maintainable test code requires zero unnecessary code.
    // Following stubbings are unnecessary (click to navigate to relevant line of code):
    //   1. -> at com.cydeo.accountingsimplified.service.implementation.UserServiceImplTest.getFilteredUsers_forRootUser(UserServiceImplTest.java:144)
    //   2. -> at com.cydeo.accountingsimplified.service.implementation.UserServiceImplTest.getFilteredUsers_forRootUser(UserServiceImplTest.java:145)
    @ParameterizedTest
    @ValueSource(strings = {"root user", "Admin"})
    void getFilteredUsers_forRootUser(String role) {
        List<User> userList = getUsers();
        userList.get(0).getRole().setDescription(role);
        List<UserDto> userDtos = getUserDTOs();
        userDtos.get(0).getRole().setDescription(role);
        when(userRepository.findAllByRole_Description(anyString())).thenReturn(userList);
        when(userRepository.findAllByCompany_Title(anyString())).thenReturn(userList);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userList.get(0)));
        when(mapperUtil.convert(any(User.class), any(UserDto.class)))
                .thenReturn(userDtos.get(0));
        when(securityService.getLoggedInUser()).thenReturn(userDtos.get(0));

        Throwable throwable = catchThrowable(()-> userService.getFilteredUsers());
        assertNull(throwable);
        List<UserDto> result = userService.getFilteredUsers();
        assertEquals("admin@bluetech.com", result.get(0).getUsername());
    }

    private User getUser(String username){
        return User.builder()
                .id(1L)
                .username(username)
                .firstname("Tom")
                .lastname("Hanks")
                .role(new Role("Admin"))
                .build();
    }

    private UserDto getUserDto(String username){
        return UserDto.builder()
                .id(1L)
                .username(username)
                .firstname("Tom")
                .lastname("Hanks")
                .role(new RoleDto(1L, "Admin"))
                .company(CompanyDto.builder().build())
                .build();
    }

    private List<User> getUsers(){
        return List.of(
                User.builder()
                        .company(Company.builder().title("Blue Tech").build())
                        .role(new Role("Admin"))
                        .firstname("Chris")
                        .lastname("Brown")
                        .username("admin@bluetech.com")
                        .phone("123456789")
                        .build(),
                User.builder()
                        .company(Company.builder().title("Green Tech").build())
                        .role(new Role("Admin"))
                        .firstname("Mary")
                        .lastname("Grant")
                        .username("admin@bluetech.com")
                        .phone("123456789")
                        .build()
        );
    }

    private List<UserDto> getUserDTOs(){
        return List.of(
                UserDto.builder()
                        .company(CompanyDto.builder().title("Blue Tech").build())
                        .role(new RoleDto(1L, "Admin"))
                        .firstname("Chris")
                        .lastname("Brown")
                        .username("admin@bluetech.com")
                        .phone("123456789")
                        .build(),
                UserDto.builder()
                        .company(CompanyDto.builder().title("Green Tech").build())
                        .role(new RoleDto(1L, "Admin"))
                        .firstname("Mary")
                        .lastname("Grant")
                        .username("admin@bluetech.com")
                        .phone("123456789")
                        .build()
        );
    }
}