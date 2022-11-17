package com.cydeo.accountingsimplified.repository;

import com.cydeo.accountingsimplified.entity.Company;
import com.cydeo.accountingsimplified.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserById(Long id);
    User findByUsername(String username);
    List<User> findAllByRole_Description(String roleDescription);
    List<User> findAllByCompany_Title(String companyName);
    List<User> findAllByCompany_TitleAndRole_Description(String companyTitle, String role);
}
