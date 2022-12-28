package com.cydeo.accountingsimplified.service.implementation;

import com.cydeo.accountingsimplified.dto.ProductDto;
import com.cydeo.accountingsimplified.entity.Category;
import com.cydeo.accountingsimplified.entity.Company;
import com.cydeo.accountingsimplified.entity.InvoiceProduct;
import com.cydeo.accountingsimplified.entity.Product;
import com.cydeo.accountingsimplified.mapper.MapperUtil;
import com.cydeo.accountingsimplified.repository.ProductRepository;
import com.cydeo.accountingsimplified.service.InvoiceProductService;
import com.cydeo.accountingsimplified.service.ProductService;
import com.cydeo.accountingsimplified.service.SecurityService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl extends CommonService implements ProductService {
    private final ProductRepository productRepository;
    private final InvoiceProductService invoiceProductService;

    public ProductServiceImpl(SecurityService securityService, MapperUtil mapperUtil, ProductRepository productRepository, @Lazy InvoiceProductService invoiceProductService) {
        super(securityService, mapperUtil);
        this.productRepository = productRepository;
        this.invoiceProductService = invoiceProductService;
    }

    @Override
    public ProductDto findProductById(Long productId) {
        Product product = productRepository.findById(productId).get();
        return mapperUtil.convert(product, new ProductDto());
    }

    @Override
    public List<ProductDto> getAllProducts() {
        return productRepository.findAllByCategoryCompany(getCompany())
                .stream()
                .sorted(Comparator.comparing((Product product) -> product.getCategory().getDescription())
                .thenComparing(Product::getName))
                .map(each -> mapperUtil.convert(each, new ProductDto()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> findAllProductsWithCategoryId(Long categoryId) {
        return productRepository.findByCategoryId(categoryId).stream()
                .map(product -> mapperUtil.convert(product, new ProductDto()))
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto save(ProductDto productDto) {
        Product product = mapperUtil.convert(productDto, new Product());
        product.setQuantityInStock(0);
        return mapperUtil.convert(productRepository.save(product), new ProductDto());
    }

    @Override
    public ProductDto update(Long productId, ProductDto productDto) {
        productDto.setId(productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new NoSuchElementException("Product " + productDto.getName() + "not found"));
        final int quantityInStock = productDto.getQuantityInStock() == null ? product.getQuantityInStock() : productDto.getQuantityInStock();
        productDto.setQuantityInStock(quantityInStock);
        product = productRepository.save(mapperUtil.convert(productDto, new Product()));
        return mapperUtil.convert(product, productDto);
    }

    @Override
    public void delete(Long productId) {
        Product product = productRepository.findById(productId).get();
        product.setIsDeleted(true);
        productRepository.save(product);
    }


    @Override
    public boolean isProductNameExist(ProductDto productDto) {
        Product existingProduct = productRepository.findByNameAndCategoryCompany(productDto.getName(),getCompany());
        if (existingProduct == null) return false;
        return !existingProduct.getId().equals(productDto.getId());
    }
    @Override
    public boolean checkProductQuantity(Long productId){
        Product product = productRepository.findById(productId).get();
        List<InvoiceProduct> invoiceProducts = invoiceProductService.findAllInvoiceProductsByProductId(product.getId());
        if (invoiceProducts.size() == 0 && product.getQuantityInStock() == 0){
            return true;
        }
        return false;
    }


}
