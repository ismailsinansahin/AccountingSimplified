package com.cydeo.accountingsimplified.service.payment;


import com.cydeo.accountingsimplified.service.CompanyService;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Coupon;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/payments")
public class PaymentController {

    @Value("${stripe.keys.secret}")
    private String stripePublicKey;

    private final StripeService stripeServiceImpl;
    private final PaymentService paymentService;
    private final CompanyService companyService;
    private StripeService stripeService;

    public PaymentController(StripeService stripeServiceImpl, PaymentService paymentService, CompanyService companyService) {
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
//        model.addAttribute("currency", ChargeRequest.Currency.EUR);
        model.addAttribute("modelId", id);
        return "payment/payment-method";
    }
    // charge controller

//    @PostMapping("/charge/{id}")
//    public String charge(ChargeRequest chargeRequest, @PathVariable("id") Long id, Model model)
//            throws StripeException {
//        chargeRequest.setDescription("Example charge");
//        chargeRequest.setCurrency(ChargeRequest.Currency.EUR);
//        Charge charge = stripeServiceImpl.charge(chargeRequest);
//        PaymentDTO dto = paymentService.updatePayment(id);
//        model.addAttribute("id", charge.getId());
//        model.addAttribute("status", charge.getStatus());
//        model.addAttribute("chargeId", charge.getId());
//        model.addAttribute("balance_transaction", charge.getBalanceTransaction());
//        model.addAttribute("company", companyService.getCompanyByLoggedInUser());
//        model.addAttribute("payment", dto);
//        return "payment/payment-success";
//    }
    @ExceptionHandler(StripeException.class)
    public String handleError(Model model, StripeException ex) {
        model.addAttribute("error", ex.getMessage());
        return "payment/payment-success";
    }

    @GetMapping("/toInvoice/{id}")
    public String toInvoice(@PathVariable("id") Long id, Model model)  {

        model.addAttribute("payment", paymentService.getPaymentById(id));
        model.addAttribute("company", companyService.getCompanyByLoggedInUser());

        return "payment/payment-success";
    }
    @GetMapping("/")
    public String homepage() {
        return "homepage";
    }

//    @GetMapping("/subscription")
//    public String subscriptionPage(Model model) {
//        model.addAttribute("stripePublicKey", API_PUBLIC_KEY);
//        return "subscription";
//    }

//    @GetMapping("/charge")
//    public String chargePage(Model model) {
//        model.addAttribute("stripePublicKey", API_PUBLIC_KEY);
//        return "charge";
//    }

    /*========== REST APIs for Handling Payments ===================*/

//    @PostMapping("/create-subscription")
//    public @ResponseBody
//    Response createSubscription(String email, String token, String plan, String coupon) {
//        //validate data
//        if (token == null || plan.isEmpty()) {
//            return new Response(false, "Stripe payment token is missing. Please, try again later.");
//        }
//
//        //create customer first
//        String customerId = stripeService.createCustomer(email, token);
//
//        if (customerId == null) {
//            return new Response(false, "An error occurred while trying to create a customer.");
//        }
//
//        //create subscription
//        String subscriptionId = stripeService.createSubscription(customerId, plan, coupon);
//        if (subscriptionId == null) {
//            return new Response(false, "An error occurred while trying to create a subscription.");
//        }
//
//        // Ideally you should store customerId and subscriptionId along with customer object here.
//        // These values are required to update or cancel the subscription at a later stage.
//
//        return new Response(true, "Success! Your subscription id is " + subscriptionId);
//    }
//
//    @PostMapping("/cancel-subscription")
//    public @ResponseBody
//    Response cancelSubscription(String subscriptionId) {
//        boolean status = stripeService.cancelSubscription(subscriptionId);
//        if (!status) {
//            return new Response(false, "Failed to cancel the subscription. Please, try later.");
//        }
//        return new Response(true, "Subscription cancelled successfully.");
//    }
//
//    @PostMapping("/coupon-validator")
//    public @ResponseBody
//    Response couponValidator(String code) {
//        Coupon coupon = stripeService.retrieveCoupon(code);
//        if (coupon != null && coupon.getValid()) {
//            String details = (coupon.getPercentOff() == null ? "$" + (coupon.getAmountOff() / 100) : coupon.getPercentOff() + "%") +
//                    " OFF " + coupon.getDuration();
//            return new Response(true, details);
//        } else {
//            return new Response(false, "This coupon code is not available. This may be because it has expired or has " +
//                    "already been applied to your account.");
//        }
//    }
//
//    @PostMapping("/create-charge")
//    public @ResponseBody
//    Response createCharge(String email, String token) {
//        //validate data
//        if (token == null) {
//            return new Response(false, "Stripe payment token is missing. Please, try again later.");
//        }
//
//        //create charge
//        String chargeId = stripeService.createCharge(email, token, 999); //$9.99 USD
//        if (chargeId == null) {
//            return new Response(false, "An error occurred while trying to create a charge.");
//        }
//
//        // You may want to store the charge id along with order information
//
//        return new Response(true, "Success! Your charge id is " + chargeId);
//    }
}
