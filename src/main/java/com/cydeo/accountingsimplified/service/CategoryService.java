package com.cydeo.accountingsimplified.service;

import com.cydeo.accountingsimplified.dto.CategoryDto;


import java.util.List;

public interface CategoryService {

    CategoryDto findCategoryById(Long categoryId);

    List<CategoryDto> getAllCategories() throws Exception;

    CategoryDto create(CategoryDto categoryDto) throws Exception;

    CategoryDto update(Long categoryId, CategoryDto categoryDto);

    void delete(Long categoryId);
    boolean hasProduct(Long categoryId);
    boolean isCategoryDescriptionExist(CategoryDto categoryDto);

}
