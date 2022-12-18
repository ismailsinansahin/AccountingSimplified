package com.cydeo.accountingsimplified.service.payment;

;
import com.cydeo.accountingsimplified.dto.CompanyDto;
import com.cydeo.accountingsimplified.entity.Company;
import com.cydeo.accountingsimplified.mapper.MapperUtil;
import com.cydeo.accountingsimplified.service.CompanyService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final MapperUtil mapperUtil;
    private final CompanyService companyService;

    public PaymentServiceImpl(PaymentRepository paymentRepository, MapperUtil mapperUtil, CompanyService companyService) {
        this.paymentRepository = paymentRepository;
        this.mapperUtil = mapperUtil;
        this.companyService = companyService;
    }

    @Override
    public List<PaymentDto> getAllPaymentsByYear(int year) {

        LocalDate instance = LocalDate.now().withYear(year);
        LocalDate dateStart = instance.with(firstDayOfYear());
        LocalDate dateEnd = instance.with(lastDayOfYear());
        var company = companyService.getCompanyByLoggedInUser();

        List<Payment> payments = paymentRepository.findAllByYearBetweenAndCompanyId(dateStart, dateEnd, company.getId());
        return payments.stream().map(obj -> mapperUtil.convert(obj, new PaymentDto())).collect(Collectors.toList());
    }

    @Override
    public void createPaymentsIfNotExist(int year) {

        LocalDate instance = LocalDate.now().withYear(year);
        LocalDate dateStart = instance.with(firstDayOfYear());
        LocalDate dateEnd = instance.with(lastDayOfYear());
        CompanyDto companyDto = companyService.getCompanyByLoggedInUser();

        List<Payment> payments = paymentRepository.findAllByYearBetweenAndCompanyId(dateStart, dateEnd, companyDto.getId());


        if (payments.size() == 0) {
            for (Months month : Months.values()){
                Payment payment = new Payment();
                payment.setMonth(month);
                payment.setYear(LocalDate.now().withYear(year));
                payment.setPaid(false);
                payment.setAmount(250);
                payment.setCompany(mapperUtil.convert(companyDto, new Company()));
                paymentRepository.save(payment);
            }
        }

    }

    @Override
    public PaymentDto getPaymentById(Long id) {
        Payment payment = paymentRepository.getById(id);
        return mapperUtil.convert(payment, new PaymentDto());
    }

    @Override
    public PaymentDto updatePayment(Long id) {

        Payment payment = paymentRepository.getById(id);
        payment.setPaid(true);
        return mapperUtil.convert(paymentRepository.save(payment), new PaymentDto());
    }

    @Override
    public void makePaymentWithSelectedInstitution(String id) {

    }

}