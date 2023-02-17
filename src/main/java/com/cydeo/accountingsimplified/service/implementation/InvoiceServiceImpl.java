package com.cydeo.accountingsimplified.service.implementation;

import com.cydeo.accountingsimplified.dto.*;
import com.cydeo.accountingsimplified.entity.*;
import com.cydeo.accountingsimplified.enums.InvoiceStatus;
import com.cydeo.accountingsimplified.enums.InvoiceType;
import com.cydeo.accountingsimplified.mapper.MapperUtil;
import com.cydeo.accountingsimplified.repository.*;
import com.cydeo.accountingsimplified.service.*;
import com.cydeo.accountingsimplified.service.common.CommonService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl extends CommonService implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final InvoiceProductService invoiceProductService;

    public InvoiceServiceImpl(SecurityService securityService, MapperUtil mapperUtil, InvoiceRepository invoiceRepository, InvoiceProductService invoiceProductService) {
        super(securityService, mapperUtil);
        this.invoiceRepository = invoiceRepository;
        this.invoiceProductService = invoiceProductService;
    }

    @Override
    public InvoiceDto findInvoiceById(long id) {
        return mapperUtil.convert(invoiceRepository.findInvoiceById(id), new InvoiceDto());
    }

    @Override
    public List<InvoiceDto> getAllInvoicesOfCompany(InvoiceType invoiceType){
        return invoiceRepository.findInvoicesByCompanyAndInvoiceType(getCompany(), invoiceType)
                .stream()
                .sorted(Comparator.comparing(Invoice::getInvoiceNo))
                .map(each -> mapperUtil.convert(each, new InvoiceDto()))
                .peek(this::calculateInvoiceDetails)
                .collect(Collectors.toList());
    }

    @Override
    public List<InvoiceDto> getAllInvoicesByInvoiceStatus(InvoiceStatus status) {
        List<Invoice> invoices = invoiceRepository.findInvoicesByCompanyAndInvoiceStatus(getCompany(), InvoiceStatus.APPROVED);
        return invoices
                .stream()
                .map(invoice -> mapperUtil.convert(invoice, new InvoiceDto()))
                .collect(Collectors.toList());
    }

    @Override
    public InvoiceDto save(InvoiceDto invoiceDto, InvoiceType invoiceType){
        invoiceDto.setCompany(securityService.getLoggedInUser().getCompany());
        invoiceDto.setInvoiceType(invoiceType);
        invoiceDto.setInvoiceStatus(InvoiceStatus.AWAITING_APPROVAL);
        invoiceDto.setInvoiceNo(generateInvoiceNo(invoiceType));
        invoiceDto.setDate(LocalDate.now());
        Invoice invoice = mapperUtil.convert(invoiceDto, new Invoice());
        return mapperUtil.convert(invoiceRepository.save(invoice), new InvoiceDto());
    }

    @Override
    public InvoiceDto update(Long invoiceId, InvoiceDto invoiceDto) {
        Invoice invoice = invoiceRepository.findInvoiceById(invoiceId);
        invoice.setClientVendor(mapperUtil.convert(invoiceDto.getClientVendor(), new ClientVendor()));
        var savedInvoice = invoiceRepository.save(invoice);
        return mapperUtil.convert(savedInvoice, new InvoiceDto());
    }

    @Override
    public InvoiceDto approve(Long invoiceId) {
        Invoice invoice = invoiceRepository.findInvoiceById(invoiceId);
        invoiceProductService.completeApprovalProcedures(invoiceId, invoice.getInvoiceType());
        invoice.setInvoiceStatus(InvoiceStatus.APPROVED);
        invoice.setDate(LocalDate.now());
        invoiceRepository.save(invoice);
        return mapperUtil.convert(invoice, new InvoiceDto());
    }

    @Override
    public InvoiceDto printInvoice(Long id) {
        InvoiceDto invoiceDto = mapperUtil.convert(invoiceRepository.findInvoiceById(id), new InvoiceDto());
        calculateInvoiceDetails(invoiceDto);
        return invoiceDto;
    }

    @Override
    public void delete(Long invoiceId) {
        Invoice invoice = invoiceRepository.findInvoiceById(invoiceId);
        invoiceProductService.getInvoiceProductsOfInvoice(invoiceId)
                .forEach( invoiceProductDto -> invoiceProductService.delete(invoiceProductDto.getId()));
        invoice.setIsDeleted(true);
        invoiceRepository.save(invoice);
    }

    @Override
    public List<InvoiceDto> getLastThreeInvoices() {
        return invoiceRepository.findInvoicesByCompanyAndInvoiceStatusOrderByDateDesc(getCompany(), InvoiceStatus.APPROVED)
                .stream()
                .limit(3)
                .map(each -> mapperUtil.convert(each, new InvoiceDto()))
                .peek(this::calculateInvoiceDetails)
                .collect(Collectors.toList());
    }

    @Override
    public InvoiceDto getNewInvoice(InvoiceType invoiceType){
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setInvoiceNo(generateInvoiceNo(invoiceType));
        invoiceDto.setDate(LocalDate.now());
        return invoiceDto;
    }

    private String generateInvoiceNo(InvoiceType invoiceType){
        List<Invoice> invoices = invoiceRepository.findInvoicesByCompanyAndInvoiceType(getCompany(), invoiceType);
        if (invoices.size() == 0) {
            return invoiceType.name().charAt(0) + "-001";
        }
        Invoice lastCreatedInvoiceOfTheCompany = invoices.stream()
                .max(Comparator.comparing(Invoice::getInsertDateTime)).get();
        int newOrder = Integer.parseInt(lastCreatedInvoiceOfTheCompany.getInvoiceNo().substring(2)) + 1;
        return invoiceType.name().charAt(0) + "-" + String.format("%03d", newOrder);
    }


    private void calculateInvoiceDetails(InvoiceDto invoiceDto) {
        BigDecimal price = getTotalPriceOfInvoice(invoiceDto);
        BigDecimal tax = getTotalTaxOfInvoice(invoiceDto);
        invoiceDto.setPrice(price);
        invoiceDto.setTax(tax);
        invoiceDto.setTotal(price.add(tax));
    }

    @Override
    public BigDecimal getTotalPriceOfInvoice(InvoiceDto invoiceDto){
        List<InvoiceProductDto> invoiceProductsOfInvoice = invoiceProductService.getInvoiceProductsOfInvoice(invoiceDto.getId());
        return invoiceProductsOfInvoice.stream()
                .map(p -> p.getPrice()
                        .multiply(BigDecimal.valueOf((long) p.getQuantity())))
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

    @Override
    public BigDecimal getTotalTaxOfInvoice(InvoiceDto invoiceDto){
        List<InvoiceProductDto> invoiceProductsOfInvoice = invoiceProductService.getInvoiceProductsOfInvoice(invoiceDto.getId());
        return invoiceProductsOfInvoice.stream()
                .map(p -> p.getPrice()
                        .multiply(BigDecimal.valueOf(p.getQuantity() * p.getTax() /100d))
                        .setScale(2, RoundingMode.HALF_UP))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    @Override
    public BigDecimal getProfitLossOfInvoice(InvoiceDto invoiceDto) {
        List<InvoiceProductDto> invoiceProductsOfInvoice = invoiceProductService.getInvoiceProductsOfInvoice(invoiceDto.getId());
        return invoiceProductsOfInvoice.stream()
                .map(InvoiceProductDto::getProfitLoss)
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

    @Override
    public boolean checkIfInvoiceExist(Long clientVendorId) {
        return invoiceRepository.countAllByCompanyAndClientVendor_Id(getCompany(), clientVendorId) > 0;
    }


}
