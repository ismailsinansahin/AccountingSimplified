package com.cydeo.accountingsimplified.service;

import com.cydeo.accountingsimplified.dto.ProductDto;

import java.util.List;

public interface ProductService {

    ProductDto findProductById(Long productId);

    List<ProductDto> getAllProducts();

    ProductDto save(ProductDto productDto);

    ProductDto update(Long productId, ProductDto productDto);

    void delete(Long productId);

    List<ProductDto> findAllProductsWithCategoryId(Long categoryId);

    boolean isProductNameExist(ProductDto productDto);
    boolean checkProductQuantity(Long productId);

}
