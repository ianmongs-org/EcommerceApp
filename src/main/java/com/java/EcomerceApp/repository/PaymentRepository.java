package com.java.EcomerceApp.repository;

import com.java.EcomerceApp.model.Order;
import com.java.EcomerceApp.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByOrder(Order order);
}
