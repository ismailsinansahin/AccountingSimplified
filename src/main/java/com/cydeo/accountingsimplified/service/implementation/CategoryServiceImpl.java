package com.cydeo.accountingsimplified.service.implementation;

import com.cydeo.accountingsimplified.dto.CategoryDto;
import com.cydeo.accountingsimplified.entity.Category;
import com.cydeo.accountingsimplified.entity.Company;
import com.cydeo.accountingsimplified.entity.User;
import com.cydeo.accountingsimplified.entity.common.UserPrincipal;
import com.cydeo.accountingsimplified.mapper.MapperUtil;
import com.cydeo.accountingsimplified.repository.CategoryRepository;
import com.cydeo.accountingsimplified.repository.UserRepository;
import com.cydeo.accountingsimplified.service.CategoryService;
import com.cydeo.accountingsimplified.service.CompanyService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final MapperUtil mapperUtil;
    private final CompanyService companyService;

    public CategoryServiceImpl(CategoryRepository categoryRepository, MapperUtil mapperUtil, CompanyService companyService) {
        this.categoryRepository = categoryRepository;
        this.mapperUtil = mapperUtil;
        this.companyService = companyService;
    }

    @Override
    public CategoryDto findCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).get();
        return mapperUtil.convert(category, new CategoryDto());
    }

    @Override
    public List<CategoryDto> getAllCategories() throws Exception {
        Company company = mapperUtil.convert(companyService.getCompanyByLoggedInUser(), new Company());
        return categoryRepository.findAllByCompany(company)
                .stream()
                .map(each -> mapperUtil.convert(each, new CategoryDto()))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto create(CategoryDto categoryDto) throws Exception {
        Category category = mapperUtil.convert(categoryDto, new Category());
        Company company = mapperUtil.convert(companyService.getCompanyByLoggedInUser(), new Company());
        category.setCompany(company);
        categoryRepository.save(category);
        return mapperUtil.convert(category, categoryDto);
    }

    @Override
    public CategoryDto update(Long categoryId, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(categoryId).get();
        category.setDescription(categoryDto.getDescription());
        categoryRepository.save(category);
        return mapperUtil.convert(category, categoryDto);
    }

    @Override
    public void delete(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).get();
        category.setIsDeleted(true);
        categoryRepository.save(category);
    }


}
