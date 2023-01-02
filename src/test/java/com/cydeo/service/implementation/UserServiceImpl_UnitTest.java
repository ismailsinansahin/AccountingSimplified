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

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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

    //    @Mock çalışmıyor
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
    void getFilteredUsers_given_logged_in_as_Admin() {
        List<User> userList = getUsers();
        when(securityService.getLoggedInUser()).thenReturn(getUserDto("Admin"));
        when(userRepository.findAllByCompany(any(Company.class))).thenReturn(userList);

        Throwable throwable = catchThrowable(() -> {
            List<UserDto> actualUsers = userService.getFilteredUsers();
            assertEquals("Active Tech", actualUsers.get(0).getCompany().getTitle());    // checks sorting
        });
        assertNull(throwable);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Root User", "admin"})
    void getFilteredUsers_given_logged_in_as_root_user_or_admin(String role) {
        List<User> userList = getUsers();
        when(securityService.getLoggedInUser()).thenReturn(getUserDto(role));
        lenient().when(userRepository.findAllByRole_Description("Admin")).thenReturn(userList);
        lenient().when(userRepository.findAllByCompany(any(Company.class))).thenReturn(userList);

        Throwable throwable = catchThrowable(() -> {
            List<UserDto> actualUsers = userService.getFilteredUsers();
            assertEquals("Active Tech", actualUsers.get(0).getCompany().getTitle());    // checks sorting
        });

        if (role.equals("Root User")) verify(userRepository).findAllByRole_Description("Admin");
        else verify(userRepository).findAllByCompany(any());

        assertNull(throwable);
    }

    @Test
    void save_happyPath(){
        User user = getUser();
        when(passwordEncoder.encode(anyString())).thenReturn("Abc1");
        when(userRepository.save(any(User.class))).thenReturn(user);

        Throwable throwable = catchThrowable(() -> {
            UserDto actualUser = userService.save(getUserDto("Admin"));
            assertEquals(user.getUsername(), actualUser.getUsername());
        });
        assertNull(throwable);
    }

    @Test
    void save_null_dto_throws_exception(){
        // only checks exception which comes from mapperUtil
        assertThrows(IllegalArgumentException.class, ()->userService.save(null));

        // check exception and message
        Throwable throwable = catchThrowable(() -> userService.save(null));
        assertInstanceOf(IllegalArgumentException.class, throwable);
        assertEquals("source cannot be null", throwable.getMessage());

        //validation of username, password, etc. should be done on controller side. that's why no test case is written here
    }

    @Test
    void update_happyPath(){
        User user = getUser();
        when(passwordEncoder.encode(anyString())).thenReturn("Abc1");
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        Throwable throwable = catchThrowable(() -> {
            UserDto actualUser = userService.update(getUserDto("Admin"));
            assertEquals(user.getUsername(), actualUser.getUsername());
        });
        assertNull(throwable);
        // since we tested
    }

    @Test
    void update_null_dto_throws_exception(){
        // only checks exception which comes from mapperUtil
        assertThrows(IllegalArgumentException.class, ()->userService.update(null));

        // check exception and message
        Throwable throwable = catchThrowable(() -> userService.update(null));
        assertInstanceOf(IllegalArgumentException.class, throwable);
        assertEquals("source cannot be null", throwable.getMessage());

        //validation of username, password, etc. should be done on controller side. that's why no test case is written here
    }

    @Test
    void delete_happyPath(){
        User user = getUser();
        user.setIsDeleted(false);
        // todo : can we mock a method which belongs to code under test class?
        //  can we test setUsername, setIsDeleted method?
        //  can we have negative test?
//        when(userService.findUserById(anyLong())).thenReturn(getUserDto("Admin"));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        Throwable throwable = catchThrowable(() -> {
            userService.delete(user.getId());
        });
        assertTrue(user.getIsDeleted());
        assertNotEquals("test@test.com", user.getUsername());
        assertNull(throwable);
    }

    @Test
    void isEmailExist_return_false(){
        User user = getUser();
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        assertFalse(userService.isEmailExist(getUserDto("admin")));
    }

    @Test
    void isEmailExist_return_true(){
        User user = getUser();
        user.setId(2L);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        assertTrue(userService.isEmailExist(getUserDto("admin")));
    }

    // todo : private methodlar test edilemiyor, kullanmalımıyız
    @ParameterizedTest
    @MethodSource(value = "input")
    void checkIfOnlyAdminForCompany_happyPath(int number, boolean expected) {
        when(userRepository.countAllByCompanyAndRole_Description(any(Company.class), any())).thenReturn(number);
        assertEquals(expected, userService.checkIfOnlyAdminForCompany(getUserDto("Admin")));
    }

    static Stream<Arguments> input() {
        return Stream.of(
                arguments(1, true),
                arguments(2, false),
                arguments(3, false),
                arguments(0, false)
        );
    }

    private User getUser() {
        return User.builder()
                .id(1L)
                .username("test@test.com")
                .firstname("Tom")
                .lastname("Hanks")
                .password("Abc1")
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
                .password("Abc1")
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