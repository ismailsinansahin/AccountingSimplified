package com.cydeo.repository;

import com.cydeo.entity.Company;
import com.cydeo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByCategoryCompany(Company company);

    List<Product> findByCategoryId(Long categoryId);

    Product findByNameAndCategoryCompany(String name, Company actualCompany);
}
