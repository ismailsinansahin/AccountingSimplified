package com.cydeo.accountingsimplified.controller;

import com.cydeo.accountingsimplified.dto.ProductDto;
import com.cydeo.accountingsimplified.enums.ProductUnit;
import com.cydeo.accountingsimplified.service.CategoryService;
import com.cydeo.accountingsimplified.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String list() {
        return "/product/product-list";
    }

    @GetMapping("/create")
    public String create(Model model) throws Exception {
        model.addAttribute("newProduct", new ProductDto());
        return "product/product-create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("newProduct") ProductDto productDto, BindingResult bindingResult, Model model) throws Exception {

        if (productService.isProductNameExist(productDto)) {
            bindingResult.rejectValue("name", " ", "This Product Name already exists.");
        }

        if (bindingResult.hasErrors()) {
            return "product/product-create";
        }
        productService.save(productDto);
        return "redirect:/products/list";
    }

    @GetMapping("/update/{productId}")
    public String update(@PathVariable("productId") Long productId, Model model) throws Exception {
        model.addAttribute("product", productService.findProductById(productId));
        return "product/product-update";
    }

    @PostMapping("/update/{productId}")
    public String update(@Valid @ModelAttribute("product") ProductDto productDto, BindingResult bindingResult, @PathVariable("productId") Long productId, Model model) throws Exception {
        productDto.setId(productId);
        if (productService.isProductNameExist(productDto)) {
            bindingResult.rejectValue("name", " ", "This Product Name already exists.");
        }

        if (bindingResult.hasErrors()) {
            return "product/product-update";
        }
        productService.update(productId, productDto);
        return "redirect:/products/list";
    }

    @GetMapping("/delete/{productId}")
    public String delete(@PathVariable("productId") Long productId, RedirectAttributes redirAttrs) {
        if (productService.checkProductQuantity(productId))  {
            redirAttrs.addFlashAttribute("error", "This product can not be deleted, You have Invoice/s with that product...");
            return "redirect:/products/list";
        }
        productService.delete(productId);
        return "redirect:/products/list";
    }

    @ModelAttribute
    public void commonAttributes(Model model) throws Exception {
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("productUnits", Arrays.asList(ProductUnit.values()));
        model.addAttribute("title", "Cydeo Accounting-Product");
    }

}
