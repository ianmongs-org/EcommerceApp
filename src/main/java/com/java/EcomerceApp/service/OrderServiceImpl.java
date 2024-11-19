package com.java.EcomerceApp.service;

import com.java.EcomerceApp.dto.OrderDTO;
import com.java.EcomerceApp.dto.OrderRequestDTO;
import com.java.EcomerceApp.exception.ResourceNotFoundException;
import com.java.EcomerceApp.model.*;
import com.java.EcomerceApp.repository.*;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    @Transactional
    public OrderDTO placeOrder(String email, OrderRequestDTO orderRequestDTO) {
        // Retrieve the cart for the given email ID
        Cart cart = cartRepository.findCartByUserEmail(email);
        if(cart == null) {
            throw new ResourceNotFoundException("Cart not found for email: " + email);
        }

        // Validate the address
        Address address = addressRepository.findById(orderRequestDTO.getAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        // Create and save the order
        Order order = new Order();
        order.setEmail(email);
        order.setAddress(address);
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(cart.getTotalPrice());
        order.setOrderStatus("PLACED");

        // Create and save the order items
        cart.getCartItems().forEach(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setProductPrice(cartItem.getProductPrice());
            orderItemRepository.save(orderItem);
        });

        // Create and save the payment
        Payment payment = new Payment(orderRequestDTO.getPgPaymentId(), orderRequestDTO.getPaymentMethod(), orderRequestDTO.getPgPaymentStatus(), orderRequestDTO.getPgResponseMessage(), orderRequestDTO.getPgName());
        payment.setOrder(order);
        payment = paymentRepository.save(payment);
        order.setPayment(payment);

        Order savedOrder = orderRepository.save(order);


        // Map the order to OrderDTO and return it
        return modelMapper.map(order, OrderDTO.class);
    }
}
