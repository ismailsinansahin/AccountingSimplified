package com.cydeo.accountingsimplified.service.implementation;

import com.cydeo.accountingsimplified.dto.*;
import com.cydeo.accountingsimplified.entity.*;
import com.cydeo.accountingsimplified.enums.InvoiceStatus;
import com.cydeo.accountingsimplified.enums.InvoiceType;
import com.cydeo.accountingsimplified.mapper.MapperUtil;
import com.cydeo.accountingsimplified.repository.*;
import com.cydeo.accountingsimplified.service.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final InvoiceProductService invoiceProductService;
    private final MapperUtil mapperUtil;
    private final SecurityService securityService;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, InvoiceProductService invoiceProductService,
                              MapperUtil mapperUtil, SecurityService securityService) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceProductService = invoiceProductService;
        this.mapperUtil = mapperUtil;
        this.securityService = securityService;
    }

    @Override
    public InvoiceDto findInvoiceById(long id) {
        return mapperUtil.convert(invoiceRepository.findInvoiceById(id), new InvoiceDto());
    }

    @Override
    public List<InvoiceDto> getAllInvoicesOfCompany(InvoiceType invoiceType){
        Company company = mapperUtil.convert(securityService.getLoggedInUser().getCompany(), new Company());
        List<InvoiceDto> allInvoicesOfTheCompany = invoiceRepository
                .findInvoicesByCompanyAndInvoiceType(company, invoiceType)
                .stream()
                .map(each -> mapperUtil.convert(each, new InvoiceDto()))
                .collect(Collectors.toList());
        allInvoicesOfTheCompany.forEach(each -> each.setPrice(getTotalPriceOfInvoice(each.getId())));
        allInvoicesOfTheCompany.forEach(each -> each.setTax(getTotalTaxOfInvoice(each.getId())));
        allInvoicesOfTheCompany.forEach(each -> each.setTotal(calculateTotalAmountOfInvoice(each.getId())));
        return allInvoicesOfTheCompany;
    }

    @Override
    public InvoiceDto create(InvoiceDto invoiceDto, InvoiceType invoiceType){
        invoiceDto.setCompany(securityService.getLoggedInUser().getCompany());
        invoiceDto.setInvoiceType(invoiceType);
        invoiceDto.setInvoiceStatus(InvoiceStatus.AWAITING_APPROVAL);
        Invoice invoice = mapperUtil.convert(invoiceDto, new Invoice());
        return mapperUtil.convert(invoiceRepository.save(invoice), new InvoiceDto());
    }

    @Override
    public InvoiceDto update(Long invoiceId, InvoiceDto invoiceDto) {
        Invoice invoice = invoiceRepository.findInvoiceById(invoiceId);
        invoice.setClientVendor(mapperUtil.convert(invoiceDto.getClientVendor(), new ClientVendor()));
        invoiceRepository.save(invoice);
        return mapperUtil.convert(invoice, invoiceDto);
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
    public void delete(Long invoiceId) {
        Invoice invoice = invoiceRepository.findInvoiceById(invoiceId);
        invoice.setIsDeleted(true);
        invoiceRepository.save(invoice);
    }

    @Override
    public List<InvoiceDto> getLastThreeInvoices() {
        Company company = mapperUtil.convert(securityService.getLoggedInUser().getCompany(), new Company());
        List<InvoiceDto> last3Invoices = invoiceRepository.findInvoicesByCompanyAndInvoiceStatusOrderByDateDesc(company, InvoiceStatus.APPROVED)
                .stream()
                .limit(3)
                .map(each -> mapperUtil.convert(each, new InvoiceDto()))
                .collect(Collectors.toList());
        last3Invoices.forEach(each -> each.setPrice(invoiceProductService.getPriceOfInvoiceProduct(each.getId())));
        last3Invoices.forEach(each -> each.setTax(invoiceProductService.getTaxOfInvoiceProduct(each.getId())));
        last3Invoices.forEach(each -> each.setTotal(invoiceProductService.getTotalOfInvoiceProduct(each.getId())));
        return last3Invoices;
    }
    @Override
    public InvoiceDto getNewInvoice(InvoiceType invoiceType){
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setInvoiceNo(generateInvoiceNo(invoiceType));
        invoiceDto.setDate(LocalDate.now());
        invoiceDto.setCompany(securityService.getLoggedInUser().getCompany());
        return invoiceDto;
    }

    private String generateInvoiceNo(InvoiceType invoiceType){
        Company company = mapperUtil.convert(securityService.getLoggedInUser().getCompany(), new Company());
        if (invoiceRepository.findInvoicesByCompanyAndInvoiceType(company, invoiceType).size() ==0) {
            return invoiceType.name().charAt(0) + "-001";
        }
        Invoice lastCreatedInvoiceOfTheCompany = invoiceRepository
                .findInvoicesByCompanyAndInvoiceType(company, invoiceType)
                .stream().max(Comparator.comparing(Invoice::getInsertDateTime)).get();
        int newOrder = Integer.parseInt(lastCreatedInvoiceOfTheCompany.getInvoiceNo().substring(2)) + 1;
        return invoiceType.name().charAt(0) + "-" + String.format("%03d", newOrder);
    }

    private int getTotalPriceOfInvoice(Long id){
        Invoice invoice = invoiceRepository.findInvoiceById(id);
        List<InvoiceProductDto> invoiceProductsOfInvoice = invoiceProductService.getInvoiceProductsOfInvoice(invoice.getId());
        return invoiceProductsOfInvoice.stream().mapToInt(InvoiceProductDto::getPrice).sum();
    }

    private int getTotalTaxOfInvoice(Long id){
        Invoice invoice = invoiceRepository.findInvoiceById(id);
        List<InvoiceProductDto> invoiceProductsOfInvoice = invoiceProductService.getInvoiceProductsOfInvoice(invoice.getId());
        return invoiceProductsOfInvoice.stream().mapToInt(InvoiceProductDto::getTax).sum();
    }

    private int calculateTotalAmountOfInvoice(Long id){
        Invoice invoice = invoiceRepository.findInvoiceById(id);
        List<InvoiceProductDto> invoiceProductsOfInvoice = invoiceProductService.getInvoiceProductsOfInvoice(invoice.getId());
        return invoiceProductsOfInvoice.stream().mapToInt(InvoiceProductDto::getTotal).sum();
    }


}
