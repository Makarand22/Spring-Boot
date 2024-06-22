package com.springBatch.repository;

import com.springBatch.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
