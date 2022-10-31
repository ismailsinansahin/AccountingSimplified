package com.cydeo.accountingsimplified.service.implementation;

import com.cydeo.accountingsimplified.dto.InvoiceDto;
import com.cydeo.accountingsimplified.dto.InvoiceProductDto;
import com.cydeo.accountingsimplified.entity.Invoice;
import com.cydeo.accountingsimplified.entity.InvoiceProduct;
import com.cydeo.accountingsimplified.enums.InvoiceType;
import com.cydeo.accountingsimplified.mapper.MapperUtil;
import com.cydeo.accountingsimplified.repository.InvoiceProductRepository;
import com.cydeo.accountingsimplified.repository.InvoiceRepository;
import com.cydeo.accountingsimplified.service.InvoiceProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceProductServiceImpl implements InvoiceProductService {

    private final InvoiceProductRepository invoiceProductRepository;
    private final InvoiceRepository invoiceRepository;
    private final MapperUtil mapperUtil;

    public InvoiceProductServiceImpl(InvoiceProductRepository invoiceProductRepository, InvoiceRepository invoiceRepository, MapperUtil mapperUtil) {
        this.invoiceProductRepository = invoiceProductRepository;
        this.invoiceRepository = invoiceRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public InvoiceProductDto findInvoiceProductById(long id) {
        return mapperUtil.convert(invoiceProductRepository.findInvoiceProductById(id), new InvoiceProductDto());
    }

    @Override
    public List<InvoiceProductDto> getInvoiceProductsOfInvoice(Long invoiceId) {
        Invoice invoice = invoiceRepository.findInvoiceById(invoiceId);
        return invoiceProductRepository
                .findInvoiceProductsByInvoice(invoice)
                .stream()
                .map(each -> mapperUtil.convert(each, new InvoiceProductDto()))
                .collect(Collectors.toList());
    }

    @Override
    public void addInvoiceProduct(Long invoiceId, InvoiceProductDto invoiceProductDto) {
        InvoiceDto invoiceDto = mapperUtil.convert(invoiceRepository.findInvoiceById(invoiceId), new InvoiceDto());
        invoiceProductDto.setInvoice(invoiceDto);
        invoiceProductDto.setTotal(getAmountOfInvoiceProduct(invoiceProductDto));
        if(invoiceDto.getInvoiceType() == InvoiceType.PURCHASE){
            invoiceProductDto.setProfitLoss(0);
        }else{
            invoiceProductDto.setProfitLoss(0);
            invoiceProductDto.setRemainingQuantity(0);
        }
        InvoiceProduct invoiceProduct = mapperUtil.convert(invoiceProductDto, new InvoiceProduct());
        invoiceProductRepository.save(invoiceProduct);
    }

    private Integer getAmountOfInvoiceProduct(InvoiceProductDto invoiceProductDto) {
        int quantity = invoiceProductDto.getQuantity();
        int price = invoiceProductDto.getPrice();
        int tax = invoiceProductDto.getTax();
        return (quantity * price) + (quantity * price * tax / 100);
    }

    @Override
    public void removeInvoiceProduct(Long invoiceProductId) {
        InvoiceProduct invoiceProduct = invoiceProductRepository.findInvoiceProductById(invoiceProductId);
        invoiceProduct.setIsDeleted(true);
        invoiceProductRepository.save(invoiceProduct);
    }

}
