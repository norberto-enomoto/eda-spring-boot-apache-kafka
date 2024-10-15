package com.mycompany.ecommerce.customer.repository;

import com.mycompany.ecommerce.customer.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}