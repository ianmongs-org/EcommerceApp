package com.java.EcomerceApp.service.orders;

import com.java.EcomerceApp.dto.OrderDTO;
import com.java.EcomerceApp.dto.OrderItemDTO;
import com.java.EcomerceApp.dto.OrderRequestDTO;
import com.java.EcomerceApp.exception.ResourceNotFoundException;
import com.java.EcomerceApp.model.*;
import com.java.EcomerceApp.repository.*;
import com.java.EcomerceApp.service.cart.CartServiceImpl;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CartServiceImpl cartServiceImpl;

    @Override
    @Transactional
    public OrderDTO placeOrder(String email, OrderRequestDTO orderRequestDTO) {
        Cart cart = getCartByEmail(email);
        if (cart == null || cart.getCartItems().isEmpty()) {
            throw new ResourceNotFoundException("Cart is empty for email: " + email);
        }
        Address address = getAddressById(orderRequestDTO.getAddressId());
        // Check if an order with the same details already exists
        if (orderRepository.existsByEmailAndAddressAndOrderDate(email, address, LocalDate.now())) {
            throw new ResourceNotFoundException("Order with the same details already exists");
        }
        Order order = createOrder(email, address, cart.getTotalPrice());
        List<OrderItem> orderItems = createOrderItems(cart, order);
        Payment payment = createPayment(orderRequestDTO, order, cart.getTotalPrice());
        order.setPayment(payment);
        cartServiceImpl.clearCart(cart); // Pass the cart parameter here
        return mapToOrderDTO(order, orderItems);
    }
    private Cart getCartByEmail(String email) {
        return cartRepository.findCartByUserEmail(email);
    }

    private Address getAddressById(Long addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));
    }

    private Order createOrder(String email, Address address, Double totalAmount) {
        Order order = new Order();
        order.setEmail(email);
        order.setAddress(address);
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(totalAmount);
        order.setOrderStatus("PLACED");
        return orderRepository.save(order);
    }

    private List<OrderItem> createOrderItems(Cart cart, Order order) {
        return cart.getCartItems().stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setProductPrice(cartItem.getProductPrice());
            orderItem.setDiscount(cartItem.getProduct().getDiscount());
            return orderItemRepository.save(orderItem);
        }).toList();
    }

    private Payment createPayment(OrderRequestDTO orderRequestDTO, Order order, Double amount) {
        Payment payment = new Payment(orderRequestDTO.getPgPaymentId(), orderRequestDTO.getPaymentMethod(),
                orderRequestDTO.getPgPaymentStatus(), orderRequestDTO.getPgResponseMessage(), orderRequestDTO.getPgName());
        payment.setOrder(order);
        payment.setAmount(amount);
        return paymentRepository.save(payment);
    }
    private OrderDTO mapToOrderDTO(Order order, List<OrderItem> orderItems) {
        OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);
        orderDTO.setOrderItems(orderItems.stream().map(orderItem -> {
            OrderItemDTO orderItemDTO = new OrderItemDTO();
            orderItemDTO.setOrderItemId(orderItem.getOrderItemId());
            orderItemDTO.setQuantity(orderItem.getQuantity());
            orderItemDTO.setProductPrice(orderItem.getProductPrice());
            orderItemDTO.setDiscount(orderItem.getDiscount());
            orderItemDTO.setProductId(orderItem.getProduct().getProductId());
            orderItemDTO.setProductName(orderItem.getProduct().getProductName());
            orderItemDTO.setProductImage(orderItem.getProduct().getImage());
            return orderItemDTO;
        }).collect(Collectors.toList()));
        return orderDTO;
    }
}