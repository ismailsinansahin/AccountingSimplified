package com.cydeo.service.implementation;

import com.cydeo.dto.UserDto;
import com.cydeo.entity.User;
import com.cydeo.exception.AccountingException;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.RoleService;
import com.cydeo.service.SecurityService;
import com.cydeo.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final SecurityService securityService;
    private final MapperUtil mapperUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, RoleService roleService,
                           @Lazy SecurityService securityService, MapperUtil mapperUtil, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.securityService = securityService;
        this.mapperUtil = mapperUtil;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto findUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow( () -> new AccountingException("User not found"));
        UserDto dto = mapperUtil.convert(user, new UserDto());
//        UserDto dto = userMapper.convertToDto(user);
        dto.setIsOnlyAdmin(checkIfOnlyAdminForCompany(dto));
        return dto;
    }

    @Override
    public UserDto findByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow( () -> new AccountingException("User not found"));
        return mapperUtil.convert(user, new UserDto());
    }

    @Override
    public List<UserDto> getFilteredUsers() {
        List<User> userList;
        if (isCurrentUserRootUser()) {
            userList = userRepository.findAllByRole_Description("Admin");
        } else {
            userList = userRepository.findAllByCompany_Title(getCurrentUserCompanyTitle());
        }
        return userList.stream()
                .sorted(Comparator.comparing((User u) -> u.getCompany().getTitle()).thenComparing(u -> u.getRole().getDescription()))
                .map(entity -> {
                    UserDto dto = mapperUtil.convert(entity, new UserDto());
                    dto.setIsOnlyAdmin(checkIfOnlyAdminForCompany(dto));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public UserDto save(UserDto userDto) {
        User user = mapperUtil.convert(userDto, new User());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        userRepository.save(user);
        return mapperUtil.convert(user, userDto);
    }

    @Override
    public UserDto update(UserDto userDto) {
        User updatedUser = mapperUtil.convert(userDto, new User());
        updatedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        updatedUser.setEnabled(userRepository.findUserById(userDto.getId()).isEnabled());
        User savedUser = userRepository.save(updatedUser);
        return mapperUtil.convert(savedUser, userDto);
    }

    @Override
    public void delete(Long userId) {
        User user = userRepository.findUserById(userId);
        user.setUsername(user.getUsername() + "-" + user.getId());  // without this modification, if entity has column(unique=true)
                                                                    // and we want to save a user with same email, it throws exception.
        user.setIsDeleted(true);
        userRepository.save(user);
    }

    @Override
    public Boolean emailExist(UserDto userDto) {
        User userWithUpdatedEmail = userRepository.findByUsername(userDto.getUsername()).orElseThrow( () -> new AccountingException("User not found"));
        if (userWithUpdatedEmail == null) return false;
        return !userWithUpdatedEmail.getId().equals(userDto.getId());
    }

    private Boolean checkIfOnlyAdminForCompany(UserDto dto) {
        if (dto.getRole().getDescription().equalsIgnoreCase("Admin")) {
            List<User> users = userRepository.findAllByCompany_TitleAndRole_Description(dto.getCompany().getTitle(), "Admin");
            return users.size() == 1;
        }
        return false;
    }

    private Boolean isCurrentUserRootUser() {
        return securityService.getLoggedInUser().getRole().getDescription().equalsIgnoreCase("root user");
    }

    private String getCurrentUserCompanyTitle() {
        String currentUserName = securityService.getLoggedInUser().getUsername();
        User user = userRepository.findByUsername(currentUserName).orElseThrow(()-> new AccountingException("User not found"));
        return user.getCompany().getTitle();
    }

}
