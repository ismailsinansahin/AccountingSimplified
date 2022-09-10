package com.cydeo.accountingsimplified.controller;

import com.cydeo.accountingsimplified.dto.ProductDto;
import com.cydeo.accountingsimplified.enums.ProductUnit;
import com.cydeo.accountingsimplified.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/list")
    public String navigateToProductList(Model model) throws Exception {
        model.addAttribute("products", productService.getAllProducts());
        return "/product/product-list";
    }

    @GetMapping("/create")
    public String navigateToProductCreate(Model model) throws Exception {
        model.addAttribute("newProduct", new ProductDto());
        model.addAttribute("categories", productService.getAllCategories());
        model.addAttribute("productUnits", Arrays.asList(ProductUnit.values()));
        return "/product/product-create";
    }

    @PostMapping("/create")
    public String createNewProduct(ProductDto productDto) throws Exception {
        productService.create(productDto);
        return "redirect:/products/list";
    }

    @PostMapping(value = "/actions/{productId}", params = {"action=update"})
    public String navigateToProductUpdate(@PathVariable("productId") Long productId){
        return "redirect:/products/update/" + productId;
    }

    @GetMapping("/update/{productId}")
    public String navigateToProductUpate(@PathVariable("productId") Long productId, Model model) throws Exception {
        model.addAttribute("product", productService.findProductById(productId));
        model.addAttribute("categories", productService.getAllCategories());
        model.addAttribute("productUnits", Arrays.asList(ProductUnit.values()));
        return "/product/product-update";
    }

    @PostMapping("/update/{productId}")
    public String updateProduct(@PathVariable("productId") Long productId, ProductDto productDto) {
        productService.update(productId, productDto);
        return "redirect:/products/list";
    }

    @PostMapping(value = "/actions/{productId}", params = {"action=delete"})
    public String deleteProduct(@PathVariable("productId") Long productId){
        productService.delete(productId);
        return "redirect:/products/list";
    }

}