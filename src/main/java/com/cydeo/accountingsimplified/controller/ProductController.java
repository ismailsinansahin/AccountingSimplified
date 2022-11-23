package com.cydeo.accountingsimplified.controller;

import com.cydeo.accountingsimplified.app_util.ErrorGenerator;
import com.cydeo.accountingsimplified.dto.ProductDto;
import com.cydeo.accountingsimplified.enums.ProductUnit;
import com.cydeo.accountingsimplified.service.CategoryService;
import com.cydeo.accountingsimplified.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/list")
    public String navigateToProductList(Model model) throws Exception {
        model.addAttribute("products", productService.getAllProducts());
        return "/product/product-list";
    }

    @GetMapping("/create")
    public String navigateToProductCreate(Model model) throws Exception {
        model.addAttribute("newProduct", new ProductDto());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("productUnits", Arrays.asList(ProductUnit.values()));
        return "/product/product-create";
    }

    @PostMapping("/create")
    public String createNewProduct(@Valid @ModelAttribute("newProduct") ProductDto productDto, BindingResult bindingResult, Model model) throws Exception {

        if (productService.isProductNameExist(productDto.getName())) {
            ErrorGenerator.generateErrorMessage(bindingResult, "name", "This Product Name already exists.");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("productUnits", Arrays.asList(ProductUnit.values()));
            return "/product/product-create";
        }
        productService.save(productDto);
        return "redirect:/products/list";
    }

    @GetMapping("/update/{productId}")
    public String navigateToProductUpdate(@PathVariable("productId") Long productId, Model model) throws Exception {
        model.addAttribute("product", productService.findProductById(productId));
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("productUnits", Arrays.asList(ProductUnit.values()));
        return "/product/product-update";
    }

    @PostMapping("/update/{productId}")
    public String updateProduct(@Valid @ModelAttribute("product") ProductDto productDto, BindingResult bindingResult, @PathVariable("productId") Long productId, Model model) throws Exception {

        boolean isProductNameSame = productService.findProductById(productId).getName().equals(productDto.getName());
        if (productService.isProductNameExist(productDto.getName()) && !isProductNameSame) {
            ErrorGenerator.generateErrorMessage(bindingResult, "name", "This Product Name already exists");
        }

        if (bindingResult.hasErrors()) {
            productDto.setId(productId);
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("productUnits", Arrays.asList(ProductUnit.values()));
            return "/product/product-update";
        }
        productService.update(productId, productDto);
        return "redirect:/products/list";
    }

    @GetMapping("/delete/{productId}")
    public String deleteProduct(@PathVariable("productId") Long productId) {
        productService.delete(productId);
        return "redirect:/products/list";
    }

}
