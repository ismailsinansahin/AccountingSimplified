package com.cydeo.accountingsimplified.service.implementation;

import com.cydeo.accountingsimplified.dto.RoleDto;
import com.cydeo.accountingsimplified.dto.UserDto;
import com.cydeo.accountingsimplified.entity.Role;
import com.cydeo.accountingsimplified.mapper.MapperUtil;
import com.cydeo.accountingsimplified.repository.RoleRepository;
import com.cydeo.accountingsimplified.service.RoleService;
import com.cydeo.accountingsimplified.service.SecurityService;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Lazy;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final SecurityService securityService;
    private final MapperUtil mapperUtil;

    public RoleServiceImpl(RoleRepository roleRepository, @Lazy SecurityService securityService, MapperUtil mapperUtil) {
        this.roleRepository = roleRepository;
        this.securityService = securityService;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public RoleDto findRoleById(Long id) {
        return mapperUtil.convert(roleRepository.findRoleById(id), new RoleDto());
    }

    @Override
    public RoleDto findByDescription(String description) {
        Role role = roleRepository.findByDescription(description);
        return mapperUtil.convert(role, new RoleDto());
    }

    @Override
    public List<RoleDto> getAllRolesForCurrentUser(){
        UserDto user = securityService.getLoggedInUser();
        if(user.getRole().getDescription().equals("Root User")){
            return findAllByDescriptionOrDescription("Root User", "Admin");
        }
        else {
            return roleRepository.findAll()
                    .stream()
                    .map(role -> mapperUtil.convert(role, new RoleDto()))
                    .collect(Collectors.toList());
        }
    }

    public List<RoleDto> findAllByDescriptionOrDescription(String description, String description2) {
        List<Role> roles = roleRepository.findAllByDescriptionOrDescription(description, description2);
        return roles.stream()
                .map(role -> mapperUtil.convert(role, new RoleDto()))
                .collect(Collectors.toList());
    }

}
