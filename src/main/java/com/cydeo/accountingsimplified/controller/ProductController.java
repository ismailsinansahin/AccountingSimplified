package com.cydeo.accountingsimplified.controller;

import com.cydeo.accountingsimplified.dto.ProductDto;
import com.cydeo.accountingsimplified.dto.ResponseWrapper;
import com.cydeo.accountingsimplified.enums.ProductUnit;
import com.cydeo.accountingsimplified.service.CategoryService;
import com.cydeo.accountingsimplified.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper> getProducts() {
        List<ProductDto> products = productService.getAllProducts();
        return ResponseEntity.ok(new ResponseWrapper("Products successfully retrieved",products, HttpStatus.OK));

    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper> getProductById(@PathVariable("id") Long id) throws Exception {
        var product = productService.findProductById(id);
        return ResponseEntity.ok(new ResponseWrapper("Product successfully retrieved",product, HttpStatus.OK));
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper> createProduct(@RequestBody ProductDto productDto) throws Exception {

        if (productService.isProductNameExist(productDto)) {
            throw new Exception("This Product Name already exists.");
        }
        productService.save(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("Product successfully created",HttpStatus.CREATED));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseWrapper> update(@RequestBody ProductDto productDto, @PathVariable("id") Long id) throws Exception {
        productDto.setId(id);
        if (productService.isProductNameExist(productDto)) {
            throw new Exception("This Product Name already exists.");
        }
        productService.update(id, productDto);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper("Product successfully updated",HttpStatus.OK));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseWrapper> delete(@PathVariable("id") Long id) throws Exception {
        if (productService.checkProductQuantity(id))  {
            throw new Exception("This product can not be deleted, You have Invoice/s with that product...");
        }
        productService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper("Product successfully deleted",HttpStatus.OK));
    }

}
