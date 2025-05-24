package com.java.EcomerceApp.controller;

import com.java.EcomerceApp.dto.OrderDTO;
import com.java.EcomerceApp.dto.OrderRequestDTO;
import com.java.EcomerceApp.dto.OrderResponse;
import com.java.EcomerceApp.security.utils.AuthUtil;
import com.java.EcomerceApp.service.orders.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/order")
@Tag(name = "Order", description = "Order management APIs")
public class OrderController {
    private final OrderService orderService;
    private final AuthUtil authUtil;

    public OrderController(OrderService orderService, AuthUtil authUtil){
        this.orderService = orderService;
        this.authUtil = authUtil;
    }

    @PostMapping("/users/payments/{paymentMethod}")
    @Operation(summary = "Place an order", description = "Place an order with the specified payment method")
    public ResponseEntity<OrderDTO> placeOrder(@PathVariable String paymentMethod, @RequestBody OrderRequestDTO orderRequestDTO) {
        String emailId = authUtil.loggedInUserEmail();
        OrderDTO orderDTO = orderService.placeOrder(emailId, orderRequestDTO);
        return new ResponseEntity<>(orderDTO, HttpStatus.CREATED);
    }
    
    @GetMapping("/users/history")
    @Operation(summary = "Get order history", description = "Retrieve the order history for the logged-in user")
    public ResponseEntity<List<OrderDTO>> getOrderHistory() {
        String emailId = authUtil.loggedInUserEmail();
        List<OrderDTO> orderHistory = orderService.getOrderHistory(emailId);
        return new ResponseEntity<>(orderHistory, HttpStatus.OK);
    }
    
    @GetMapping("/users/{orderId}")
    @Operation(
        summary = "Get order details", 
        description = "Retrieve the details of a specific order",
        responses = @ApiResponse(
            responseCode = "200",
            description = "Order details retrieved successfully",
            content = @Content(schema = @Schema(implementation = OrderDTO.class))
        )
    )
    public ResponseEntity<OrderDTO> getOrderDetails(@PathVariable Long orderId) {
        String emailId = authUtil.loggedInUserEmail();
        OrderDTO orderDTO = orderService.getOrderDetails(orderId, emailId);
        return new ResponseEntity<>(orderDTO, HttpStatus.OK);
    }
    
    @PutMapping("/admin/{orderId}/status/{status}")
    @Operation(summary = "Update order status", description = "Update the status of an order (Admin only)")
    public ResponseEntity<OrderDTO> updateOrderStatus(
            @PathVariable Long orderId,
            @PathVariable String status) {
        OrderDTO orderDTO = orderService.updateOrderStatus(orderId, status);
        return new ResponseEntity<>(orderDTO, HttpStatus.OK);
    }
    
    @DeleteMapping("/users/{orderId}/cancel")
    @Operation(summary = "Cancel order", description = "Cancel an existing order")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId) {
        String emailId = authUtil.loggedInUserEmail();
        String result = orderService.cancelOrder(orderId, emailId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    @GetMapping("/admin/all")
    @Operation(summary = "Get all orders", description = "Retrieve all orders with pagination (Admin only)")
    public ResponseEntity<OrderResponse> getAllOrders(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "orderDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder) {
        OrderResponse orderResponse = orderService.getAllOrders(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }
}
