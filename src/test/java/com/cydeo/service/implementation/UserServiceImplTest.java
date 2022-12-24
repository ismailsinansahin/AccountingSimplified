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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

//    Mockito.mockitoSession()
//            .initMocks(this)
//  .strictness(Strictness.STRICT_STUBS)
//  .startMocking();

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
//    @Autowired
    UserServiceImpl userService;

    @Test
    void should_find_user_by_id_happy_path() {
        User user = getUser("test@test.com");
        UserDto userDto = getUserDto("test@test.com");
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
//        when(mapperUtil.convert(any(User.class), any(UserDto.class))).thenReturn(userDto);
//        when(mapperUtil.convert(any(User.class), ArgumentMatchers.<Class<UserDto>>any())).thenReturn(UserDto.class);
//        when(mapperUtil.convert(user, any(UserDto.class))).thenReturn(userDto); // exceptions.misusing.InvalidUseOfMatchersException
//        when(mapperUtil.convert(user, new UserDto())).thenReturn(userDto); // mockito.exceptions.misusing.PotentialStubbingProblem
//        when(userMapper.convertToDto(user)).thenReturn(userDto);

        Throwable throwable = catchThrowable(()-> {
            UserDto actualUser = userService.findUserById(anyLong());
            assertEquals("test@test.com", actualUser.getUsername());
        });
        assertNull(throwable);
    }

    @Test
    void findUserById_should_throw_accounting_exception_with_user_not_found_text() {
        Throwable throwable = catchThrowable(()-> userService.findUserById(1L));
        assertInstanceOf(AccountingException.class,throwable);
        assertEquals("User not found", throwable.getMessage());
    }

    @Test
    void should_find_by_username_happy_path() {
        User user = getUser("test@test.com");
        UserDto userDto = getUserDto("test@test.com");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
//        when(mapperUtil.convert(any(User.class), any(UserDto.class))).thenReturn(userDto);

        Throwable throwable = catchThrowable(()-> userService.findByUsername(anyString()));
        assertNull(throwable);
    }

    @Test
    void findByUsername_should_throw_accounting_exception_with_user_not_found_text() {
        Throwable throwable = catchThrowable(()-> userService.findByUsername(anyString()));
        assertInstanceOf(AccountingException.class,throwable);
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

        Throwable throwable = catchThrowable(()-> {
            List<UserDto> users = userService.getFilteredUsers();
            assertEquals("admin@bluetech.com", users.get(0).getUsername());
        });
        assertNull(throwable);
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

    private User getUser(String username){
        return User.builder()
                .id(1L)
                .username(username)
                .firstname("Tom")
                .lastname("Hanks")
                .company(Company.builder().title("Blue Tech").id(1L).build())
                .role(new Role("Admin"))
                .build();
    }

    private UserDto getUserDto(String username){
        return UserDto.builder()
                .id(1L)
                .username(username)
                .firstname("Tom")
                .lastname("Hanks")
                .company(CompanyDto.builder().title("Blue Tech").id(1L).build())
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
//                        .build(),
//                User.builder()
//                        .company(Company.builder().title("Green Tech").build())
//                        .role(new Role("Admin"))
//                        .firstname("Mary")
//                        .lastname("Grant")
//                        .username("admin@bluetech.com")
//                        .phone("123456789")
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
//                        .build(),
//                UserDto.builder()
//                        .company(CompanyDto.builder().title("Green Tech").build())
//                        .role(new RoleDto(1L, "Admin"))
//                        .firstname("Mary")
//                        .lastname("Grant")
//                        .username("admin@bluetech.com")
//                        .phone("123456789")
                        .build()
        );
    }
}