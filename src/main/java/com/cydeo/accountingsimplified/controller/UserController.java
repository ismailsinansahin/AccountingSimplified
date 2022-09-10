package com.cydeo.accountingsimplified.controller;

import com.cydeo.accountingsimplified.dto.UserDto;
import com.cydeo.accountingsimplified.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/list")
    public String navigateToUserList(Model model) throws Exception {
        model.addAttribute("users", userService.getAllUsers());
        return "/user/user-list";
    }

    @GetMapping("/create")
    public String navigateToUserCreate(Model model) throws Exception {
        model.addAttribute("newUser", new UserDto());
        model.addAttribute("userRoles", userService.getAllRoles());
        return "/user/user-create";
    }

    @PostMapping("/create")
    public String createNewUser(UserDto userDto) throws Exception {
        userService.create(userDto);
        return "redirect:/users/list";
    }

    @PostMapping(value = "/actions/{userId}", params = {"action=update"})
    public String navigateToUserUpdate(@PathVariable("userId") Long userId){
        return "redirect:/users/update/" + userId;
    }

    @GetMapping("/update/{userId}")
    public String navigateToUserUpate(@PathVariable("userId") Long userId, Model model) throws Exception {
        model.addAttribute("user", userService.findUserById(userId));
        model.addAttribute("userRoles", userService.getAllRoles());
        return "/user/user-update";
    }

    @PostMapping("/update/{userId}")
    public String updateCompany(@PathVariable("userId") Long userId, UserDto userDto) {
        userService.update(userId, userDto);
        return "redirect:/users/list";
    }

    @PostMapping(value = "/actions/{userId}", params = {"action=delete"})
    public String activateCompany(@PathVariable("userId") Long userId){
        userService.delete(userId);
        return "redirect:/users/list";
    }

}
