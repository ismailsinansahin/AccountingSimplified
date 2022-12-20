package com.cydeo.controller;

import com.cydeo.dto.ClientVendorDto;
import com.cydeo.enums.ClientVendorType;
import com.cydeo.service.AddressService;
import com.cydeo.service.ClientVendorService;
import com.cydeo.service.InvoiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Arrays;

@Controller
@RequestMapping("/clientVendors")
public class ClientVendorController {

    private final ClientVendorService clientVendorService;
    private final InvoiceService invoiceService;
    private final AddressService addressService;

    public ClientVendorController(ClientVendorService clientVendorService, InvoiceService invoiceService, AddressService addressService) {
        this.clientVendorService = clientVendorService;
        this.invoiceService = invoiceService;
        this.addressService = addressService;
    }

    @GetMapping("/list")
    public String list(Model model) throws Exception {
        model.addAttribute("clientVendors", clientVendorService.getAllClientVendors());
        return "clientVendor/clientVendor-list";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("newClientVendor", new ClientVendorDto());
        return "clientVendor/clientVendor-create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("newClientVendor") ClientVendorDto clientVendorDto, BindingResult result, Model model) throws Exception {
        boolean isDuplicatedCompanyName = clientVendorService.companyNameExists(clientVendorDto);
        if (result.hasErrors() || isDuplicatedCompanyName) {
            if (isDuplicatedCompanyName) {
                result.rejectValue("clientVendorName", " ", "A client/vendor with this name already exists. Please try with different name.");
            }
            return "clientVendor/clientVendor-create";
        }
        clientVendorService.create(clientVendorDto);
        return "redirect:/clientVendors/list";
    }

    @GetMapping("/update/{clientVendorId}")
    public String update(@PathVariable("clientVendorId") Long clientVendorId, Model model) {
        model.addAttribute("clientVendor", clientVendorService.findClientVendorById(clientVendorId));
        return "clientVendor/clientVendor-update";
    }

    @PostMapping("/update/{clientVendorId}")
    public String update(@PathVariable("clientVendorId") Long clientVendorId, @Valid @ModelAttribute("clientVendor") ClientVendorDto clientVendorDto, BindingResult result) throws ClassNotFoundException, CloneNotSupportedException {
        clientVendorDto.setId(clientVendorId);
        boolean isDuplicatedCompanyName = clientVendorService.companyNameExists(clientVendorDto);
        if (result.hasErrors() || isDuplicatedCompanyName) {
            if (isDuplicatedCompanyName) {
                result.rejectValue("clientVendorName", " ", "A client/vendor with this name already exists. Please try with different name.");
            }
            return "clientVendor/clientVendor-update";
        }
        clientVendorService.update(clientVendorId, clientVendorDto);
        return "redirect:/clientVendors/list";
    }

    @GetMapping(value = "/delete/{clientVendorId}")
    public String delete(@PathVariable("clientVendorId") Long clientVendorId, RedirectAttributes redirAttrs) {
        if (invoiceService.checkIfInvoiceExist(clientVendorId)){
            redirAttrs.addFlashAttribute("error", "Can not be deleted...You have invoices with this client/vendor");
            return "redirect:/clientVendors/list";
        }
        clientVendorService.delete(clientVendorId);
        return "redirect:/clientVendors/list";
    }

    @ModelAttribute
    public void commonAttributes(Model model){
        model.addAttribute("countries",addressService.getCountryList() );
        model.addAttribute("clientVendorTypes", Arrays.asList(ClientVendorType.values()));
        model.addAttribute("title", "Cydeo Accounting-Client/Vendor");
    }

}
