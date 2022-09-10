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
import com.cydeo.accountingsimplified.service.ProductService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final MapperUtil mapperUtil;
    private UserPrincipal userPrincipal;

    public ProductServiceImpl(ProductRepository productRepository, UserRepository userRepository,
                              CategoryRepository categoryRepository, MapperUtil mapperUtil) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public ProductDto findProductById(Long productId) {
        Product product = productRepository.findById(productId).get();
        return mapperUtil.convert(product, new ProductDto());
    }

    @Override
    public List<ProductDto> getAllProducts() throws Exception {
        Company company = getCurrentUser().getCompany();
        return productRepository.findAllByCategoryCompany(company)
                .stream()
                .map(each -> mapperUtil.convert(each, new ProductDto()))
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto create(ProductDto productDto) {
        Product product = mapperUtil.convert(productDto, new Product());
        Category category = mapperUtil.convert(productDto.getCategory(), new Category());
        product.setCategory(category);
        product.setName(productDto.getName());
        product.setLowLimitAlert(productDto.getLowLimitAlert());
        product.setProductUnit(productDto.getProductUnit());
        productRepository.save(product);
        return mapperUtil.convert(product, productDto);
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
        Company company = getCurrentUser().getCompany();
        return categoryRepository.findAllByCompany(company)
                .stream()
                .map(each -> mapperUtil.convert(each, new CategoryDto()))
                .collect(Collectors.toList());
    }

    public User getCurrentUser() throws Exception {
        userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findUserById(userPrincipal.getId());
    }

}
