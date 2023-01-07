package com.cydeo.service.implementation;

import com.cydeo.dto.CategoryDto;
import com.cydeo.entity.Category;
import com.cydeo.exception.AccountingException;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.CategoryRepository;
import com.cydeo.service.CategoryService;
import com.cydeo.service.CompanyService;
import com.cydeo.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.channels.AcceptPendingException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CompanyService companyService;
    private final ProductService productService;
    private final MapperUtil mapperUtil;


    @Override
    public CategoryDto findCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new AccountingException("Category not found"));
        return mapperUtil.convert(category, new CategoryDto());
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryRepository
                .findAllByCompany_Title(companyService.getCompanyDtoByLoggedInUser().getTitle())
                .stream()
                .sorted(Comparator.comparing(Category::getDescription))
                .map(each -> mapperUtil.convert(each, new CategoryDto()))
//                .peek(dto -> dto.setHasProduct(hasProduct(dto.getId())))  //sprint-1
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto create(CategoryDto categoryDto) throws Exception {
        categoryDto.setCompany(companyService.getCompanyDtoByLoggedInUser());
        Category category = mapperUtil.convert(categoryDto, new Category());
        return mapperUtil.convert(categoryRepository.save(category), new CategoryDto());
    }

    @Override
    public CategoryDto update(Long categoryId, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(categoryId).get();
        category.setDescription(categoryDto.getDescription());
        CategoryDto updated = mapperUtil.convert(categoryRepository.save(category), new CategoryDto());
//        updated.setHasProduct(hasProduct(dto.getId())))  //sprint-1
        return updated;
    }

    @Override
    public void delete(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).get();
        category.setIsDeleted(true);
        category.setDescription(category.getDescription() + "-" + category.getId());
        categoryRepository.save(category);
    }

//    private boolean hasProduct(Long categoryId) {
//        return productService.findAllProductsWithCategoryId(categoryId).size() > 0;
//    }

    @Override
    public boolean isCategoryDescriptionExist(CategoryDto categoryDTO) {
        Category existingCategory = categoryRepository.findByDescriptionAndCompany_Title(categoryDTO.getDescription(),
                companyService.getCompanyDtoByLoggedInUser().getTitle());
        if (existingCategory == null) return false;
        return !existingCategory.getId().equals(categoryDTO.getId());
    }
}
