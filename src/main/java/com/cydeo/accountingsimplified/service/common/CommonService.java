package com.cydeo.accountingsimplified.service.common;

import com.cydeo.accountingsimplified.entity.Company;
import com.cydeo.accountingsimplified.entity.User;
import com.cydeo.accountingsimplified.mapper.MapperUtil;
import com.cydeo.accountingsimplified.service.SecurityService;
import org.springframework.stereotype.Service;

@Service
public class CommonService {

    protected final SecurityService securityService;
    protected final MapperUtil mapperUtil;

    public CommonService(SecurityService securityService, MapperUtil mapperUtil) {
        this.securityService = securityService;
        this.mapperUtil = mapperUtil;
    }

    protected Company getCompany(){
        return mapperUtil.convert(securityService.getLoggedInUser().getCompany(), new Company());
    }

    protected User getCurrentUser(){
        return mapperUtil.convert(securityService.getLoggedInUser(), new User());
    }




}
