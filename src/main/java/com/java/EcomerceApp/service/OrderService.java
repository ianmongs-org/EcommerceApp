package com.java.EcomerceApp.service;


import com.java.EcomerceApp.dto.OrderDTO;
import com.java.EcomerceApp.dto.OrderRequestDTO;

public interface OrderService {
    OrderDTO placeOrder(String emailId, OrderRequestDTO orderDTO);

}
