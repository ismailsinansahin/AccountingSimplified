package com.cydeo.service.implementation;

import com.cydeo.dto.UserDto;
import com.cydeo.entity.Company;
import com.cydeo.entity.User;
import com.cydeo.exception.AccountingException;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.SecurityService;
import com.cydeo.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final SecurityService securityService;
    private final MapperUtil mapperUtil;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,@Lazy SecurityService securityService,
                           MapperUtil mapperUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.securityService = securityService;
        this.mapperUtil = mapperUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto findUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow( () -> new AccountingException("User not found"));
        UserDto dto = mapperUtil.convert(user, new UserDto());
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
        User currentUser = mapperUtil.convert(securityService.getLoggedInUser(), new User());
        List<User> userList;
        if (currentUser.getRole().getDescription().equals("Root User")) {
            userList = userRepository.findAllByRole_Description("Admin");
        } else {
            userList = userRepository.findAllByCompany(currentUser.getCompany());
        }
        return userList.stream()
                .sorted(Comparator.comparing((User u) -> u.getCompany().getTitle())
                        .thenComparing(u -> u.getRole().getDescription()))
                .map(entity -> mapperUtil.convert(entity, new UserDto()))
                .peek(dto -> dto.setIsOnlyAdmin(this.checkIfOnlyAdminForCompany(dto)))
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
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(()-> new NoSuchElementException("this user does not exist"));
        updatedUser.setEnabled(user.isEnabled());
        User savedUser = userRepository.save(updatedUser);
        return mapperUtil.convert(savedUser, userDto);
    }

    @Override
    public void delete(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new NoSuchElementException("this user does not exist"));
        user.setUsername(user.getUsername() + "-" + user.getId());  // without this modification, if entity has column(unique=true)
                                                                    // and we want to save a user with same email, it throws exception.
        user.setIsDeleted(true);
        userRepository.save(user);
    }

    @Override
    public Boolean emailExist(UserDto userDto) {
        User userWithUpdatedEmail = userRepository.findByUsername(userDto.getUsername())
                .orElse(null);
        if (userWithUpdatedEmail == null) return false;
        return !userWithUpdatedEmail.getId().equals(userDto.getId());
    }


    protected Boolean checkIfOnlyAdminForCompany(UserDto dto) {
        Company company = mapperUtil.convert(dto.getCompany(), new Company());
        return userRepository.countAllByCompanyAndRole_Description(company,"Admin") == 1;
    }


}
