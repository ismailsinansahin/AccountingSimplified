package com.cydeo.accountingsimplified.service.implementation;

import com.cydeo.accountingsimplified.dto.UserDto;
import com.cydeo.accountingsimplified.entity.Company;
import com.cydeo.accountingsimplified.mapper.MapperUtil;
import com.cydeo.accountingsimplified.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;

public class CommonService {
    public final UserService userService;
    public final MapperUtil mapperUtil;
    public Company company;

    public CommonService(UserService userService, MapperUtil mapperUtil) {
        this.userService = userService;
        this.mapperUtil = mapperUtil;
    }

    public Company getCompanyByLoggedInUser() {
        var currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDto userDTO = userService.findByUsername(currentUsername);
        company = mapperUtil.convert(userDTO.getCompany(), new Company());
        return company;
    }
}
