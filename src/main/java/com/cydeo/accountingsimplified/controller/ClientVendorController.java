package com.cydeo.accountingsimplified.controller;

import com.cydeo.accountingsimplified.dto.ClientVendorDto;
import com.cydeo.accountingsimplified.dto.CompanyDto;
import com.cydeo.accountingsimplified.enums.ClientVendorType;
import com.cydeo.accountingsimplified.enums.CompanyStatus;
import com.cydeo.accountingsimplified.service.ClientVendorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;

@Controller
@RequestMapping("/clientVendors")
public class ClientVendorController {

    private final ClientVendorService clientVendorService;

    public ClientVendorController(ClientVendorService clientVendorService) {
        this.clientVendorService = clientVendorService;
    }

    @GetMapping("/list")
    public String navigateToClientVendorList(Model model) throws Exception {
        model.addAttribute("clientVendors", clientVendorService.getAllClientVendors());
        return "/clientVendor/clientVendor-list";
    }

    @GetMapping("/create")
    public String navigateToClientVendorCreate(Model model) {
        model.addAttribute("newClientVendor", new ClientVendorDto());
        model.addAttribute("clientVendorTypes", Arrays.asList(ClientVendorType.values()));
        return "/clientVendor/clientVendor-create";
    }

    @PostMapping("/create")
    public String createNewClientVendor(ClientVendorDto clientVendorDto) throws Exception {
        clientVendorService.create(clientVendorDto);
        return "redirect:/clientVendors/list";
    }

    @PostMapping(value = "/actions/{clientVendorId}", params = {"action=update"})
    public String navigateToClientVendorUpdate(@PathVariable("clientVendorId") Long clientVendorId){
        return "redirect:/clientVendors/update/" + clientVendorId;
    }

    @GetMapping("/update/{clientVendorId}")
    public String navigateToClientVendorUpate(@PathVariable("clientVendorId") Long clientVendorId, Model model) {
        model.addAttribute("clientVendor", clientVendorService.findClientVendorById(clientVendorId));
        model.addAttribute("clientVendorTypes", Arrays.asList(ClientVendorType.values()));
        return "/clientVendor/clientVendor-update";
    }

    @PostMapping("/update/{clientVendorId}")
    public String updateClientVendor(@PathVariable("clientVendorId") Long clientVendorId, ClientVendorDto clientVendorDto) {
        clientVendorService.update(clientVendorId, clientVendorDto);
        return "redirect:/clientVendors/list";
    }

    @PostMapping(value = "/actions/{clientVendorId}", params = {"action=delete"})
    public String activateCompany(@PathVariable("clientVendorId") Long clientVendorId){
        clientVendorService.delete(clientVendorId);
        return "redirect:/clientVendors/list";
    }

}
