package com.cydeo.accountingsimplified.controller;

import com.cydeo.accountingsimplified.app_util.ErrorGenerator;
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
import java.util.Objects;


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
        model.addAttribute("clients", clientVendorService.getAllClientVendorsOfCompany(ClientVendorType.CLIENT));
        return "/invoice/sales-invoice-create";
    }

    @PostMapping("/create")
    public String createNewSalesInvoice(@Valid @ModelAttribute("newSalesInvoice") InvoiceDto invoiceDto, RedirectAttributes redirAttrs) {

        if (invoiceDto.getClientVendor() == null) {
            redirAttrs.addFlashAttribute("error", "Please choose a Client");
            return "redirect:/salesInvoices/create";
        }

        InvoiceDto invoice = invoiceService.create(invoiceDto, InvoiceType.SALES);
        return "redirect:/salesInvoices/update/" + invoice.getId();
    }

    @PostMapping(value = "/actions/{invoiceId}", params = {"action=update"})
    public String navigateToSalesInvoiceUpdate(@PathVariable("invoiceId") Long invoiceId){
        return "redirect:/salesInvoices/update/" + invoiceId;
    }

    @GetMapping("/update/{invoiceId}")
    public String navigateToSalesInvoiceUpdate(@PathVariable("invoiceId") Long invoiceId, Model model) throws Exception {
        model.addAttribute("invoice", invoiceService.findInvoiceById(invoiceId));
        model.addAttribute("clients", clientVendorService.getAllClientVendorsOfCompany(ClientVendorType.CLIENT));
        model.addAttribute("products", productService.getProductsOfCompany());
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
    public String addInvoiceProductToSalesInvoice(@PathVariable("invoiceId") Long invoiceId, @Valid @ModelAttribute("newInvoiceProduct") InvoiceProductDto invoiceProductDto, BindingResult result, RedirectAttributes redirAttrs) {

        if (invoiceProductDto.getProduct() == null){
            redirAttrs.addFlashAttribute("product", "Please choose a product");
        }

        if (result.hasErrors()){
            result.getAllErrors().stream()
                    .map(obj -> (FieldError)obj)
                    .forEach(err -> redirAttrs.addAttribute(err.getField(), err.getDefaultMessage()));
            return "redirect:/salesInvoices/update/" + invoiceId;
        }

        if (!invoiceProductService.checkProductQuantity(invoiceProductDto))  {
            redirAttrs.addFlashAttribute("error", "Not enough "+invoiceProductDto.getProduct().getName()+" quantity to sell...");
            return "redirect:/salesInvoices/update/" + invoiceId;
        }

        invoiceProductService.save(invoiceId, invoiceProductDto);
        return "redirect:/salesInvoices/update/" + invoiceId;
    }

    @PostMapping("/removeInvoiceProduct/{invoiceId}/{invoiceProductId}")
    public String removeInvoiceProductFromSalesInvoice(@PathVariable("invoiceId") Long invoiceId, @PathVariable("invoiceProductId") Long invoiceProductId) {
        invoiceProductService.removeInvoiceProduct(invoiceProductId);
        return "redirect:/salesInvoices/update/" + invoiceId;
    }

    @PostMapping(value = "/actions/{invoiceId}", params = {"action=approve"})
    public String approveSalesInvoice(@PathVariable("invoiceId") Long invoiceId){
        invoiceService.approve(invoiceId);
        return "redirect:/salesInvoices/list";
    }

    @PostMapping(value = "/actions/{invoiceId}", params = {"action=delete"})
    public String deleteSalesInvoice(@PathVariable("invoiceId") Long invoiceId){
        invoiceService.delete(invoiceId);
        return "redirect:/salesInvoices/list";
    }

    //Print functionality

    @PostMapping(value = "/actions/{invoiceId}", params = {"action=print"})
    public String print(@PathVariable("invoiceId") Long id, Model model)  {

        model.addAttribute("company", companyService.getCompanyByLoggedInUser());
        model.addAttribute("invoice", invoiceService.findInvoiceById(id));
        model.addAttribute("invoiceProducts",invoiceProductService.getInvoiceProductsOfInvoice(id));

        return "invoice/invoice_print";
    }




}
