package com.cydeo.accountingsimplified.repository;

import com.cydeo.accountingsimplified.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@Repository
@CrossOrigin(origins = "http://localhost:4200/")
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findRoleById(Long id);
    Role findByDescription(String description);
}
