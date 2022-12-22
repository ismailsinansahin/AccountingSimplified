package com.cydeo.controller;

import com.cydeo.dto.CompanyDto;
import com.cydeo.dto.RoleDto;
import com.cydeo.dto.UserDto;
import com.cydeo.service.CompanyService;
import com.cydeo.service.RoleService;
import com.cydeo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.StatusResultMatchers.*;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.http.ResponseEntity.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@AutoConfigureMockMvc(addFilters = false)
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
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
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

    // converts any object to Json
    private static String toJsonString(final Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}