package com.cydeo.controller;

import com.cydeo.dto.InvoiceDto;
import com.cydeo.dto.InvoiceProductDto;
import com.cydeo.enums.ClientVendorType;
import com.cydeo.enums.InvoiceType;
import com.cydeo.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Controller
@RequestMapping("/purchaseInvoices")
public class PurchaseInvoiceController {

    private final InvoiceService invoiceService;
    private final InvoiceProductService invoiceProductService;
    private final ClientVendorService clientVendorService;
    private final ProductService productService;
    private final CompanyService companyService;

    public PurchaseInvoiceController(InvoiceService invoiceService, InvoiceProductService invoiceProductService, ClientVendorService clientVendorService, ProductService productService, CompanyService companyService) {
        this.invoiceService = invoiceService;
        this.invoiceProductService = invoiceProductService;
        this.clientVendorService = clientVendorService;
        this.productService = productService;
        this.companyService = companyService;
    }

    @GetMapping("/list")
    public String list(Model model) throws Exception {
        model.addAttribute("invoices", invoiceService.getAllInvoicesOfCompany(InvoiceType.PURCHASE));
        return "invoice/purchase-invoice-list";
    }

    @GetMapping("/create")
    public String create(Model model) throws Exception {
        model.addAttribute("newPurchaseInvoice", invoiceService.getNewInvoice(InvoiceType.PURCHASE));
        return "invoice/purchase-invoice-create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("newPurchaseInvoice") InvoiceDto invoiceDto, BindingResult result) {

        if (result.hasErrors()) {
            return "invoice/purchase-invoice-create";
        }

        var invoice = invoiceService.save(invoiceDto, InvoiceType.PURCHASE);
        return "redirect:/purchaseInvoices/update/" + invoice.getId();
    }

    @GetMapping("/update/{invoiceId}")
    public String update(@PathVariable("invoiceId") Long invoiceId, Model model) {
        model.addAttribute("invoice", invoiceService.findInvoiceById(invoiceId));
        model.addAttribute("newInvoiceProduct", new InvoiceProductDto());
        model.addAttribute("invoiceProducts", invoiceProductService.getInvoiceProductsOfInvoice(invoiceId));
        return "invoice/purchase-invoice-update";
    }

    @PostMapping("/update/{invoiceId}")
    public String update(@PathVariable("invoiceId") Long invoiceId, InvoiceDto invoiceDto) {
        invoiceService.update(invoiceId, invoiceDto);
        return "redirect:/purchaseInvoices/list";
    }

    @PostMapping("/addInvoiceProduct/{invoiceId}")
    public String addInvoiceProduct(@PathVariable("invoiceId") Long invoiceId,
                                    @Valid @ModelAttribute("newInvoiceProduct") InvoiceProductDto invoiceProductDto,
                                    BindingResult result, Model model) {

        if (result.hasErrors()){
            model.addAttribute("invoice", invoiceService.findInvoiceById(invoiceId));
            model.addAttribute("invoiceProducts", invoiceProductService.getInvoiceProductsOfInvoice(invoiceId));
            return "invoice/purchase-invoice-update";
        }

        invoiceProductService.save(invoiceId, invoiceProductDto);
        return "redirect:/purchaseInvoices/update/" + invoiceId;
    }

    @GetMapping("/removeInvoiceProduct/{invoiceId}/{invoiceProductId}")
    public String removeInvoiceProduct(@PathVariable("invoiceId") Long invoiceId,
                                       @PathVariable("invoiceProductId") Long invoiceProductId) {
        invoiceProductService.delete(invoiceProductId);
        return "redirect:/purchaseInvoices/update/" + invoiceId;
    }

    @GetMapping("/approve/{invoiceId}")
    public String approve(@PathVariable("invoiceId") Long invoiceId){
        invoiceService.approve(invoiceId);
        return "redirect:/purchaseInvoices/list";
    }

    @GetMapping("/delete/{invoiceId}")
    public String delete(@PathVariable("invoiceId") Long invoiceId){
        invoiceService.delete(invoiceId);
        return "redirect:/purchaseInvoices/list";
    }

    @GetMapping(value = "/print/{invoiceId}")
    public String print(@PathVariable("invoiceId") Long id, Model model)  {

        model.addAttribute("invoice", invoiceService.printInvoice(id));
        model.addAttribute("invoiceProducts",invoiceProductService.getInvoiceProductsOfInvoice(id));

        return "invoice/invoice_print";
    }

    @ModelAttribute
    public void commonAttributes(Model model) {
        model.addAttribute("vendors", clientVendorService.getAllClientVendors(ClientVendorType.VENDOR));
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("company", companyService.getCompanyByLoggedInUser());   // for printing functionality
        model.addAttribute("title", "Cydeo Accounting-Purchase Invoice");
    }




}
