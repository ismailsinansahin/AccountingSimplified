package com.cydeo.accountingsimplified.service.payment;



import java.util.List;

public interface PaymentService {

    List<PaymentDto> getAllPaymentsByYear(int year);

    void createPaymentsIfNotExist(int year);

    PaymentDto getPaymentById(Long id);

    PaymentDto updatePayment(Long id);

    void makePaymentWithSelectedInstitution(String id);



}