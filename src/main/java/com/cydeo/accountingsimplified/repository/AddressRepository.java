package com.cydeo.accountingsimplified.repository;

import com.cydeo.accountingsimplified.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
