package com.shopsmart.backend.service;

import com.shopsmart.backend.exception.ResourceNotFoundException;
import com.shopsmart.backend.entity.*;
import com.shopsmart.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        List<CartItem> cartItems = cart.getItems();

        if (cartItems == null || cartItems.isEmpty()) {
            throw new ResourceNotFoundException("Cart is empty for user: " + userId);
        }

        // Calculate total amount
        BigDecimal totalAmount = cartItems.stream()
                .map(item -> BigDecimal.valueOf(item.getPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Create Order
        Order order = Order.builder()
                .userId(userId)
                .status(OrderStatus.PENDING)
                .totalAmount(totalAmount)
                .createdAt(LocalDateTime.now())
                .build();

        order = orderRepository.save(order);

        // Create OrderItems & update stock
        Order finalOrder = order;
        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
            Product product = productRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product ID " + cartItem.getProductId() + " not found"));

            // Deduct stock
            product.setQuantity(product.getQuantity() - cartItem.getQuantity());
            productRepository.save(product);

            return OrderItem.builder()
                    .order(finalOrder)
                    .productId(cartItem.getProductId())
                    .quantity(cartItem.getQuantity())
                    .price(BigDecimal.valueOf(cartItem.getPrice()))
                    .build();
        }).collect(Collectors.toList());

        orderItemRepository.saveAll(orderItems);

        // Link order items to order
        order.setItems(orderItems);

        // Clear cart after placing order
        cartService.clearCart(userId);

        return order;
    }

    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order ID " + orderId + " not found"));
    }
}
