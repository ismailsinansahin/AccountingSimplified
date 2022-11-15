package com.cydeo.accountingsimplified.controller;

import com.cydeo.accountingsimplified.dto.UserDto;
import com.cydeo.accountingsimplified.service.CompanyService;
import com.cydeo.accountingsimplified.service.RoleService;
import com.cydeo.accountingsimplified.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final CompanyService companyService;

    public UserController(UserService userService, RoleService roleService, CompanyService companyService) {
        this.userService = userService;
        this.roleService = roleService;
        this.companyService = companyService;
    }

    @GetMapping("/list")
    public String navigateToUserList(Model model) throws Exception {
        model.addAttribute("users", userService.getAllUsers());
        return "/user/user-list";
    }

    @GetMapping("/create")
    public String navigateToUserCreate(Model model) {
        model.addAttribute("newUser", new UserDto());
        model.addAttribute("userRoles", roleService.getAllRolesForCurrentUser());
        model.addAttribute("companies", companyService.getAllActiveCompanies());
        model.addAttribute("currentUserRole", userService.getCurrentUserRoleDescription()); // to decide to show the company box or not
        return "/user/user-create";
    }

    @PostMapping("/create")
    public String createNewUser(@Valid @ModelAttribute("newUser") UserDto userDto, BindingResult result, Model model) {
        boolean emailExist = userService.validateIfEmailUnique(userDto.getUsername());
//        boolean emailExist = false;

        if (result.hasErrors() || emailExist){
            if (emailExist) {
                result.rejectValue("username", " ", "A user with this email already exists. Please try with different email.");
            }
            model.addAttribute("userRoles", roleService.getAllRolesForCurrentUser());
            model.addAttribute("companies", companyService.getAllCompanies());
            model.addAttribute("currentUserRole", userService.getCurrentUserRoleDescription()); // to decide to show the company box or not
            return "/user/user-create";
        }

        userService.create(userDto);
        return "redirect:/users/list";
    }

    @PostMapping(value = "/actions/{userId}", params = {"action=update"})
    public String navigateToUserUpdate(@PathVariable("userId") Long userId){
        return "redirect:/users/update/" + userId;
    }

    @GetMapping("/update/{userId}")
    public String navigateToUserUpdate(@PathVariable("userId") Long userId, Model model) throws Exception {
        model.addAttribute("user", userService.findUserById(userId));
        model.addAttribute("userRoles", roleService.getAllRolesForCurrentUser());
        return "/user/user-update";
    }

    @PostMapping("/update/{userId}")
    public String updateCompany(@PathVariable("userId") Long userId, UserDto userDto) {
        userService.update(userId, userDto);
        return "redirect:/users/list";
    }

    @PostMapping(value = "/actions/{userId}", params = {"action=delete"})
    public String deleteUser(@PathVariable("userId") Long userId){
        userService.delete(userId);
        return "redirect:/users/list";
    }

}
