package com.cydeo.accountingsimplified.controller;

import com.cydeo.accountingsimplified.dto.CompanyDto;
import com.cydeo.accountingsimplified.dto.ResponseWrapper;
import com.cydeo.accountingsimplified.service.AddressService;
import com.cydeo.accountingsimplified.service.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper> getCompanies() {
        List<CompanyDto> companies = companyService.getAllCompanies();
        return ResponseEntity.ok(new ResponseWrapper("Companies successfully retrieved",companies, HttpStatus.OK));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper> getCompanyById(@PathVariable("id") Long id) {
        CompanyDto company = companyService.findCompanyById(id);
        return ResponseEntity.ok(new ResponseWrapper("Company successfully retrieved",company, HttpStatus.OK));
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper> createCompany(@RequestBody CompanyDto companyDto) throws Exception {

        if (companyService.isTitleExist(companyDto.getTitle())) {
           throw new Exception("Company title already exist");
        }
        companyService.create(companyDto);
        return ResponseEntity.ok(new ResponseWrapper("Company successfully created",HttpStatus.OK));

    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseWrapper> update(@RequestBody CompanyDto companyDto, @PathVariable("id") Long id) throws Exception {

        boolean isThisCompanyTitle = companyDto.getTitle().equals(companyService.findCompanyById(id).getTitle());
        if (companyService.isTitleExist(companyDto.getTitle()) && !isThisCompanyTitle) {
            throw new Exception("Company title already exist");
        }
        companyService.update(id, companyDto);
        return ResponseEntity.ok(new ResponseWrapper("Company successfully updated",HttpStatus.OK));
    }


    @GetMapping("/activate/{id}")
    public ResponseEntity<ResponseWrapper> activate(@PathVariable("id") Long id) {
        companyService.activate(id);
        return ResponseEntity.ok(new ResponseWrapper("Company successfully activated",HttpStatus.OK));
    }

    @GetMapping("/deactivate/{id}")
    public ResponseEntity<ResponseWrapper> deactivate(@PathVariable("id") Long id) {
        companyService.deactivate(id);
        return ResponseEntity.ok(new ResponseWrapper("Company successfully deactivated",HttpStatus.OK));
    }

}
