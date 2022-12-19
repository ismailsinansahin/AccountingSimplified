package com.cydeo.accountingsimplified.controller;

import com.cydeo.accountingsimplified.dto.CompanyDto;
import com.cydeo.accountingsimplified.dto.RoleDto;
import com.cydeo.accountingsimplified.dto.UserDto;
import com.cydeo.accountingsimplified.entity.Role;
import com.cydeo.accountingsimplified.service.CompanyService;
import com.cydeo.accountingsimplified.service.RoleService;
import com.cydeo.accountingsimplified.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    RoleService roleService;

    @MockBean
    CompanyService companyService;

    @Test
    void listUsers() throws Exception {
        when(userService.getFilteredUsers()).thenReturn(getUsers());

        mockMvc.perform(MockMvcRequestBuilders.get("/users/list")
                        .header("username", "root@cydeo.com")
                        .header("password", "Abc1"))
                .andDo(print());
    }

    @Test
    void navigateToUserCreate() {

    }

    @Test
    void createNewUser() throws Exception {
        when(userService.emailExist(getUsers().get(0))).thenReturn(false);
        when(userService.save(getUsers().get(0))).thenReturn(getUsers().get(0));

        mockMvc.perform(MockMvcRequestBuilders.post("/users/create"))
                .andDo(print());
    }

    private List<UserDto> getUsers(){
        return List.of(
                UserDto.builder()
                        .company(CompanyDto.builder().title("Blue Tech").build())
                        .role(new RoleDto(1L, "Admin"))
                        .firstname("Chris")
                        .lastname("Brown")
                        .username("admin@bluetech.com")
                        .phone("123456789")
                        .build(),
                UserDto.builder()
                        .company(CompanyDto.builder().title("Green Tech").build())
                        .role(new RoleDto(1L, "Admin"))
                        .firstname("Mary")
                        .lastname("Grant")
                        .username("admin@bluetech.com")
                        .phone("123456789")
                        .build()
        );
    }
}