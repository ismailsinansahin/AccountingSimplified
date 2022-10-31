package com.cydeo.accountingsimplified.controller;

import com.cydeo.accountingsimplified.service.CompanyService;
import com.cydeo.accountingsimplified.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;

    private final CompanyService companyService;

    public DashboardController(DashboardService dashboardService, CompanyService companyService) {
        this.dashboardService = dashboardService;
        this.companyService = companyService;
    }

    @GetMapping("/dashboard")
    public String navigateToDashboard(Model model) throws Exception {
        model.addAttribute("companyTitle", companyService.getCompanyByLoggedInUser().getTitle());
        model.addAttribute("summaryNumbers", dashboardService.getSummaryNumbers());
        model.addAttribute("invoices", dashboardService.getLastThreeInvoices());
        model.addAttribute("exchangeRates", dashboardService.getExchangeRates());
        return "dashboard";
    }

}
