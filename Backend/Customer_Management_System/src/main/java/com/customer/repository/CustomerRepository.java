package com.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.customer.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer>{

}
