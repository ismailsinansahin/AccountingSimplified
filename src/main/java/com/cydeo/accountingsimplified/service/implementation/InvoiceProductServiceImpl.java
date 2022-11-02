package com.cydeo.accountingsimplified.service.implementation;

import com.cydeo.accountingsimplified.dto.InvoiceDto;
import com.cydeo.accountingsimplified.dto.InvoiceProductDto;
import com.cydeo.accountingsimplified.dto.ProductDto;
import com.cydeo.accountingsimplified.entity.Invoice;
import com.cydeo.accountingsimplified.entity.InvoiceProduct;
import com.cydeo.accountingsimplified.entity.Product;
import com.cydeo.accountingsimplified.enums.InvoiceType;
import com.cydeo.accountingsimplified.mapper.MapperUtil;
import com.cydeo.accountingsimplified.repository.InvoiceProductRepository;
import com.cydeo.accountingsimplified.repository.InvoiceRepository;
import com.cydeo.accountingsimplified.service.InvoiceProductService;
import com.cydeo.accountingsimplified.service.InvoiceService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceProductServiceImpl implements InvoiceProductService {

    private final InvoiceProductRepository invoiceProductRepository;
    private final InvoiceService invoiceService;
    private final MapperUtil mapperUtil;

    public InvoiceProductServiceImpl(InvoiceProductRepository invoiceProductRepository,
                                     @Lazy InvoiceService invoiceService, MapperUtil mapperUtil) {
        this.invoiceProductRepository = invoiceProductRepository;
        this.invoiceService = invoiceService;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public InvoiceProductDto findInvoiceProductById(long id) {
        return mapperUtil.convert(invoiceProductRepository.findInvoiceProductById(id), new InvoiceProductDto());
    }

    @Override
    public List<InvoiceProductDto> getInvoiceProductsOfInvoice(Long invoiceId) {
        InvoiceDto invoiceDto = invoiceService.findInvoiceById(invoiceId);
        return invoiceProductRepository
                .findInvoiceProductsByInvoice(mapperUtil.convert(invoiceDto, new Invoice()))
                .stream()
                .map(each -> mapperUtil.convert(each, new InvoiceProductDto()))
                .collect(Collectors.toList());
    }

    @Override
    public void addInvoiceProduct(Long invoiceId, InvoiceProductDto invoiceProductDto) {
        InvoiceDto invoiceDto = invoiceService.findInvoiceById(invoiceId);
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

    @Override
    public int getPriceOfInvoiceProduct(Long id) {
        InvoiceDto invoiceDto = invoiceService.findInvoiceById(id);
        List<InvoiceProduct> invoiceProductsOfInvoice = invoiceProductRepository.findInvoiceProductsByInvoice(mapperUtil.convert(invoiceDto, new Invoice()));
        return invoiceProductsOfInvoice.stream().mapToInt(InvoiceProduct::getPrice).sum();
    }

    @Override
    public int getTaxOfInvoiceProduct(Long id) {
        InvoiceDto invoiceDto = invoiceService.findInvoiceById(id);
        List<InvoiceProduct> invoiceProductsOfInvoice = invoiceProductRepository.findInvoiceProductsByInvoice(mapperUtil.convert(invoiceDto, new Invoice()));
        return invoiceProductsOfInvoice.stream().mapToInt(InvoiceProduct::getTax).sum();
    }

    @Override
    public int getTotalOfInvoiceProduct(Long id) {
        InvoiceDto invoiceDto = invoiceService.findInvoiceById(id);
        List<InvoiceProduct> invoiceProductsOfInvoice = invoiceProductRepository.findInvoiceProductsByInvoice(mapperUtil.convert(invoiceDto, new Invoice()));
        return invoiceProductsOfInvoice.stream().mapToInt(InvoiceProduct::getTotal).sum();
    }

    @Override
    public int getProfitLossOfInvoiceProduct(Long id) {
        InvoiceDto invoiceDto = invoiceService.findInvoiceById(id);
        List<InvoiceProduct> invoiceProductsOfInvoice = invoiceProductRepository.findInvoiceProductsByInvoice(mapperUtil.convert(invoiceDto, new Invoice()));
        return invoiceProductsOfInvoice.stream().mapToInt(InvoiceProduct::getProfitLoss).sum();
    }

    @Override
    public void update(InvoiceProductDto invoiceProductDto) {
        invoiceProductRepository.save(mapperUtil.convert(invoiceProductDto, new InvoiceProduct()));
    }

    @Override
    public List<InvoiceProductDto> findInvoiceProductsByInvoiceTypeAndProductRemainingQuantity(InvoiceType type, ProductDto product, Integer remainingQuantity ) {
       return invoiceProductRepository
                .findInvoiceProductsByInvoiceInvoiceTypeAndProductAndRemainingQuantityNotOrderByIdAsc(type,mapperUtil.convert(product, new Product()),remainingQuantity)
                .stream()
                .map(invoiceProduct -> mapperUtil.convert(invoiceProduct, new InvoiceProductDto()))
                .collect(Collectors.toList());
    }

}
