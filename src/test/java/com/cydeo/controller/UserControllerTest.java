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

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
//@Import(SecurityConfig.class)
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

//    @MockBean
//    Authentication authentication;
//
//    @MockBean
//    UserPrincipal userPrincipal;
//
//    @MockBean
//    Principal principal;

    @Test
    void listUsers() throws Exception {
        when(userService.getFilteredUsers()).thenReturn(getUsers());
//        lenient().when(authentication.getPrincipal()).thenReturn(getUsers().get(0));
//        when(userPrincipal.getUsername()).thenReturn(getUsers().get(0).getUsername());
//        when(principal.getName()).thenReturn(getUsers().get(0).getUsername());

//        TemplateProcessingException: Exception evaluating SpringEL expression: "user.isOnlyAdmin || #authentication.principal.getUsername().equals(user.username)" (template: "user/user-list" - line 65, col 75)
        mockMvc.perform(get("/users/list")
//                        .header("username", "root@cydeo.com")
//                        .header("password", "Abc1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("user-list"))
                .andExpect(model().attributeExists("users"))
                .andDo(print());
    }

    @Test
    void create_user_get() {

    }

    @Test
    void create_user_post() throws Exception {
        when(userService.isEmailExist(getUsers().get(0))).thenReturn(false);
        when(userService.save(getUsers().get(0))).thenReturn(getUsers().get(0));

        mockMvc.perform(MockMvcRequestBuilders.post("/users/create")
                        .param("firstname", "duke")
                        .param("lastname", "duke")
                        .param("username", "C0124@gmail.com")
                        .param("phone", "+111 123 456 789")
                        .param("password", "Abc1")
                        .param("confirmPassword", "Abc1"))
//                        .param("role", String.valueOf(new RoleDto(1L, "Admin")))
//                        .param("company", "Blue Tech")
//                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/users/list"))
                .andDo(print());
    }

    private List<UserDto> getUsers() {
        return List.of(
                UserDto.builder()
                        .company(CompanyDto.builder().title("Blue Tech").build())
                        .role(new RoleDto(1L, "Admin"))
                        .firstname("Chris")
                        .lastname("Brown")
                        .username("admin@bluetech.com")
                        .phone("123456789")
                        .isOnlyAdmin(false)
                        .build(),
                UserDto.builder()
                        .company(CompanyDto.builder().title("Green Tech").build())
                        .role(new RoleDto(1L, "Admin"))
                        .firstname("Mary")
                        .lastname("Grant")
                        .username("admin@bluetech.com")
                        .phone("123456789")
                        .isOnlyAdmin(false)
                        .build()
        );
    }

    // converts any object to Json
//    private static String toJsonString(final Object obj) {
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
//            objectMapper.registerModule(new JavaTimeModule());
//            return objectMapper.writeValueAsString(obj);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
}