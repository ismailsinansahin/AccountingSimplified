package com.cydeo.accountingsimplified.controller;

import com.cydeo.accountingsimplified.dto.PaymentDto;
import com.cydeo.accountingsimplified.entity.common.ChargeRequest;
import com.cydeo.accountingsimplified.service.CompanyService;
import com.cydeo.accountingsimplified.service.PaymentService;
import com.cydeo.accountingsimplified.service.implementation.StripeServiceImpl;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/payments")
public class PaymentController {

    @Value("${STRIPE_PUBLIC_KEY}")
    private String stripePublicKey;

    private final StripeServiceImpl stripeServiceImpl;
    private final PaymentService paymentService;
    private final CompanyService companyService;

    public PaymentController(StripeServiceImpl stripeServiceImpl, PaymentService paymentService, CompanyService companyService) {
        this.stripeServiceImpl = stripeServiceImpl;
        this.paymentService = paymentService;
        this.companyService = companyService;
    }


    @GetMapping({"/list", "/list/{year}"})
    public String createPayment(@RequestParam(value = "year", required = false) String selectedYear, Model model) {

        int selectedYear1 = (selectedYear == null || selectedYear.isEmpty()) ? LocalDate.now().getYear() : Integer.parseInt(selectedYear);
        paymentService.createPaymentsIfNotExist(selectedYear1);
        model.addAttribute("payments",paymentService.getAllPaymentsByYear(selectedYear1));
        model.addAttribute("year", selectedYear1);
        return "payment/payment-list";
    }


    @GetMapping("/newpayment/{id}")
    public String checkout(@PathVariable("id") Long id, Model model) {

        PaymentDto dto = paymentService.getPaymentById(id);
        model.addAttribute("payment", dto);
        model.addAttribute("amount", dto.getAmount() * 100); // in cents
        model.addAttribute("stripePublicKey", stripePublicKey);
        model.addAttribute("currency", ChargeRequest.Currency.EUR);
        model.addAttribute("modelId", id);
        return "payment/payment-method";
    }

    // charge controller

    @PostMapping("/charge/{id}")
    public String charge(ChargeRequest chargeRequest, @PathVariable("id") Long id, Model model)
            throws StripeException {
        chargeRequest.setDescription("Example charge");
        chargeRequest.setCurrency(ChargeRequest.Currency.EUR);
        stripeServiceImpl.charge(chargeRequest);
        paymentService.updatePayment(id);
        return "redirect:/payments/list";
    }

    @ExceptionHandler(StripeException.class)
    public String handleError(Model model, StripeException ex) {
        model.addAttribute("error", ex.getMessage());
        return "payment/payment-success";
    }

    // To invoice

    @GetMapping("/toInvoice/{id}")
    public String toInvoice(@PathVariable("id") Long id, Model model) {

        model.addAttribute("payment", paymentService.getPaymentById(id));
        model.addAttribute("company", companyService.getCompanyByLoggedInUser());

        return "payment/payment-success";
    }

    // charge controller



}