package com.cydeo.accountingsimplified.controller;

import com.cydeo.accountingsimplified.dto.InvoiceDto;
import com.cydeo.accountingsimplified.dto.InvoiceProductDto;
import com.cydeo.accountingsimplified.enums.ClientVendorType;
import com.cydeo.accountingsimplified.enums.InvoiceType;
import com.cydeo.accountingsimplified.service.ClientVendorService;
import com.cydeo.accountingsimplified.service.InvoiceProductService;
import com.cydeo.accountingsimplified.service.InvoiceService;
import com.cydeo.accountingsimplified.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/salesInvoices")
public class SalesInvoiceController {

    private final InvoiceService invoiceService;
    private final InvoiceProductService invoiceProductService;
    private final ClientVendorService clientVendorService;
    private final ProductService productService;

    public SalesInvoiceController(InvoiceService invoiceService, InvoiceProductService invoiceProductService, ClientVendorService clientVendorService, ProductService productService) {
        this.invoiceService = invoiceService;
        this.invoiceProductService = invoiceProductService;
        this.clientVendorService = clientVendorService;
        this.productService = productService;
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
    public String createNewSalesInvoice(InvoiceDto invoiceDto) {
        invoiceService.create(invoiceDto, InvoiceType.SALES);
        return "redirect:/salesInvoices/list";
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
    public String addInvoiceProductToPurchaseInvoice(@PathVariable("invoiceId") Long invoiceId, InvoiceProductDto invoiceProductDto) {
        invoiceProductService.addInvoiceProduct(invoiceId, invoiceProductDto);
        return "redirect:/salesInvoices/update/" + invoiceId;
    }

    @PostMapping("/removeInvoiceProduct/{invoiceId}/{invoiceProductId}")
    public String removeInvoiceProductFromPurchaseInvoice(@PathVariable("invoiceId") Long invoiceId, @PathVariable("invoiceProductId") Long invoiceProductId) {
        invoiceProductService.removeInvoiceProduct(invoiceProductId);
        return "redirect:/salesInvoices/update/" + invoiceId;
    }

    @PostMapping(value = "/actions/{invoiceId}", params = {"action=approve"})
    public String approvePurchaseInvoice(@PathVariable("invoiceId") Long invoiceId){
        invoiceService.approve(invoiceId);
        return "redirect:/salesInvoices/list";
    }

    @PostMapping(value = "/actions/{invoiceId}", params = {"action=delete"})
    public String deleteSalesInvoice(@PathVariable("invoiceId") Long invoiceId){
        invoiceService.delete(invoiceId);
        return "redirect:/salesInvoices/list";
    }




}