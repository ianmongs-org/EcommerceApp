package com.java.EcomerceApp.repository;

import com.java.EcomerceApp.model.Address;
import com.java.EcomerceApp.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByEmailAndAddressAndOrderDate(String email, Address address, LocalDate now);
}
