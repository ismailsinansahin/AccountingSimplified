package com.cydeo.accountingsimplified.service.implementation;

import com.cydeo.accountingsimplified.dto.CategoryDto;
import com.cydeo.accountingsimplified.entity.Category;
import com.cydeo.accountingsimplified.entity.Company;
import com.cydeo.accountingsimplified.mapper.MapperUtil;
import com.cydeo.accountingsimplified.repository.CategoryRepository;
import com.cydeo.accountingsimplified.service.CategoryService;
import com.cydeo.accountingsimplified.service.CompanyService;
import com.cydeo.accountingsimplified.service.SecurityService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final SecurityService securityService;
    private final MapperUtil mapperUtil;



    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               SecurityService securityService,
                               MapperUtil mapperUtil) {
        this.categoryRepository = categoryRepository;
        this.securityService = securityService;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public CategoryDto findCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).get();
        return mapperUtil.convert(category, new CategoryDto());
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        Company company = mapperUtil.convert(securityService.getLoggedInUser().getCompany(), new Company());
        return categoryRepository
                .findAllByCompany(company)
                .stream()
                .map(each -> mapperUtil.convert(each, new CategoryDto()))
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
        categoryRepository.save(category);
    }


}
