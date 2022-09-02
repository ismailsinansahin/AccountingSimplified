package com.cydeo.accountingsimplified.controller;

import com.cydeo.accountingsimplified.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String navigateToDashboard() {
        return "dashboard";
    }

}
