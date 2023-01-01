package com.cydeo.accountingsimplified.service;

import com.cydeo.accountingsimplified.dto.PaymentDto;

import java.util.List;

public interface PaymentService {

    List<PaymentDto> getAllPaymentsByYear(int year);

    void createPaymentsIfNotExist(int year);

    PaymentDto getPaymentById(Long id);

    PaymentDto updatePayment(Long id);
}
