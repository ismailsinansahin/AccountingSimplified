package com.cydeo.accountingsimplified.service.implementation;

import com.cydeo.accountingsimplified.dto.InvoiceProductDto;
import com.cydeo.accountingsimplified.dto.ProductDto;
import com.cydeo.accountingsimplified.entity.Invoice;
import com.cydeo.accountingsimplified.entity.InvoiceProduct;
import com.cydeo.accountingsimplified.entity.Product;
import com.cydeo.accountingsimplified.enums.InvoiceType;
import com.cydeo.accountingsimplified.mapper.MapperUtil;
import com.cydeo.accountingsimplified.repository.InvoiceProductRepository;
import com.cydeo.accountingsimplified.service.InvoiceProductService;
import com.cydeo.accountingsimplified.service.InvoiceService;
import com.cydeo.accountingsimplified.service.ProductService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
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
                .findAllByInvoice(invoice)
                .stream()
                .sorted(Comparator.comparing((InvoiceProduct each) -> each.getInvoice().getInvoiceNo()).reversed())
                .map(each -> {
                    InvoiceProductDto dto = mapperUtil.convert(each, new InvoiceProductDto());
                    dto.setTotal(each.getPrice().multiply(BigDecimal.valueOf(each.getQuantity())));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void save(Long invoiceId, InvoiceProductDto invoiceProductDto) {
        Invoice invoice = mapperUtil.convert(invoiceService.findInvoiceById(invoiceId), new Invoice());
        InvoiceProduct invoiceProduct = mapperUtil.convert(invoiceProductDto, new InvoiceProduct());
        invoiceProduct.setInvoice(invoice);
        invoiceProduct.setProfitLoss(BigDecimal.ZERO);
        invoiceProductRepository.save(invoiceProduct);
    }

    @Override
    public void completeApprovalProcedures(Long invoiceId, InvoiceType type) {
        List<InvoiceProduct> invoiceProductList = invoiceProductRepository.findAllByInvoice_Id(invoiceId);
        if (type == InvoiceType.SALES) {
            for (InvoiceProduct salesInvoiceProduct : invoiceProductList) {
                if (checkProductQuantity(mapperUtil.convert(salesInvoiceProduct, new InvoiceProductDto()))) {
                    updateQuantityOfProduct(salesInvoiceProduct, type);
                    salesInvoiceProduct.setRemainingQuantity(salesInvoiceProduct.getQuantity());
                    invoiceProductRepository.save(salesInvoiceProduct);
                    setProfitLossOfInvoiceProductsForSalesInvoice(salesInvoiceProduct);
                } else {
                    throw new NoSuchElementException("This sale cannot be completed due to insufficient quantity of the product"); // todo custom exception
                }
            }
        } else {
            for (InvoiceProduct purchaseInvoiceProduct : invoiceProductList) {
                updateQuantityOfProduct(purchaseInvoiceProduct, type);
                purchaseInvoiceProduct.setRemainingQuantity(purchaseInvoiceProduct.getQuantity());
                invoiceProductRepository.save(purchaseInvoiceProduct);
            }
        }
    }

    @Override
    public boolean checkProductQuantity(InvoiceProductDto salesInvoiceProduct) {
        return salesInvoiceProduct.getProduct().getQuantityInStock() >= salesInvoiceProduct.getQuantity();
    }

    private void updateQuantityOfProduct(InvoiceProduct invoiceProduct, InvoiceType type) {
        ProductDto productDto = mapperUtil.convert(invoiceProduct.getProduct(), new ProductDto());
        if (type.equals(InvoiceType.SALES)) {
            productDto.setQuantityInStock(productDto.getQuantityInStock() - invoiceProduct.getQuantity());
        } else {
            productDto.setQuantityInStock(productDto.getQuantityInStock() + invoiceProduct.getQuantity());
        }
        productService.update(productDto.getId(), productDto);
    }

    private void setProfitLossOfInvoiceProductsForSalesInvoice(InvoiceProduct salesInvoiceProduct) {
        List<InvoiceProduct> availableProductsForSale =
                findInvoiceProductsByInvoiceTypeAndProductRemainingQuantity(InvoiceType.PURCHASE, salesInvoiceProduct.getProduct(), 0);
        for (InvoiceProduct availableProduct : availableProductsForSale) {
            if (salesInvoiceProduct.getRemainingQuantity() <= availableProduct.getRemainingQuantity()) {
                BigDecimal costTotalForQty = availableProduct.getPrice().multiply(BigDecimal.valueOf(salesInvoiceProduct.getRemainingQuantity()));
                BigDecimal salesTotalForQty = salesInvoiceProduct.getPrice().multiply(BigDecimal.valueOf(salesInvoiceProduct.getRemainingQuantity()));
                BigDecimal profitLoss = salesInvoiceProduct.getProfitLoss().add(salesTotalForQty.subtract( costTotalForQty));
                availableProduct.setRemainingQuantity(availableProduct.getRemainingQuantity() - salesInvoiceProduct.getRemainingQuantity());
                salesInvoiceProduct.setRemainingQuantity(0);
                salesInvoiceProduct.setProfitLoss(profitLoss);
                invoiceProductRepository.save(availableProduct);
                invoiceProductRepository.save(salesInvoiceProduct);
                break;
            } else {
                BigDecimal costTotalForQty = availableProduct.getPrice()
                        .multiply(BigDecimal.valueOf(availableProduct.getRemainingQuantity()));
                BigDecimal salesTotalForQty = salesInvoiceProduct.getPrice().multiply(BigDecimal.valueOf(availableProduct.getRemainingQuantity()));
                BigDecimal profitLoss = salesInvoiceProduct.getProfitLoss()
                        .add(salesTotalForQty.subtract(costTotalForQty));
                salesInvoiceProduct.setRemainingQuantity(salesInvoiceProduct.getRemainingQuantity() - availableProduct.getRemainingQuantity());
                availableProduct.setRemainingQuantity(0);
                salesInvoiceProduct.setProfitLoss(profitLoss);
                invoiceProductRepository.save(availableProduct);
                invoiceProductRepository.save(salesInvoiceProduct);
            }
        }
    }

    private BigDecimal getAmountOfInvoiceProduct(InvoiceProductDto invoiceProductDto) {
        int quantity = invoiceProductDto.getQuantity();
        BigDecimal price = invoiceProductDto.getPrice();
        int tax = invoiceProductDto.getTax();
        return price.multiply(BigDecimal.valueOf(quantity).add(price.multiply(BigDecimal.valueOf(tax/100))));
    }

    @Override
    public void delete(Long invoiceProductId) {
        InvoiceProduct invoiceProduct = invoiceProductRepository.findInvoiceProductById(invoiceProductId);
        invoiceProduct.setIsDeleted(true);
        invoiceProductRepository.save(invoiceProduct);
    }

    @Override
    public BigDecimal getPriceOfInvoiceProductWithoutTax(Long id) {
        Invoice invoice = mapperUtil.convert(invoiceService.findInvoiceById(id), new Invoice());
        List<InvoiceProduct> invoiceProductsOfInvoice = invoiceProductRepository.findAllByInvoice(invoice);
        return invoiceProductsOfInvoice.stream()
                .map(p -> p.getPrice().multiply(BigDecimal.valueOf(p.getQuantity() * 100 / (100d + p.getTax())))
                        .setScale(2, RoundingMode.HALF_UP))
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

    @Override
    public BigDecimal getTaxOfInvoiceProduct(Long id) {
        Invoice invoice = mapperUtil.convert(invoiceService.findInvoiceById(id), new Invoice());
        List<InvoiceProduct> invoiceProductsOfInvoice = invoiceProductRepository.findAllByInvoice(invoice);
        return invoiceProductsOfInvoice.stream()
                .map(p -> p.getPrice().multiply(BigDecimal.valueOf(p.getQuantity() * p.getTax() / (100d + p.getTax())))
                        .setScale(2, RoundingMode.HALF_UP))
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

    @Override
    public BigDecimal getTotalOfInvoiceProduct(Long id) {
        Invoice invoice = mapperUtil.convert(invoiceService.findInvoiceById(id), new Invoice());
        List<InvoiceProduct> invoiceProductsOfInvoice = invoiceProductRepository.findAllByInvoice(invoice);
        return invoiceProductsOfInvoice.stream()
                .map(p -> p.getPrice().multiply(BigDecimal.valueOf(p.getQuantity())))
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

    @Override
    public BigDecimal getProfitLossOfInvoiceProduct(Long id) {
        Invoice invoice = mapperUtil.convert(invoiceService.findInvoiceById(id), new Invoice());
        List<InvoiceProduct> invoiceProductsOfInvoice = invoiceProductRepository.findAllByInvoice(invoice);
        return invoiceProductsOfInvoice.stream()
                .map(InvoiceProduct::getProfitLoss)
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }


    @Override
    public List<InvoiceProduct> findInvoiceProductsByInvoiceTypeAndProductRemainingQuantity(InvoiceType type, Product product, Integer remainingQuantity) {
        return invoiceProductRepository.findInvoiceProductsByInvoiceInvoiceTypeAndProductAndRemainingQuantityNotOrderByIdAsc(type, product, remainingQuantity);
    }

    @Override
    public List<InvoiceProduct> findAllInvoiceProductsByProductId(Long id) {
        return invoiceProductRepository.findAllInvoiceProductByProductId(id);
    }

}
