package com.cydeo.accountingsimplified.service.implementation;

import com.cydeo.accountingsimplified.dto.CategoryDto;
import com.cydeo.accountingsimplified.entity.Category;
import com.cydeo.accountingsimplified.mapper.MapperUtil;
import com.cydeo.accountingsimplified.repository.CategoryRepository;
import com.cydeo.accountingsimplified.service.CategoryService;
import com.cydeo.accountingsimplified.service.ProductService;
import com.cydeo.accountingsimplified.service.SecurityService;
import com.cydeo.accountingsimplified.service.common.CommonService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class CategoryServiceImpl extends CommonService implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductService productService;

    public CategoryServiceImpl(SecurityService securityService, MapperUtil mapperUtil, CategoryRepository categoryRepository, ProductService productService) {
        super(securityService, mapperUtil);
        this.categoryRepository = categoryRepository;
        this.productService = productService;
    }

    @Override
    public CategoryDto findCategoryById(Long categoryId) {
        CategoryDto dto = mapperUtil.convert(categoryRepository.findById(categoryId).get(), new CategoryDto());
        dto.setHasProduct(hasProduct(dto.getId()));
        return dto;
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryRepository
                .findAllByCompany(getCompany())
                .stream()
                .sorted(Comparator.comparing(Category::getDescription))
                .map(each -> mapperUtil.convert(each, new CategoryDto()))
                .peek(dto -> dto.setHasProduct(hasProduct(dto.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto create(CategoryDto categoryDto) throws Exception {
        Category category = mapperUtil.convert(categoryDto, new Category());
        category.setCompany(getCompany());
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
        Category existingCategory = categoryRepository.findByDescriptionAndCompany(categoryDTO.getDescription(), getCompany());
        if (existingCategory == null) return false;
        return !existingCategory.getId().equals(categoryDTO.getId());
    }
}
