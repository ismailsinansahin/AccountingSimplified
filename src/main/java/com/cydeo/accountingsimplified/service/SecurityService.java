package com.cydeo.accountingsimplified.service;

import com.cydeo.accountingsimplified.dto.UserDto;

public interface SecurityService  {

    UserDto getLoggedInUser();

}
