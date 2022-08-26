package com.cydeo.accountingsimplified.service.implementation;

import com.cydeo.accountingsimplified.dto.AddressDto;
import com.cydeo.accountingsimplified.dto.CompanyDto;
import com.cydeo.accountingsimplified.dto.RoleDto;
import com.cydeo.accountingsimplified.dto.UserDto;
import com.cydeo.accountingsimplified.entity.Address;
import com.cydeo.accountingsimplified.entity.Company;
import com.cydeo.accountingsimplified.entity.Role;
import com.cydeo.accountingsimplified.entity.User;
import com.cydeo.accountingsimplified.mapper.MapperUtil;
import com.cydeo.accountingsimplified.repository.CompanyRepository;
import com.cydeo.accountingsimplified.repository.RoleRepository;
import com.cydeo.accountingsimplified.repository.UserRepository;
import com.cydeo.accountingsimplified.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final RoleRepository roleRepository;
    private final MapperUtil mapperUtil;

    public UserServiceImpl(UserRepository userRepository, CompanyRepository companyRepository, RoleRepository roleRepository, MapperUtil mapperUtil) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.roleRepository = roleRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public UserDto findUserById(Long id) {
        User user = userRepository.findUserById(id);
        return mapperUtil.convert(user, new UserDto());
    }

    @Override
    public List<UserDto> getAllUsers() {
        User currentUser = getCurrentUser();
        if (currentUser.getRole().getDescription().equals("Root User")) {
            Role role1 = roleRepository.findByDescription("Root User");
            Role role2 = roleRepository.findByDescription("Admin");
            return userRepository.findAllByRoleOrRole(role1, role2)
                    .stream()
                    .map(each -> mapperUtil.convert(each, new UserDto()))
                    .collect(Collectors.toList());
        } else {
            return userRepository.findAllByCompany(getCurrentUser().getCompany())
                    .stream()
                    .map(each -> mapperUtil.convert(each, new UserDto()))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public UserDto create(UserDto userDto) {
        User user = mapperUtil.convert(userDto, new User());
        if(getCurrentUser().getRole().getDescription().equals("Admin")){
            user.setCompany(getCurrentUser().getCompany());
        }
        userRepository.save(user);
        return mapperUtil.convert(user, userDto);
    }

    @Override
    public List<RoleDto> getAllRoles() {
        return roleRepository.findAll()
        .stream()
        .map(each -> mapperUtil.convert(each, new RoleDto()))
        .collect(Collectors.toList());
    }

    @Override
    public CompanyDto getCompanyOfNewUser() {
        User user = getCurrentUser();
        return mapperUtil.convert(user.getCompany(), new CompanyDto());
    }

    @Override
    public UserDto update(Long userId, UserDto userDto) {
        User user = userRepository.findById(userId).get();
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setUsername(userDto.getUsername());
        user.setPhone(userDto.getPhone());
        user.setPassword(userDto.getPassword());
        Role role = roleRepository.findById(userDto.getRole().getId()).get();
        user.setRole(role);
        userRepository.save(user);
        return mapperUtil.convert(user, userDto);
    }

    @Override
    public void delete(Long userId) {
        User user = userRepository.findUserById(userId);
        user.setIsDeleted(true);
        userRepository.save(user);
    }

    public User getCurrentUser(){
        return userRepository.findUserById(3L);
    }

}
