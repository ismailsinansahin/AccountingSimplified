package com.cydeo.service.implementation;

import com.cydeo.dto.InvoiceProductDto;
import com.cydeo.dto.ProductDto;
import com.cydeo.entity.Invoice;
import com.cydeo.entity.InvoiceProduct;
import com.cydeo.entity.Product;
import com.cydeo.enums.InvoiceType;
import com.cydeo.exception.AccountingException;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.InvoiceProductRepository;
import com.cydeo.service.InvoiceProductService;
import com.cydeo.service.InvoiceService;
import com.cydeo.service.ProductService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
                .sorted(Comparator.comparing((InvoiceProduct each) -> each.getInvoice().getDate()).reversed())
                .map(each -> {
                    InvoiceProductDto dto = mapperUtil.convert(each, new InvoiceProductDto());
                    dto.setTotal(each.getPrice().multiply(BigDecimal.valueOf(each.getQuantity() * (each.getTax() + 100) / 100d)));
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
    @Transactional
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
                    throw new AccountingException("This sale cannot be completed due to insufficient quantity of the product");
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

    //    @Override
//    public void completeApprovalProcedures2(Long invoiceId, InvoiceType type) {
//        // Get All InvoiceProducts related to that invoice...
//        List<InvoiceProduct> invoiceProductList = invoiceProductRepository.findAllByInvoice_Id(invoiceId);
//        // If type of invoice is SALES
//        if (type == InvoiceType.SALES) {
//            // Then Loop through all sale invoiceProducts...
//            for (InvoiceProduct each : invoiceProductList) {
//                // If You have enough quantity of that product in your stock then move to next step
//                if (each.getProduct().getQuantityInStock() >= each.getQuantity()) {
//                    // Update Product-QuantityInStock value to after sale complete...
//                    updateQuantityOfProduct(each, type);
//                    // Set remainingQuantity of each InvoiceProduct to its quantity.. We're gonna use it later to control our loop..
//                    // Note that this is the first time we assign a value to remainingQuantity...
//                    each.setRemainingQuantity(each.getQuantity());
//                    // Save each Invoice Repository to database - actually update...
//                    invoiceProductRepository.save(each);
//                    // Write a method to set ProfitLoss field of each sales InvoiceProduct...
//                    setProfitLossOfInvoiceProductsForSalesInvoice(each);
//                } else {
//                    throw new NoSuchElementException("This sale cannot be completed due to insufficient quantity of the product"); // todo custom exception
//                }
//            }
//        } else {
//            // Loop through each purchase InvoiceProducts
//            for (InvoiceProduct each : invoiceProductList) {
//                // Update quantity of products in Invoice (as an Invoice Product)
//                updateQuantityOfProduct(each, type);
//                // Set remainingQuantity of each InvoiceProduct to its quantity
//                // Note that this is the first time we assign a value to remainingQuantity...
//                each.setRemainingQuantity(each.getQuantity());
//                // Save each Invoice Repository to database - actually update...
//                invoiceProductRepository.save(each);
//            }
//        }
//    }

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
        List<InvoiceProduct> availableProductsForSale = invoiceProductRepository
                .findInvoiceProductsByInvoiceInvoiceTypeAndProductAndRemainingQuantityNotOrderByIdAsc(InvoiceType.PURCHASE, salesInvoiceProduct.getProduct(), 0);
        for (InvoiceProduct availableProduct : availableProductsForSale) {
            if (salesInvoiceProduct.getRemainingQuantity() <= availableProduct.getRemainingQuantity()) {
                BigDecimal costTotalForQty = availableProduct.getPrice().multiply(
                        BigDecimal.valueOf(salesInvoiceProduct.getRemainingQuantity() * (availableProduct.getTax() + 100) / 100d));
                BigDecimal salesTotalForQty = salesInvoiceProduct.getPrice().multiply(
                        BigDecimal.valueOf(salesInvoiceProduct.getRemainingQuantity() * (salesInvoiceProduct.getTax() + 100) / 100d));
                BigDecimal profitLoss = salesInvoiceProduct.getProfitLoss().add(salesTotalForQty.subtract(costTotalForQty));
                availableProduct.setRemainingQuantity(availableProduct.getRemainingQuantity() - salesInvoiceProduct.getRemainingQuantity());
                salesInvoiceProduct.setRemainingQuantity(0);
                salesInvoiceProduct.setProfitLoss(profitLoss);
                invoiceProductRepository.save(availableProduct);
                invoiceProductRepository.save(salesInvoiceProduct);
                break;
            } else {
                BigDecimal costTotalForQty = availableProduct.getPrice()
                        .multiply(BigDecimal.valueOf(availableProduct.getRemainingQuantity() * (availableProduct.getTax() + 100) / 100d));
                BigDecimal salesTotalForQty = salesInvoiceProduct.getPrice().multiply(
                        BigDecimal.valueOf(availableProduct.getRemainingQuantity() * (salesInvoiceProduct.getTax() + 100) / 100d));
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

//    private void setProfitLossOfInvoiceProductsForSalesInvoice2(InvoiceProduct toBeSoldProduct) {
//        // Get all previous Purchase_Invoice_Products which remaining quantity is not ZERO...
//        List<InvoiceProduct> purchasedProducts = findNotSoldProducts(toBeSoldProduct.getProduct());
//        // We'll start looking from latest dated InvoiceProduct...Let s say 'purchasedProduct'
//        for (InvoiceProduct purchasedProduct : purchasedProducts) {
//            // If the remaining quantity of the latest purchasedProduct, is smaller than the quantity we want to sell
//            if (toBeSoldProduct.getRemainingQuantity() <= purchasedProduct.getRemainingQuantity()) {
//                // How much money I spent to buy that much for this very same product before?...
//                BigDecimal costTotalForQty = purchasedProduct.getPrice().multiply(
//                        BigDecimal.valueOf(toBeSoldProduct.getRemainingQuantity() * (purchasedProduct.getTax() +100)/100d));
//                // How much money I will earn from that sale?
//                BigDecimal salesTotalForQty = toBeSoldProduct.getPrice().multiply(
//                        BigDecimal.valueOf(toBeSoldProduct.getRemainingQuantity() * (toBeSoldProduct.getTax() +100)/100d));
//                // Subtract above values and reach your Profit/Loss...
//                BigDecimal profitLoss = toBeSoldProduct.getProfitLoss().add(salesTotalForQty.subtract( costTotalForQty));
//                // Update remaining quantity of purchaseInvoiceProduct, so we can calculate this remained products next time..
//                purchasedProduct.setRemainingQuantity(purchasedProduct.getRemainingQuantity() - toBeSoldProduct.getRemainingQuantity());
//                // Set profitLoss to the field of saleInvoiceProduct..
//                toBeSoldProduct.setProfitLoss(profitLoss);
//                // We need to set it to zero, because we have a condition at the beginning of this for loop...
//                toBeSoldProduct.setRemainingQuantity(0);
//                // Save purchaseInvoiceProduct and salesInvoiceProduct to database... Because we made some changes...
//                invoiceProductRepository.save(purchasedProduct);
//                invoiceProductRepository.save(toBeSoldProduct);
//                break;
//            } else {
//                // How much money I spent to buy that much for this very same product before?...
//                BigDecimal costTotalForQty = purchasedProduct.getPrice()
//                        .multiply(BigDecimal.valueOf(purchasedProduct.getRemainingQuantity() * (purchasedProduct.getTax() +100)/100d));
//                // How much money I will earn from that sale?
//                BigDecimal salesTotalForQty = toBeSoldProduct.getPrice().multiply(
//                        BigDecimal.valueOf(purchasedProduct.getRemainingQuantity() * (toBeSoldProduct.getTax() +100)/100d));
//                // Add new Profit/Loss to previous one.. Because formerly we made some sale, but not all the products from that purchase..
//                BigDecimal profitLoss = toBeSoldProduct.getProfitLoss()
//                        .add(salesTotalForQty.subtract(costTotalForQty));
//                // Set remaining quantity of that purchasedInvoice to zero, means we are selling all we purchased in that Invoice...
//                purchasedProduct.setRemainingQuantity(0);
//                // We need to setRemainingQuantity of saleInvoiceProduct in order to control for loop we are in... There is a condition..
//                toBeSoldProduct.setRemainingQuantity(toBeSoldProduct.getRemainingQuantity() - purchasedProduct.getRemainingQuantity());
//                // Set profit/Loss of the saleInvoiceProduct
//                toBeSoldProduct.setProfitLoss(profitLoss);
//                // Save purchaseInvoiceProduct and salesInvoiceProduct to database... Because we made some changes...
//                invoiceProductRepository.save(purchasedProduct);
//                invoiceProductRepository.save(toBeSoldProduct);
//            }
//        }
//    }

    @Override
    public void delete(Long invoiceProductId) {
        InvoiceProduct invoiceProduct = invoiceProductRepository.findInvoiceProductById(invoiceProductId);
        invoiceProduct.setIsDeleted(true);
        invoiceProductRepository.save(invoiceProduct);
    }

    @Override
    public List<InvoiceProduct> findAllInvoiceProductsByProductId(Long id) {
        return invoiceProductRepository.findAllInvoiceProductByProductId(id);
    }

}
