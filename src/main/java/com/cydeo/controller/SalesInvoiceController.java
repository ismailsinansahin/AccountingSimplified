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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;


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
    public String list(Model model) throws Exception {
        model.addAttribute("invoices", invoiceService.getAllInvoicesOfCompany(InvoiceType.SALES));
        return "invoice/sales-invoice-list";
    }

    @GetMapping("/create")
    public String create(Model model) throws Exception {
        model.addAttribute("newSalesInvoice", invoiceService.getNewInvoice(InvoiceType.SALES));
        return "invoice/sales-invoice-create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("newSalesInvoice") InvoiceDto invoiceDto, BindingResult result) {

        if (result.hasErrors()) {
            return "invoice/sales-invoice-create";
        }

        var invoice = invoiceService.save(invoiceDto, InvoiceType.SALES);
        return "redirect:/salesInvoices/update/" + invoice.getId();
    }

    @GetMapping("/update/{invoiceId}")
    public String update(@PathVariable("invoiceId") Long invoiceId, Model model) {
        model.addAttribute("invoice", invoiceService.findInvoiceById(invoiceId));
        model.addAttribute("newInvoiceProduct", new InvoiceProductDto());
        model.addAttribute("invoiceProducts", invoiceProductService.getInvoiceProductsOfInvoice(invoiceId));
        return "invoice/sales-invoice-update";
    }

    @PostMapping("/update/{invoiceId}")
    public String update(@PathVariable("invoiceId") Long invoiceId, InvoiceDto invoiceDto) {
        invoiceService.update(invoiceId, invoiceDto);
        return "redirect:/salesInvoices/list";
    }

    @PostMapping("/addInvoiceProduct/{invoiceId}")
    public String addInvoiceProduct(@PathVariable("invoiceId") Long invoiceId,
                                    @Valid @ModelAttribute("newInvoiceProduct") InvoiceProductDto invoiceProductDto,
                                    BindingResult result, RedirectAttributes redirAttrs, Model model) {

//        if (!invoiceProductService.checkProductQuantity(invoiceProductDto))  {
////            redirAttrs.addAttribute("error", "Not enough "+invoiceProductDto.getProduct().getName()+" quantity to sell...");
//            redirAttrs.addFlashAttribute("error", "Not enough "+invoiceProductDto.getProduct().getName()+" quantity to sell...");
//            return "redirect:/salesInvoices/update/" + invoiceId;
//        }
        if (result.hasErrors()||(!invoiceProductService.checkProductQuantity(invoiceProductDto))){
            model.addAttribute("invoice", invoiceService.findInvoiceById(invoiceId));
            model.addAttribute("invoiceProducts", invoiceProductService.getInvoiceProductsOfInvoice(invoiceId));
            if (!invoiceProductService.checkProductQuantity(invoiceProductDto)){
                result.rejectValue("quantity", " ", "Not enough " + invoiceProductDto.getProduct().getName() + " quantity to sell...");
            }
            return "invoice/sales-invoice-update";
        }
        invoiceProductService.save(invoiceId, invoiceProductDto);
        return "redirect:/salesInvoices/update/" + invoiceId;
    }

    @GetMapping("/removeInvoiceProduct/{invoiceId}/{invoiceProductId}")
    public String removeInvoiceProduct(@PathVariable("invoiceId") Long invoiceId, @PathVariable("invoiceProductId") Long invoiceProductId) {
        invoiceProductService.delete(invoiceProductId);
        return "redirect:/salesInvoices/update/" + invoiceId;
    }

    @GetMapping(value = "/approve/{invoiceId}")
    public String approve(@PathVariable("invoiceId") Long invoiceId){
        invoiceService.approve(invoiceId);
        return "redirect:/salesInvoices/list";
    }

    @GetMapping(value = "/delete/{invoiceId}")
    public String delete(@PathVariable("invoiceId") Long invoiceId){
        invoiceService.delete(invoiceId);
        return "redirect:/salesInvoices/list";
    }

    @GetMapping(value = "/print/{invoiceId}")
    public String print(@PathVariable("invoiceId") Long id, Model model)  {

        model.addAttribute("invoice", invoiceService.printInvoice(id));
        model.addAttribute("invoiceProducts",invoiceProductService.getInvoiceProductsOfInvoice(id));
        return "invoice/invoice_print";
    }

    @ModelAttribute
    public void commonAttributes(Model model) {
        model.addAttribute("clients", clientVendorService.getAllClientVendors(ClientVendorType.CLIENT));
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("company", companyService.getCompanyDtoByLoggedInUser());
        model.addAttribute("title", "Cydeo Accounting-Sales Invoice");
    }




}
