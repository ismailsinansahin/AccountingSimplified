package com.cydeo.accountingsimplified.controller;

import com.cydeo.accountingsimplified.service.ReportingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reports")
public class ReportingController {

    private final ReportingService reportingService;

    public ReportingController( ReportingService reportingService) {
        this.reportingService = reportingService;
    }

    @GetMapping("/stockData")
    public String getStockData(Model model) throws Exception {
        model.addAttribute("invoiceProducts", reportingService.getStockData());
        return "/report/stock-report";
    }

    @GetMapping("/profitLossData")
    public String getProfitLossData(Model model) {
        model.addAttribute("profitLossDataMap", reportingService.getProfitLossDataMap());
        return "/report/profit-loss-report";
    }

}