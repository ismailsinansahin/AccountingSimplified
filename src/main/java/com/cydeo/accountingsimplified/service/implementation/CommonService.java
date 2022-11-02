package com.cydeo.accountingsimplified.service.implementation;

import com.cydeo.accountingsimplified.mapper.MapperUtil;
import com.cydeo.accountingsimplified.service.UserService;

public class CommonService {

    public final UserService userService;
    public final MapperUtil mapperUtil;

    public CommonService(UserService userService, MapperUtil mapperUtil) {
        this.userService = userService;
        this.mapperUtil = mapperUtil;
    }
}
