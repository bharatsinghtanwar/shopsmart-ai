package com.shopsmart.backend.service;

import com.shopsmart.backend.exception.ResourceNotFoundException;
import com.shopsmart.backend.entity.Cart;
import com.shopsmart.backend.entity.CartItem;
import com.shopsmart.backend.entity.Product;
import com.shopsmart.backend.repository.CartItemRepository;
import com.shopsmart.backend.repository.CartRepository;
import com.shopsmart.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setItems(new ArrayList<>());
            return cartRepository.save(cart);
        });
    }

    @Transactional
    public Cart addToCart(Long userId, Long productId, Integer quantity) {
        Cart cart = getCartByUserId(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product with ID " + productId + " not found"));

        if (cart.getItems() == null) {
            cart.setItems(new ArrayList<>());
        }

        CartItem cartItem = CartItem.builder()
                .cart(cart)
                .productId(productId)
                .quantity(quantity)
                .price(product.getPrice() * quantity)
                .build();

        cart.getItems().add(cartItem);
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart clearCart(Long userId) {
        Cart cart = getCartByUserId(userId);
        if (cart.getItems() != null) {
            cart.getItems().clear();
        }
        return cartRepository.save(cart);
    }

    public List<CartItem> getItems(Long userId) {
        Cart cart = getCartByUserId(userId);
        return cart.getItems() != null ? cart.getItems() : new ArrayList<>();
    }
}