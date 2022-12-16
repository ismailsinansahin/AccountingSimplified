package com.cydeo.accountingsimplified.controller;

import com.cydeo.accountingsimplified.dto.CompanyDto;
import com.cydeo.accountingsimplified.service.AddressService;
import com.cydeo.accountingsimplified.service.CompanyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Controller
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;
    private final AddressService addressService;

    public CompanyController(CompanyService companyService, AddressService addressService) {
        this.companyService = companyService;
        this.addressService = addressService;
    }

    @GetMapping("/list")
    public String navigateToCompanyList(Model model) {
        model.addAttribute("companies", companyService.getAllCompanies());
        return "/company/company-list";
    }

    @GetMapping("/create")
    public String navigateToCompanyCreate(Model model) {
        model.addAttribute("newCompany", new CompanyDto());
        return "/company/company-create";
    }

    @PostMapping("/create")
    public String createNewCompany(@Valid @ModelAttribute("newCompany") CompanyDto companyDto, BindingResult bindingResult) {

        if (companyService.isTitleExist(companyDto.getTitle())) {
            bindingResult.rejectValue("title", " ", "This title already exists.");
        }

        if (bindingResult.hasErrors()) {
            return "/company/company-create";
        }

        companyService.create(companyDto);
        return "redirect:/companies/list";
    }


    @GetMapping("/update/{companyId}")
    public String navigateToCompanyUpdate(@PathVariable("companyId") Long companyId, Model model) {
        model.addAttribute("company", companyService.findCompanyById(companyId));
        return "/company/company-update";
    }

    @PostMapping("/update/{companyId}")
    public String updateCompany(@Valid @ModelAttribute("company") CompanyDto companyDto, BindingResult bindingResult, @PathVariable Long companyId) throws CloneNotSupportedException {

        boolean isThisCompanyTitle = companyDto.getTitle().equals(companyService.findCompanyById(companyId).getTitle());
        if (companyService.isTitleExist(companyDto.getTitle()) && !isThisCompanyTitle) {
            bindingResult.rejectValue("title", " ", "This title already exists.");
        }

        if (bindingResult.hasErrors()) {
            companyDto.setId(companyId);
            return "/company/company-update";
        }

        companyService.update(companyId, companyDto);
        return "redirect:/companies/list";
    }

    @GetMapping("/activate/{companyId}")
    public String activateCompany(@PathVariable("companyId") Long companyId) {
        companyService.activate(companyId);
        return "redirect:/companies/list";
    }

    @GetMapping("/deactivate/{companyId}")
    public String deactivateCompany(@PathVariable("companyId") Long companyId) {
        companyService.deactivate(companyId);
        return "redirect:/companies/list";
    }

    @ModelAttribute
    public void commonAttributes(Model model){
        model.addAttribute("countries",addressService.getCountryList() );
        model.addAttribute("title", "Cydeo Accounting-Company");
    }

}
