package com.cydeo.accountingsimplified.controller;

import com.cydeo.accountingsimplified.dto.InvoiceDto;
import com.cydeo.accountingsimplified.dto.InvoiceProductDto;
import com.cydeo.accountingsimplified.enums.ClientVendorType;
import com.cydeo.accountingsimplified.enums.InvoiceType;
import com.cydeo.accountingsimplified.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/salesInvoices")
public class SalesInvoiceController {

    private final InvoiceService invoiceService;
    private final InvoiceProductService invoiceProductService;
    private final ClientVendorService clientVendorService;
    private final ProductService productService;
    private final CompanyService companyService;

    public SalesInvoiceController(InvoiceService invoiceService, InvoiceProductService invoiceProductService,
                                  ClientVendorService clientVendorService, ProductService productService,
                                  CompanyService companyService) {
        this.invoiceService = invoiceService;
        this.invoiceProductService = invoiceProductService;
        this.clientVendorService = clientVendorService;
        this.productService = productService;
        this.companyService = companyService;
    }

    @GetMapping("/list")
    public String navigateToSalesInvoiceList(Model model) throws Exception {
        model.addAttribute("invoices", invoiceService.getAllInvoicesOfCompany(InvoiceType.SALES));
        return "/invoice/sales-invoice-list";
    }

    @GetMapping("/create")
    public String navigateToSalesInvoiceCreate(Model model) throws Exception {
        model.addAttribute("newSalesInvoice", invoiceService.getNewInvoice(InvoiceType.SALES));
        return "/invoice/sales-invoice-create";
    }

    @PostMapping("/create")
    public String createNewSalesInvoice(@Valid @ModelAttribute("newSalesInvoice") InvoiceDto invoiceDto, BindingResult result) {

        if (result.hasErrors()) {
            return "/invoice/sales-invoice-create";
        }

        var invoice = invoiceService.save(invoiceDto, InvoiceType.SALES);
        return "redirect:/salesInvoices/update/" + invoice.getId();
    }

    @GetMapping("/update/{invoiceId}")
    public String navigateToSalesInvoiceUpdate(@PathVariable("invoiceId") Long invoiceId, Model model) {
        model.addAttribute("invoice", invoiceService.findInvoiceById(invoiceId));
        model.addAttribute("newInvoiceProduct", new InvoiceProductDto());
        model.addAttribute("invoiceProducts", invoiceProductService.getInvoiceProductsOfInvoice(invoiceId));
        return "/invoice/sales-invoice-update";
    }

    @PostMapping("/update/{invoiceId}")
    public String updateSalesInvoice(@PathVariable("invoiceId") Long invoiceId, InvoiceDto invoiceDto) {
        invoiceService.update(invoiceId, invoiceDto);
        return "redirect:/salesInvoices/list";
    }

    @PostMapping("/addInvoiceProduct/{invoiceId}")
    public String addInvoiceProductToSalesInvoice(@PathVariable("invoiceId") Long invoiceId, @Valid @ModelAttribute("newInvoiceProduct") InvoiceProductDto invoiceProductDto, BindingResult result, RedirectAttributes redirAttrs, Model model) {

        if (result.hasErrors()){
            model.addAttribute("invoice", invoiceService.findInvoiceById(invoiceId));
            model.addAttribute("invoiceProducts", invoiceProductService.getInvoiceProductsOfInvoice(invoiceId));
            return "/invoice/sales-invoice-update";
        }

        if (!invoiceProductService.checkProductQuantity(invoiceProductDto))  {
            redirAttrs.addFlashAttribute("error", "Not enough "+invoiceProductDto.getProduct().getName()+" quantity to sell...");
            return "redirect:/salesInvoices/update/" + invoiceId;
        }

        invoiceProductService.save(invoiceId, invoiceProductDto);
        return "redirect:/salesInvoices/update/" + invoiceId;
    }

    @GetMapping("/removeInvoiceProduct/{invoiceId}/{invoiceProductId}")
    public String removeInvoiceProductFromPurchaseInvoice(@PathVariable("invoiceId") Long invoiceId, @PathVariable("invoiceProductId") Long invoiceProductId) {
        invoiceProductService.delete(invoiceProductId);
        return "redirect:/salesInvoices/update/" + invoiceId;
    }

    @GetMapping(value = "/approve/{invoiceId}")
    public String approvePurchaseInvoice(@PathVariable("invoiceId") Long invoiceId){
        invoiceService.approve(invoiceId);
        return "redirect:/salesInvoices/list";
    }

    @GetMapping(value = "/delete/{invoiceId}")
    public String deleteSalesInvoice(@PathVariable("invoiceId") Long invoiceId){
        invoiceService.delete(invoiceId);
        return "redirect:/salesInvoices/list";
    }

    //Print functionality

    @GetMapping(value = "/print/{invoiceId}")
    public String print(@PathVariable("invoiceId") Long id, Model model)  {

        model.addAttribute("invoice", invoiceService.findInvoiceById(id));
        model.addAttribute("invoiceProducts",invoiceProductService.getInvoiceProductsOfInvoice(id));

        return "invoice/invoice_print";
    }

    @ModelAttribute
    public void commonAttributes(Model model) {
        model.addAttribute("clients", clientVendorService.getAllClientVendorsOfCompany(ClientVendorType.CLIENT));
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("company", companyService.getCompanyByLoggedInUser());
    }




}
