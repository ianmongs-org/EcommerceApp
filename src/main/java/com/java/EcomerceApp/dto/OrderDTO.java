package com.java.EcomerceApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Long orderId;
    private String email;
    private String orderStatus;
    private List<OrderItemDTO> orderItems;
    private LocalDate orderDate;
    private Double totalAmount;
    private Long addressId;
    private PaymentDTO payment;
}
