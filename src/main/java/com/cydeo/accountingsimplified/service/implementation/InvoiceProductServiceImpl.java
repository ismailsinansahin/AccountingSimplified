package com.cydeo.accountingsimplified.service.implementation;

import com.cydeo.accountingsimplified.dto.InvoiceProductDto;
import com.cydeo.accountingsimplified.dto.ProductDto;
import com.cydeo.accountingsimplified.entity.Invoice;
import com.cydeo.accountingsimplified.entity.InvoiceProduct;
import com.cydeo.accountingsimplified.entity.Product;
import com.cydeo.accountingsimplified.enums.InvoiceType;
import com.cydeo.accountingsimplified.mapper.MapperUtil;
import com.cydeo.accountingsimplified.repository.InvoiceProductRepository;
import com.cydeo.accountingsimplified.service.ProductService;
import org.springframework.context.annotation.Lazy;
import com.cydeo.accountingsimplified.service.InvoiceProductService;
import com.cydeo.accountingsimplified.service.InvoiceService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceProductServiceImpl implements InvoiceProductService {

    private final InvoiceProductRepository invoiceProductRepository;
    private final InvoiceService invoiceService;
    private final ProductService productService;
    private final MapperUtil mapperUtil;

    public InvoiceProductServiceImpl(InvoiceProductRepository invoiceProductRepository,
                                     @Lazy InvoiceService invoiceService, ProductService productService,
                                     MapperUtil mapperUtil) {
        this.invoiceProductRepository = invoiceProductRepository;
        this.invoiceService = invoiceService;
        this.productService = productService;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public InvoiceProductDto findInvoiceProductById(long id) {
        return mapperUtil.convert(invoiceProductRepository.findInvoiceProductById(id), new InvoiceProductDto());
    }

    @Override
    public List<InvoiceProductDto> getInvoiceProductsOfInvoice(Long invoiceId) {
        Invoice invoice = mapperUtil.convert(invoiceService.findInvoiceById(invoiceId), new Invoice());
        return invoiceProductRepository
                .findInvoiceProductsByInvoice(invoice)
                .stream()
                .map(each -> mapperUtil.convert(each, new InvoiceProductDto()))
                .collect(Collectors.toList());
    }

    @Override
    public void save(Long invoiceId, InvoiceProductDto invoiceProductDto) {
        Invoice invoice = mapperUtil.convert(invoiceService.findInvoiceById(invoiceId), new Invoice());
        InvoiceProduct invoiceProduct = mapperUtil.convert(invoiceProductDto, new InvoiceProduct());
        invoiceProduct.setInvoice(invoice);
        invoiceProduct.setTotal(getAmountOfInvoiceProduct(invoiceProductDto));
        if(invoice.getInvoiceType() == InvoiceType.PURCHASE){
            invoiceProduct.setProfitLoss(0);
        }else{
            invoiceProduct.setProfitLoss(0);
            invoiceProduct.setRemainingQuantity(0);
        }
        invoiceProductRepository.save(invoiceProduct);
    }

    @Override
    public void completeApprovalProcedures(Long invoiceId, InvoiceType type) {
        List<InvoiceProduct> invoiceProductList = invoiceProductRepository.findInvoiceProductsByInvoice_Id(invoiceId);
        if(type==InvoiceType.SALES){
            for(InvoiceProduct salesInvoiceProduct : invoiceProductList){
                if(checkProductQuantity(mapperUtil.convert(salesInvoiceProduct, new InvoiceProductDto()))){
                    updateQuantityOfProductForSalesInvoice(salesInvoiceProduct);
                    salesInvoiceProduct.setRemainingQuantity(salesInvoiceProduct.getQuantity());
                    invoiceProductRepository.save(salesInvoiceProduct);
                    setProfitLossOfInvoiceProductsForSalesInvoice(salesInvoiceProduct);
                }else{
                    System.out.println("This sale cannot be completed due to insufficient quantity of the product");
                    throw new RuntimeException(); // todo custom exception
                }
            }
        }else{
            for(InvoiceProduct purchaseInvoiceProduct : invoiceProductList) {
                updateQuantityOfProductForPurchaseInvoice(purchaseInvoiceProduct);
                purchaseInvoiceProduct.setRemainingQuantity(purchaseInvoiceProduct.getQuantity());
                invoiceProductRepository.save(purchaseInvoiceProduct);
            }
        }
    }

    @Override
    public boolean checkProductQuantity(InvoiceProductDto salesInvoiceProduct) {
        return salesInvoiceProduct.getProduct().getQuantityInStock() >= salesInvoiceProduct.getQuantity();
    }

    private void updateQuantityOfProductForSalesInvoice(InvoiceProduct salesInvoiceProduct) {
        ProductDto productDto = mapperUtil.convert(salesInvoiceProduct.getProduct(), new ProductDto());
        productDto.setQuantityInStock(productDto.getQuantityInStock() - salesInvoiceProduct.getQuantity());
        productService.update(productDto.getId(), productDto);
    }

    private void updateQuantityOfProductForPurchaseInvoice(InvoiceProduct purchaseInvoiceProduct) {
        ProductDto productDto = mapperUtil.convert(purchaseInvoiceProduct.getProduct(), new ProductDto());
        productDto.setQuantityInStock(productDto.getQuantityInStock() + purchaseInvoiceProduct.getQuantity());
        productService.update(productDto.getId(), productDto);
    }

    private void setProfitLossOfInvoiceProductsForSalesInvoice(InvoiceProduct salesInvoiceProduct) {
        List<InvoiceProduct> notSoldPurchaseInvoiceProducts = findInvoiceProductsByInvoiceTypeAndProductRemainingQuantity(InvoiceType.PURCHASE, salesInvoiceProduct.getProduct(), 0);
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
        Invoice invoice = mapperUtil.convert(invoiceService.findInvoiceById(id), new Invoice());
        List<InvoiceProduct> invoiceProductsOfInvoice = invoiceProductRepository.findInvoiceProductsByInvoice(invoice);
        return invoiceProductsOfInvoice.stream().mapToInt(InvoiceProduct::getPrice).sum();
    }

    @Override
    public int getTaxOfInvoiceProduct(Long id) {
        Invoice invoice = mapperUtil.convert(invoiceService.findInvoiceById(id), new Invoice());
        List<InvoiceProduct> invoiceProductsOfInvoice = invoiceProductRepository.findInvoiceProductsByInvoice(invoice);
        return invoiceProductsOfInvoice.stream().mapToInt(InvoiceProduct::getTax).sum();
    }

    @Override
    public int getTotalOfInvoiceProduct(Long id) {
        Invoice invoice = mapperUtil.convert(invoiceService.findInvoiceById(id), new Invoice());
        List<InvoiceProduct> invoiceProductsOfInvoice = invoiceProductRepository.findInvoiceProductsByInvoice(invoice);
        return invoiceProductsOfInvoice.stream().mapToInt(InvoiceProduct::getTotal).sum();
    }

    @Override
    public int getProfitLossOfInvoiceProduct(Long id) {
        Invoice invoice = mapperUtil.convert(invoiceService.findInvoiceById(id), new Invoice());
        List<InvoiceProduct> invoiceProductsOfInvoice = invoiceProductRepository.findInvoiceProductsByInvoice(invoice);
        return invoiceProductsOfInvoice.stream().mapToInt(InvoiceProduct::getProfitLoss).sum();
    }



    @Override
    public List<InvoiceProduct> findInvoiceProductsByInvoiceTypeAndProductRemainingQuantity(InvoiceType type, Product product, Integer remainingQuantity ) {
       return invoiceProductRepository.findInvoiceProductsByInvoiceInvoiceTypeAndProductAndRemainingQuantityNotOrderByIdAsc(type,product,remainingQuantity);
    }

}
