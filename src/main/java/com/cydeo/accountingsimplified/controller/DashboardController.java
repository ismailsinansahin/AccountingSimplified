package com.cydeo.accountingsimplified.controller;

import com.cydeo.accountingsimplified.dto.InvoiceDto;
import com.cydeo.accountingsimplified.entity.Invoice;
import com.cydeo.accountingsimplified.enums.InvoiceStatus;
import com.cydeo.accountingsimplified.enums.InvoiceType;
import com.cydeo.accountingsimplified.service.CompanyService;
import com.cydeo.accountingsimplified.service.DashboardService;
import com.cydeo.accountingsimplified.service.InvoiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class DashboardController {
    private final DashboardService dashboardService;
    private final InvoiceService invoiceService;
    private final CompanyService companyService;

    public DashboardController(DashboardService dashboardService, InvoiceService invoiceService, CompanyService companyService) {
        this.dashboardService = dashboardService;
        this.invoiceService = invoiceService;
        this.companyService = companyService;
    }

    @GetMapping("/dashboard")
    public String navigateToDashboard(Model model) throws Exception {
        model.addAttribute("companyTitle", companyService.getCompanyByLoggedInUser().getTitle());
        model.addAttribute("summaryNumbers", dashboardService.getSummaryNumbers());
        model.addAttribute("invoices", invoiceService.getLastThreeInvoices());
        model.addAttribute("exchangeRates", dashboardService.getExchangeRates());
        model.addAttribute("title", "Cydeo Accounting-Dashboard");
        return "dashboard";
    }

}
