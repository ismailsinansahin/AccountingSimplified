package com.cydeo.accountingsimplified.service;

import com.cydeo.accountingsimplified.dto.CategoryDto;
import com.cydeo.accountingsimplified.dto.ProductDto;

import java.util.List;

public interface ProductService {

    ProductDto findProductById(Long productId);
    List<ProductDto> getAllProducts() throws Exception;
    List<ProductDto> getProductsOfCompany();
    ProductDto save(ProductDto productDto);
    ProductDto update(Long productId, ProductDto productDto);
    void delete(Long productId);
    List<CategoryDto> getAllCategories() throws Exception;

}
