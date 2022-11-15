package com.cydeo.accountingsimplified.controller;

import com.cydeo.accountingsimplified.dto.CategoryDto;
import com.cydeo.accountingsimplified.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/list")
    public String navigateToCategoryList(Model model) throws Exception {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "/category/category-list";
    }

    @GetMapping("/create")
    public String navigateToCategoryCreate(Model model) {
        model.addAttribute("newCategory", new CategoryDto());
        return "/category/category-create";
    }

    @PostMapping("/create")
    public String createNewCategory(@Valid @ModelAttribute("newCategory") CategoryDto categoryDto, BindingResult bindingResult) throws Exception {

        if (bindingResult.hasErrors()) {
            return "/category/category-create";
        }

        categoryService.create(categoryDto);
        return "redirect:/categories/list";
    }

    @PostMapping(value = "/actions/{categoryId}", params = {"action=update"})
    public String navigateToCategoryUpdate(@PathVariable("categoryId") Long categoryId) {
        return "redirect:/categories/update/" + categoryId;
    }

    @GetMapping("/update/{categoryId}")
    public String navigateToCategoryUpate(@PathVariable("categoryId") Long categoryId, Model model) {
        CategoryDto categoryById = categoryService.findCategoryById(categoryId);
        categoryById.setHasProduct(categoryService.hasProduct(categoryId));
        model.addAttribute("category", categoryById);
        return "/category/category-update";
    }

    @PostMapping("/update/{categoryId}")
    public String updateCategory(@Valid @ModelAttribute("category") CategoryDto categoryDto, BindingResult bindingResult, @PathVariable("categoryId") Long categoryId) {

        if (bindingResult.hasErrors()) {
            categoryDto.setId(categoryId);
            return "/category/category-update";
        }

        categoryService.update(categoryId, categoryDto);
        return "redirect:/categories/list";
    }

    @PostMapping(value = "/actions/{categoryId}", params = {"action=delete"})
    public String activateCategory(@PathVariable("categoryId") Long categoryId) {
        categoryService.delete(categoryId);
        return "redirect:/categories/list";
    }

}
