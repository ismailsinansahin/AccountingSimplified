package com.cydeo.accountingsimplified.repository;

import com.cydeo.accountingsimplified.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findAllByYearBetweenAndCompanyId(LocalDate start, LocalDate end, Long id);
}