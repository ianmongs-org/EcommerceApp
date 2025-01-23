package com.java.EcomerceApp.controller;

import com.java.EcomerceApp.dto.OrderDTO;
import com.java.EcomerceApp.dto.OrderRequestDTO;
import com.java.EcomerceApp.security.utils.AuthUtil;
import com.java.EcomerceApp.service.orders.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private AuthUtil authUtil;

    @PostMapping("/placeOrder/users/payments/{paymentMethod}")
    @Operation(summary = "Place an order", description = "Place an order with the specified payment method")
    public ResponseEntity<OrderDTO> placeOrder(@PathVariable String paymentMethod, @RequestBody OrderRequestDTO orderRequestDTO) {
        String emailId = authUtil.loggedInUserEmail();

        OrderDTO orderDTO = orderService.placeOrder(emailId, orderRequestDTO);
        return new ResponseEntity<>(orderDTO, HttpStatus.CREATED);
    }
}
