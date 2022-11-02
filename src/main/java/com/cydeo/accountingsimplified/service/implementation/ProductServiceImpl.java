package com.cydeo.accountingsimplified.service.implementation;

import com.cydeo.accountingsimplified.dto.CategoryDto;
import com.cydeo.accountingsimplified.dto.ProductDto;
import com.cydeo.accountingsimplified.entity.Category;
import com.cydeo.accountingsimplified.entity.Company;
import com.cydeo.accountingsimplified.entity.Product;
import com.cydeo.accountingsimplified.entity.User;
import com.cydeo.accountingsimplified.entity.common.UserPrincipal;
import com.cydeo.accountingsimplified.mapper.MapperUtil;
import com.cydeo.accountingsimplified.repository.CategoryRepository;
import com.cydeo.accountingsimplified.repository.ProductRepository;
import com.cydeo.accountingsimplified.repository.UserRepository;
import com.cydeo.accountingsimplified.service.CompanyService;
import com.cydeo.accountingsimplified.service.ProductService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CompanyService companyService;
    private final MapperUtil mapperUtil;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository,
                              CompanyService companyService, MapperUtil mapperUtil) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.companyService = companyService;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public ProductDto findProductById(Long productId) {
        Product product = productRepository.findById(productId).get();
        return mapperUtil.convert(product, new ProductDto());
    }

    @Override
    public List<ProductDto> getAllProducts() throws Exception {
        Company company = mapperUtil.convert(companyService.getCompanyByLoggedInUser(), new Company());
        return productRepository.findAllByCategoryCompany(company)
                .stream()
                .map(each -> mapperUtil.convert(each, new ProductDto()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> getProductsOfCompany() {
        Company company = mapperUtil.convert(companyService.getCompanyByLoggedInUser(), new Company());
        return productRepository.findAllByCategoryCompany(company)
                .stream()
                .map(each -> mapperUtil.convert(each, new ProductDto()))
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto save(ProductDto productDto) {
        Product product = mapperUtil.convert(productDto, new Product());
        return mapperUtil.convert(productRepository.save(product), new ProductDto());
    }

    @Override
    public ProductDto update(Long productId, ProductDto productDto) {
        Product product = productRepository.findById(productId).get();
        Category category = mapperUtil.convert(productDto.getCategory(), new Category());
        product.setCategory(category);
        product.setName(productDto.getName());
        product.setLowLimitAlert(productDto.getLowLimitAlert());
        product.setProductUnit(productDto.getProductUnit());
        productRepository.save(product);
        return mapperUtil.convert(product, productDto);
    }

    @Override
    public void delete(Long productId) {
        Product product = productRepository.findById(productId).get();
        product.setIsDeleted(true);
        productRepository.save(product);
    }

    @Override
    public List<CategoryDto> getAllCategories() throws Exception {
        Company company = mapperUtil.convert(companyService.getCompanyByLoggedInUser(), new Company());
        return categoryRepository.findAllByCompany(company)
                .stream()
                .map(each -> mapperUtil.convert(each, new CategoryDto()))
                .collect(Collectors.toList());
    }


}
