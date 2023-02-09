package com.cydeo.accountingsimplified.controller;

import com.cydeo.accountingsimplified.dto.CategoryDto;
import com.cydeo.accountingsimplified.dto.ResponseWrapper;
import com.cydeo.accountingsimplified.service.CategoryService;
import com.stripe.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper> getCategories() throws Exception {
        List<CategoryDto> categoryList = categoryService.getAllCategories();
        return ResponseEntity.ok(new ResponseWrapper("Categories are successfully retrieved",categoryList, HttpStatus.OK));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper> getCategoryById(@PathVariable("id") Long id){
        CategoryDto category = categoryService.findCategoryById(id);
        return ResponseEntity.ok(new ResponseWrapper("Category successfully retrieved",category, HttpStatus.OK));
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper> create(@ModelAttribute("newCategory") CategoryDto categoryDto) throws Exception {

        boolean categoryDescriptionExist = categoryService.isCategoryDescriptionExist(categoryDto);
        if (categoryDescriptionExist) {
            throw new Exception("This category description already exists");
        }
        categoryService.create(categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("Category successfully created",HttpStatus.CREATED));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseWrapper> update(@RequestBody CategoryDto categoryDto, @PathVariable("id") Long id) throws Exception {
        categoryDto.setId(id);
        boolean categoryDescriptionExist = categoryService.isCategoryDescriptionExist(categoryDto);
        if (categoryDescriptionExist) {
            throw new Exception("This category description already exists");
        }
        categoryService.update(id, categoryDto);
        return ResponseEntity.ok(new ResponseWrapper("Category successfully updated",HttpStatus.OK));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseWrapper> delete(@PathVariable("id") Long id) {
        categoryService.delete(id);
        return ResponseEntity.ok(new ResponseWrapper("Category successfully deleted",HttpStatus.OK));
    }

}
