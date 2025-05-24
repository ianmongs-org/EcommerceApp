package com.java.EcomerceApp.service.orders;

import com.java.EcomerceApp.dto.OrderDTO;
import com.java.EcomerceApp.dto.OrderRequestDTO;
import com.java.EcomerceApp.dto.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderDTO placeOrder(String email, OrderRequestDTO orderRequestDTO);
    
    List<OrderDTO> getOrderHistory(String email);
    
    OrderDTO getOrderDetails(Long orderId, String email);
    
    OrderDTO updateOrderStatus(Long orderId, String status);
    
    String cancelOrder(Long orderId, String email);
    
    OrderResponse getAllOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
}
