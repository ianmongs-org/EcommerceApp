package com.java.EcomerceApp.repository;

import com.java.EcomerceApp.model.Address;
import com.java.EcomerceApp.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByEmailAndAddressAndOrderDate(String email, Address address, LocalDate orderDate);
    List<Order> findByEmailOrderByOrderDateDesc(String email);
    Page<Order> findAll(Pageable pageable);
}
