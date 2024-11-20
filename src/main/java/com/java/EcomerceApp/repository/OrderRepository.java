package com.java.EcomerceApp.repository;

import com.java.EcomerceApp.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
