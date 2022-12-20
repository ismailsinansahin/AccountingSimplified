package com.cydeo.service.implementation;

import com.cydeo.dto.CategoryDto;
import com.cydeo.entity.Category;
import com.cydeo.entity.Company;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.CategoryRepository;
import com.cydeo.service.CategoryService;
import com.cydeo.service.ProductService;
import com.cydeo.service.SecurityService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final SecurityService securityService;
    private final ProductService productService;
    private final MapperUtil mapperUtil;


    @Override
    public CategoryDto findCategoryById(Long categoryId) {
        CategoryDto dto = mapperUtil.convert(categoryRepository.findById(categoryId).get(), new CategoryDto());
        dto.setHasProduct(hasProduct(dto.getId()));
        return dto;
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        Company company = mapperUtil.convert(securityService.getLoggedInUser().getCompany(), new Company());
        return categoryRepository
                .findAllByCompany(company)
                .stream()
                .sorted(Comparator.comparing(Category::getDescription))
                .map(each -> mapperUtil.convert(each, new CategoryDto()))
                .peek(dto -> dto.setHasProduct(hasProduct(dto.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto create(CategoryDto categoryDto) throws Exception {
        Category category = mapperUtil.convert(categoryDto, new Category());
        Company company = mapperUtil.convert(securityService.getLoggedInUser().getCompany(), new Company());
        category.setCompany(company);
        return mapperUtil.convert(categoryRepository.save(category), new CategoryDto());
    }

    @Override
    public CategoryDto update(Long categoryId, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(categoryId).get();
        category.setDescription(categoryDto.getDescription());
        return mapperUtil.convert(categoryRepository.save(category), new CategoryDto());
    }

    @Override
    public void delete(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).get();
        category.setIsDeleted(true);
        category.setDescription(category.getDescription() + "-" + category.getId());
        categoryRepository.save(category);
    }

    private boolean hasProduct(Long categoryId) {
        return productService.findAllProductsWithCategoryId(categoryId).size() > 0;
    }

    @Override
    public boolean isCategoryDescriptionExist(CategoryDto categoryDTO) {
        Company actualCompany = mapperUtil.convert(securityService.getLoggedInUser().getCompany(), new Company());
        Category existingCategory = categoryRepository.findByDescriptionAndCompany(categoryDTO.getDescription(), actualCompany);
        if (existingCategory == null) return false;
        return !existingCategory.getId().equals(categoryDTO.getId());
    }
}
