package com.cydeo.accountingsimplified.controller;

import com.cydeo.accountingsimplified.dto.CompanyDto;
import com.cydeo.accountingsimplified.enums.CompanyStatus;
import com.cydeo.accountingsimplified.service.CompanyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;

@Controller
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/list")
    public String navigateToCompanyList(Model model) {
        model.addAttribute("companies", companyService.getAllCompanies());
        return "/company/company-list";
    }

    @GetMapping("/create")
    public String navigateToCompanyCreate(Model model) {
        model.addAttribute("newCompany", new CompanyDto());
        model.addAttribute("companyStatuses", Arrays.asList(CompanyStatus.values()));
        return "/company/company-create";
    }

    @PostMapping("/create")
    public String createNewCompany(CompanyDto companyDto) {
        companyService.create(companyDto);
        return "redirect:/companies/list";
    }

    @PostMapping(value = "/actions/{companyId}", params = {"action=update"})
    public String navigateToCompanyUpdate(@PathVariable("companyId") Long companyId){
        return "redirect:/companies/update/" + companyId;
    }

    @GetMapping("/update/{companyId}")
    public String navigateToCompanyUpate(@PathVariable("companyId") Long companyId, Model model) {
        model.addAttribute("company", companyService.findCompanyById(companyId));
        model.addAttribute("companyStatuses", Arrays.asList(CompanyStatus.values()));
        return "/company/company-update";
    }

    @PostMapping("/update/{companyId}")
    public String updateCompany(@PathVariable("companyId") Long companyId, CompanyDto companyDto) {
        companyService.update(companyId, companyDto);
        return "redirect:/companies/list";
    }

    @PostMapping(value = "/actions/{companyId}", params = {"action=activate"})
    public String activateCompany(@PathVariable("companyId") Long companyId){
        companyService.activate(companyId);
        return "redirect:/companies/list";
    }

    @PostMapping(value = "/actions/{companyId}", params = {"action=deactivate"})
    public String deactivateCompany(@PathVariable("companyId") Long companyId){
        companyService.deactivate(companyId);
        return "redirect:/companies/list";
    }




}
