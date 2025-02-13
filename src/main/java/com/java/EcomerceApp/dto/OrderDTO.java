package com.java.EcomerceApp.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
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
