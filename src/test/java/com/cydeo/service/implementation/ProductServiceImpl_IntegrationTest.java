package com.cydeo.service.implementation;

import com.cydeo.dto.CategoryDto;
import com.cydeo.dto.ProductDto;
import com.cydeo.entity.Product;
import com.cydeo.enums.ProductUnit;
import com.cydeo.repository.ProductRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductServiceImpl_IntegrationTest {

    @Autowired
    ProductServiceImpl productService;

    @Autowired
    ProductRepository productRepository;

    @Test
    @Transactional
    void findProductById() {
        ProductDto productDto = productService.findProductById(1L);
        assertNotNull(productDto);
    }

    @Test
    @Transactional
    @Order(1)
    void findAllProductsWithCategoryId() {
        List<ProductDto> dtos = productService.findAllProductsWithCategoryId(1L);
        assertNotNull(dtos);
    }

    @Test
    void getAllProducts() {
    }

    @Test
    @WithMockUser(username = "manager@bluetech.com", password = "Abc1", roles = "MANAGER")
    void save() {
        ProductDto productDto = getDto();

        ProductDto actualDto = productService.save(productDto);
        System.out.println(productService.save(productDto));
        assertThat(actualDto).usingRecursiveComparison()
                        .ignoringFields("id")
                                .isEqualTo(productDto);
      //  assertEquals(productDto, productService.save(productDto));
    }

    @Test
    @Transactional
   // @WithMockUser(username = "manager@bluetech.com", password = "Abc1", roles = "MANAGER")
    void update() {
        ProductDto productDto = productService.findProductById(1L);
        productDto.setName("update");
        ProductDto actual = productService.update(productDto.getId(), productDto);
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("category")
                .isEqualTo(productDto);
       // assertEquals(productDto, actual);
    }

    @Test
    @Transactional
//    @Rollback(value = false)
    @Commit
//    @WithMockUser(username = "manager@bluetech.com", password = "Abc1", roles = "MANAGER")
    void delete() {
        productService.delete(8L);
        Product actual = productRepository.findById(8L)
                .orElseThrow( ()-> new NoSuchElementException("product not found"));
        assertTrue(actual.getIsDeleted());
    }

    @Test
    void isProductNameExist() {
    }

    private ProductDto getDto(){
//        CompanyDto companyDto = new CompanyDto();
//        companyDto.setId(1L);
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        return new ProductDto(null, "test", 0, 3, ProductUnit.KG, categoryDto);
    }
}