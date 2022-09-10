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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final MapperUtil mapperUtil;
    private UserPrincipal userPrincipal;

    public CategoryServiceImpl(UserRepository userRepository, CategoryRepository categoryRepository, MapperUtil mapperUtil) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public CategoryDto findCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).get();
        return mapperUtil.convert(category, new CategoryDto());
    }

    @Override
    public List<CategoryDto> getAllCategories() throws Exception {
        Company company = getCurrentUser().getCompany();
        return categoryRepository.findAllByCompany(company)
                .stream()
                .map(each -> mapperUtil.convert(each, new CategoryDto()))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto create(CategoryDto categoryDto) throws Exception {
        Category category = mapperUtil.convert(categoryDto, new Category());
        category.setCompany(getCurrentUser().getCompany());
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

    public User getCurrentUser() throws Exception {
        userPrincipal = (UserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findUserById(userPrincipal.getId());
    }

}
