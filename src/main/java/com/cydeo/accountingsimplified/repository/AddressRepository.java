package com.cydeo.accountingsimplified.repository;

import com.cydeo.accountingsimplified.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@Repository
@CrossOrigin(origins = "http://localhost:4200/")
public interface AddressRepository extends JpaRepository<Address, Long> {
}
