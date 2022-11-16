package com.cydeo.accountingsimplified.controller;

import com.cydeo.accountingsimplified.dto.UserDto;
import com.cydeo.accountingsimplified.service.CompanyService;
import com.cydeo.accountingsimplified.service.RoleService;
import com.cydeo.accountingsimplified.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String listUsers(Model model) throws Exception {
        model.addAttribute("users", userService.getAllUsers());
        return "/user/user-list";
    }

    @GetMapping("/create")
    public String navigateToUserCreate(Model model) {
        model.addAttribute("newUser", new UserDto());
        model.addAttribute("userRoles", roleService.getAllRolesForCurrentUser());
        model.addAttribute("companies", companyService.getAllSuitableCompanies());
        model.addAttribute("currentUserRole", userService.getCurrentUserRoleDescription()); // to decide to show the company box or not
        return "/user/user-create";
    }

    @PostMapping("/create")
    public String createNewUser(@Valid @ModelAttribute("newUser") UserDto userDto, BindingResult result, Model model) {
        boolean emailExist = userService.validateIfEmailUnique(userDto.getUsername());

        if (result.hasErrors() || emailExist){
            if (emailExist) {
                result.rejectValue("username", " ", "A user with this email already exists. Please try with different email.");
            }
            // back-up option for company validation:
//            if (userService.getCurrentUserRoleDescription().equalsIgnoreCase("root user") && userDto.getCompany() == null){
//                result.rejectValue("company", "NotNull.newUser.company", "Company is required field.");
//            }
            model.addAttribute("userRoles", roleService.getAllRolesForCurrentUser());
            model.addAttribute("companies", companyService.getAllCompanies());
            model.addAttribute("currentUserRole", userService.getCurrentUserRoleDescription()); // to decide to show the company box or not
            return "/user/user-create";
        }

        userService.create(userDto);
        return "redirect:/users/list";
    }

    @GetMapping("/update/{userId}")
    public String navigateToUserUpdate(@PathVariable("userId") Long userId, Model model) {
        model.addAttribute("user", userService.findUserById(userId));
        model.addAttribute("companies", companyService.getAllCompanies());
        model.addAttribute("userRoles", roleService.getAllRolesForCurrentUser());
        model.addAttribute("currentUserRole", userService.getCurrentUserRoleDescription()); // to decide to show the company box or not
        System.out.println("last : " + userService.findUserById(userId).getFirstname() + userService.findUserById(userId).getLastAdminOrRootUser());
        return "/user/user-update";
    }

    @PostMapping("/update/{userId}")
    public String updateUser(@PathVariable("userId") Long userId, @Valid @ModelAttribute("user") UserDto userDto, BindingResult result, Model model) {

        if (result.hasErrors()){
            userDto.setId(userId);
            model.addAttribute("companies", companyService.getAllCompanies());
            model.addAttribute("userRoles", roleService.getAllRolesForCurrentUser());
            model.addAttribute("currentUserRole", userService.getCurrentUserRoleDescription()); // to decide to show all companies or only user's company
            return "/user/user-update";
        }
        userService.update(userId, userDto);
        return "redirect:/users/list";
    }

    @GetMapping("/delete/{userId}")
    public String deleteUser(@PathVariable("userId") Long userId){
        userService.delete(userId);
        return "redirect:/users/list";
    }

}
