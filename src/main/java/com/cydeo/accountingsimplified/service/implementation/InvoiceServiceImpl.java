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
    private final ProductService productService;
    private final MapperUtil mapperUtil;
    private final SecurityService securityService;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, InvoiceProductService invoiceProductService,
                              ProductService productService, MapperUtil mapperUtil, SecurityService securityService) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceProductService = invoiceProductService;
        this.productService = productService;
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

    @Override
    public InvoiceDto getNewInvoice(InvoiceType invoiceType){
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setInvoiceNo(generateInvoiceNo(invoiceType));
        invoiceDto.setDate(LocalDate.now());
        Company company = mapperUtil.convert(securityService.getLoggedInUser().getCompany(), new Company());
        invoiceDto.setCompany(mapperUtil.convert(company, new CompanyDto()));
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
        List<InvoiceProductDto> invoiceProductList = invoiceProductService.getInvoiceProductsOfInvoice(invoiceId);
        if(invoice.getInvoiceType()==InvoiceType.SALES){
            for(InvoiceProductDto salesInvoiceProduct : invoiceProductList){
                if(productIsEnough(salesInvoiceProduct)){
                    updateQuantityOfProductForSalesInvoice(salesInvoiceProduct);
                    salesInvoiceProduct.setRemainingQuantity(salesInvoiceProduct.getQuantity());
                    invoiceProductService.update(salesInvoiceProduct);
                    setProfitLossOfInvoiceProductsForSalesInvoice(salesInvoiceProduct);
                }else{
                    System.out.println("This sale cannot be completed due to insufficient quantity of the product");
                    return null;
                }
            }
        }else{
            for(InvoiceProductDto purchaseInvoiceProduct : invoiceProductList) {
                updateQuantityOfProductForPurchaseInvoice(purchaseInvoiceProduct);
                purchaseInvoiceProduct.setRemainingQuantity(purchaseInvoiceProduct.getQuantity());
                // todo toplamamiz gerekmez mi? purchaseInvoiceProduct.getRemainingQuantity +
                invoiceProductService.update(purchaseInvoiceProduct);
            }
        }
        invoice.setInvoiceStatus(InvoiceStatus.APPROVED);
        invoice.setDate(LocalDate.now());
        invoiceRepository.save(invoice);
        return mapperUtil.convert(invoice, new InvoiceDto());
    }

    private boolean productIsEnough(InvoiceProductDto salesInvoiceProduct) {
        return salesInvoiceProduct.getProduct().getQuantityInStock() >= salesInvoiceProduct.getQuantity();
    }

    private void updateQuantityOfProductForSalesInvoice(InvoiceProductDto salesInvoiceProduct) {
        ProductDto productDto = salesInvoiceProduct.getProduct();
        productDto.setQuantityInStock(productDto.getQuantityInStock() - salesInvoiceProduct.getQuantity());
        productService.update(productDto.getId(), productDto);
    }

    private void updateQuantityOfProductForPurchaseInvoice(InvoiceProductDto purchaseInvoiceProduct) {
        ProductDto productDto = purchaseInvoiceProduct.getProduct();
        productDto.setQuantityInStock(productDto.getQuantityInStock() + purchaseInvoiceProduct.getQuantity());
        productService.update(productDto.getId(), productDto);
    }

    private void setProfitLossOfInvoiceProductsForSalesInvoice(InvoiceProductDto salesInvoiceProduct) {
        List<InvoiceProductDto> notSoldPurchaseInvoiceProducts = invoiceProductService.findInvoiceProductsByInvoiceTypeAndProductRemainingQuantity(InvoiceType.PURCHASE, salesInvoiceProduct.getProduct(), 0);
        for (InvoiceProductDto notSoldPurchaseInvoiceProduct : notSoldPurchaseInvoiceProducts) {
            if (salesInvoiceProduct.getRemainingQuantity() > notSoldPurchaseInvoiceProduct.getRemainingQuantity()) {
                int costTotalForQty = notSoldPurchaseInvoiceProduct.getTotal() * notSoldPurchaseInvoiceProduct.getRemainingQuantity() / notSoldPurchaseInvoiceProduct.getQuantity();
                int salesPriceForQty = salesInvoiceProduct.getPrice() * notSoldPurchaseInvoiceProduct.getRemainingQuantity();
                int salesTaxForQty = salesPriceForQty * salesInvoiceProduct.getTax() / 100;
                int salesTotalForQty = salesPriceForQty + salesTaxForQty;
                int profitLoss = salesInvoiceProduct.getProfitLoss() + (salesTotalForQty - costTotalForQty);
                salesInvoiceProduct.setRemainingQuantity(salesInvoiceProduct.getRemainingQuantity() - notSoldPurchaseInvoiceProduct.getRemainingQuantity());
                notSoldPurchaseInvoiceProduct.setRemainingQuantity(0);
                salesInvoiceProduct.setProfitLoss(profitLoss);
                invoiceProductService.update(notSoldPurchaseInvoiceProduct);
                invoiceProductService.update(salesInvoiceProduct);
            } else {
                int costTotalForQty = notSoldPurchaseInvoiceProduct.getTotal() * salesInvoiceProduct.getRemainingQuantity() / notSoldPurchaseInvoiceProduct.getQuantity();
                int salesPriceForQty = salesInvoiceProduct.getPrice() * salesInvoiceProduct.getRemainingQuantity();
                int salesTaxForQty = salesPriceForQty * salesInvoiceProduct.getTax() / 100;
                int salesTotalForQty = salesPriceForQty + salesTaxForQty;
                int profitLoss = salesInvoiceProduct.getProfitLoss() + salesTotalForQty - costTotalForQty;
                notSoldPurchaseInvoiceProduct.setRemainingQuantity(notSoldPurchaseInvoiceProduct.getRemainingQuantity() - salesInvoiceProduct.getRemainingQuantity());
                salesInvoiceProduct.setRemainingQuantity(0);
                salesInvoiceProduct.setProfitLoss(profitLoss);
                invoiceProductService.update(notSoldPurchaseInvoiceProduct);
                invoiceProductService.update(salesInvoiceProduct);
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


}
