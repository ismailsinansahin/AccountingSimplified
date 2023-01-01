package com.cydeo.accountingsimplified.service.implementation;

import com.cydeo.accountingsimplified.AccountingSimplifiedApplication;
import com.cydeo.accountingsimplified.dto.CategoryDto;
import com.cydeo.accountingsimplified.dto.ProductDto;
import com.cydeo.accountingsimplified.entity.BaseEntity;
import com.cydeo.accountingsimplified.enums.ProductUnit;
import com.cydeo.accountingsimplified.repository.ProductRepository;
import com.cydeo.accountingsimplified.service.ProductService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = AccountingSimplifiedApplication.class)
class ProductServiceImplTest extends AbstractIntegrationTest{


    @Autowired
    ProductRepository repository;

    @Autowired
    ProductService productService;



    @Test
    @Transactional
    void save() {


        var p = ProductDto.builder()
                .id(55l)
                .name("name")
                .lowLimitAlert(2)
                .productUnit(ProductUnit.GALLON)
                .category(new CategoryDto(1l,"admin",null,false))
                .build();




        productService.save(p);

        var data = repository.findById(55l);
        Assertions.assertThat(data).isNotNull();
    }
}