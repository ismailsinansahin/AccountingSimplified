package com.cydeo.accountingsimplified.service.implementation;

import com.cydeo.accountingsimplified.dto.*;
import com.cydeo.accountingsimplified.entity.*;
import com.cydeo.accountingsimplified.entity.common.UserPrincipal;
import com.cydeo.accountingsimplified.enums.ClientVendorType;
import com.cydeo.accountingsimplified.enums.InvoiceStatus;
import com.cydeo.accountingsimplified.enums.InvoiceType;
import com.cydeo.accountingsimplified.mapper.MapperUtil;
import com.cydeo.accountingsimplified.repository.*;
import com.cydeo.accountingsimplified.service.CompanyService;
import com.cydeo.accountingsimplified.service.InvoiceService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final InvoiceProductRepository invoiceProductRepository;
    private final ClientVendorRepository clientVendorRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;
    private final MapperUtil mapperUtil;
    private final CompanyService companyService;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository,
                              InvoiceProductRepository invoiceProductRepository,
                              ClientVendorRepository clientVendorRepository, AddressRepository addressRepository,
                              ProductRepository productRepository, MapperUtil mapperUtil, CompanyService companyService) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceProductRepository = invoiceProductRepository;
        this.clientVendorRepository = clientVendorRepository;
        this.addressRepository = addressRepository;
        this.productRepository = productRepository;
        this.mapperUtil = mapperUtil;
        this.companyService = companyService;
    }

    @Override
    public InvoiceDto findInvoiceById(long id) {
        return mapperUtil.convert(invoiceRepository.findInvoiceById(id), new InvoiceDto());
    }

    @Override
    public List<InvoiceDto> getAllInvoicesOfCompany(InvoiceType invoiceType){
        Company company = mapperUtil.convert(companyService.getCompanyByLoggedInUser(), new Company());
        List<InvoiceDto> allInvoicesOfTheCompany = invoiceRepository
                .findInvoicesByCompanyAndInvoiceType(company, invoiceType)
                .stream()
                .map(each -> mapperUtil.convert(each, new InvoiceDto()))
                .collect(Collectors.toList());
        allInvoicesOfTheCompany.forEach(each -> each.setPrice(getPriceOfInvoiceProduct(each.getId())));
        allInvoicesOfTheCompany.forEach(each -> each.setTax(getTaxOfInvoiceProduct(each.getId())));
        allInvoicesOfTheCompany.forEach(each -> each.setTotal(getTotalOfInvoiceProduct(each.getId())));
        return allInvoicesOfTheCompany;
    }

    private int getPriceOfInvoiceProduct(Long id){
        Invoice invoice = invoiceRepository.findInvoiceById(id);
        List<InvoiceProduct> invoiceProductsOfInvoice = invoiceProductRepository.findInvoiceProductsByInvoice(invoice);
        return invoiceProductsOfInvoice.stream().mapToInt(InvoiceProduct::getPrice).sum();
    }

    private int getTaxOfInvoiceProduct(Long id){
        Invoice invoice = invoiceRepository.findInvoiceById(id);
        List<InvoiceProduct> invoiceProductsOfInvoice = invoiceProductRepository.findInvoiceProductsByInvoice(invoice);
        return invoiceProductsOfInvoice.stream().mapToInt(InvoiceProduct::getTax).sum();
    }

    private int getTotalOfInvoiceProduct(Long id){
        Invoice invoice = invoiceRepository.findInvoiceById(id);
        List<InvoiceProduct> invoiceProductsOfInvoice = invoiceProductRepository.findInvoiceProductsByInvoice(invoice);
        return invoiceProductsOfInvoice.stream().mapToInt(InvoiceProduct::getTotal).sum();
    }

    @Override
    public InvoiceDto getNewInvoice(InvoiceType invoiceType){
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setInvoiceNo(generateInvoiceNo(invoiceType));
        invoiceDto.setDate(LocalDate.now());
        Company company = mapperUtil.convert(companyService.getCompanyByLoggedInUser(), new Company());
        invoiceDto.setCompany(mapperUtil.convert(company, new CompanyDto()));
        return invoiceDto;
    }

    private String generateInvoiceNo(InvoiceType invoiceType){
        Company company = mapperUtil.convert(companyService.getCompanyByLoggedInUser(), new Company());
        if (invoiceRepository.findInvoicesByCompanyAndInvoiceType(company, invoiceType).size() ==0) {
            return invoiceType.name().charAt(0) + "-001";
        }
        Invoice lastCreatedInvoiceOfTheCompany = invoiceRepository
                .findInvoicesByCompanyAndInvoiceType(company, invoiceType)
                .stream().max(Comparator.comparing(Invoice::getInsertDateTime)).get();
        int newOrder = Integer.parseInt(lastCreatedInvoiceOfTheCompany.getInvoiceNo().substring(2)) + 1;
        return invoiceType.name().charAt(0) + "-" + String.format("%03d", newOrder);
    }

    @Override
    public InvoiceDto create(InvoiceDto invoiceDto, InvoiceType invoiceType){
        Address address = mapperUtil.convert(invoiceDto.getClientVendor().getAddress(), new Address());
        addressRepository.save(address);
        Company company = mapperUtil.convert(companyService.getCompanyByLoggedInUser(), new Company());
        CompanyDto companyDto = mapperUtil.convert(company, new CompanyDto());
        invoiceDto.setCompany(companyDto);
        invoiceDto.setInvoiceType(invoiceType);
        invoiceDto.setInvoiceStatus(InvoiceStatus.AWAITING_APPROVAL);
        Invoice invoice = mapperUtil.convert(invoiceDto, new Invoice());
        invoiceRepository.save(invoice);
        return mapperUtil.convert(invoice, invoiceDto);
    }

    @Override
    public InvoiceDto update(Long invoiceId, InvoiceDto invoiceDto) {
        Invoice invoice = invoiceRepository.findInvoiceById(invoiceId);
        ClientVendor clientVendor = clientVendorRepository.findClientVendorById(invoiceDto.getClientVendor().getId());
        invoice.setClientVendor(clientVendor);
        invoiceRepository.save(invoice);
        return mapperUtil.convert(invoice, invoiceDto);
    }

    @Override
    public InvoiceDto approve(Long invoiceId) {
        Invoice invoice = invoiceRepository.findInvoiceById(invoiceId);
        List<InvoiceProduct> invoiceProductList = invoiceProductRepository.findInvoiceProductsByInvoice(invoice);
        if(invoice.getInvoiceType()==InvoiceType.SALES){
            for(InvoiceProduct salesInvoiceProduct : invoiceProductList){
                if(productIsEnough(salesInvoiceProduct)){
                    updateQuantityOfProductForSalesInvoice(salesInvoiceProduct);
                    salesInvoiceProduct.setRemainingQuantity(salesInvoiceProduct.getQuantity());
                    invoiceProductRepository.save(salesInvoiceProduct);
                    setProfitLossOfInvoiceProductsForSalesInvoice(salesInvoiceProduct);
                }else{
                    System.out.println("This sale cannot be completed due to insufficient quantity of the product");
                    return null;
                }
            }
        }else{
            for(InvoiceProduct purchaseInvoiceProduct : invoiceProductList) {
                updateQuantityOfProductForPurchaseInvoice(purchaseInvoiceProduct);
                purchaseInvoiceProduct.setRemainingQuantity(purchaseInvoiceProduct.getQuantity());
                invoiceProductRepository.save(purchaseInvoiceProduct);
            }
        }
        invoice.setInvoiceStatus(InvoiceStatus.APPROVED);
        invoice.setDate(LocalDate.now());
        invoiceRepository.save(invoice);
        return mapperUtil.convert(invoice, new InvoiceDto());
    }

    private boolean productIsEnough(InvoiceProduct salesInvoiceProduct) {
        return salesInvoiceProduct.getProduct().getQuantityInStock() >= salesInvoiceProduct.getQuantity();
    }

    private void updateQuantityOfProductForSalesInvoice(InvoiceProduct salesInvoiceProduct) {
        Product product = salesInvoiceProduct.getProduct();
        product.setQuantityInStock(product.getQuantityInStock() - salesInvoiceProduct.getQuantity());
        productRepository.save(product);
    }

    private void updateQuantityOfProductForPurchaseInvoice(InvoiceProduct purchaseInvoiceProduct) {
        Product product = purchaseInvoiceProduct.getProduct();
        product.setQuantityInStock(product.getQuantityInStock() + purchaseInvoiceProduct.getQuantity());
        productRepository.save(product);
    }

    private void setProfitLossOfInvoiceProductsForSalesInvoice(InvoiceProduct salesInvoiceProduct) {
        List<InvoiceProduct> notSoldPurchaseInvoiceProducts = invoiceProductRepository
                .findInvoiceProductsByInvoiceInvoiceTypeAndProductAndRemainingQuantityNotOrderByIdAsc(InvoiceType.PURCHASE, salesInvoiceProduct.getProduct(), 0);
        for (InvoiceProduct notSoldPurchaseInvoiceProduct : notSoldPurchaseInvoiceProducts) {
            if (salesInvoiceProduct.getRemainingQuantity() > notSoldPurchaseInvoiceProduct.getRemainingQuantity()) {
                int costTotalForQty = notSoldPurchaseInvoiceProduct.getTotal() * notSoldPurchaseInvoiceProduct.getRemainingQuantity() / notSoldPurchaseInvoiceProduct.getQuantity();
                int salesPriceForQty = salesInvoiceProduct.getPrice() * notSoldPurchaseInvoiceProduct.getRemainingQuantity();
                int salesTaxForQty = salesPriceForQty * salesInvoiceProduct.getTax() / 100;
                int salesTotalForQty = salesPriceForQty + salesTaxForQty;
                int profitLoss = salesInvoiceProduct.getProfitLoss() + (salesTotalForQty - costTotalForQty);
                salesInvoiceProduct.setRemainingQuantity(salesInvoiceProduct.getRemainingQuantity() - notSoldPurchaseInvoiceProduct.getRemainingQuantity());
                notSoldPurchaseInvoiceProduct.setRemainingQuantity(0);
                salesInvoiceProduct.setProfitLoss(profitLoss);
                invoiceProductRepository.save(notSoldPurchaseInvoiceProduct);
                invoiceProductRepository.save(salesInvoiceProduct);
            } else {
                int costTotalForQty = notSoldPurchaseInvoiceProduct.getTotal() * salesInvoiceProduct.getRemainingQuantity() / notSoldPurchaseInvoiceProduct.getQuantity();
                int salesPriceForQty = salesInvoiceProduct.getPrice() * salesInvoiceProduct.getRemainingQuantity();
                int salesTaxForQty = salesPriceForQty * salesInvoiceProduct.getTax() / 100;
                int salesTotalForQty = salesPriceForQty + salesTaxForQty;
                int profitLoss = salesInvoiceProduct.getProfitLoss() + salesTotalForQty - costTotalForQty;
                notSoldPurchaseInvoiceProduct.setRemainingQuantity(notSoldPurchaseInvoiceProduct.getRemainingQuantity() - salesInvoiceProduct.getRemainingQuantity());
                salesInvoiceProduct.setRemainingQuantity(0);
                salesInvoiceProduct.setProfitLoss(profitLoss);
                invoiceProductRepository.save(notSoldPurchaseInvoiceProduct);
                invoiceProductRepository.save(salesInvoiceProduct);
                break;
            }
        }
    }

    @Override
    public void delete(Long invoiceId) {
        Invoice invoice = invoiceRepository.findInvoiceById(invoiceId);
        invoice.setIsDeleted(true);
        invoiceRepository.save(invoice);
    }





}
