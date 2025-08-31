package com.shopsmart.backend.controller;

import com.shopsmart.backend.entity.Cart;
import com.shopsmart.backend.entity.CartItem;
import com.shopsmart.backend.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getCart(@PathVariable Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/{userId}/add")
    public ResponseEntity<?> addItem(@PathVariable Long userId,
                                     @RequestParam Long productId,
                                     @RequestParam Integer quantity) {
        Cart cart = cartService.addToCart(userId, productId, quantity);
        return ResponseEntity.ok(cart);
    }

    @GetMapping("/{userId}/items")
    public ResponseEntity<List<CartItem>> getCartItems(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getItems(userId));
    }

    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<?> clearCart(@PathVariable Long userId) {
        Cart cart = cartService.clearCart(userId);
        return ResponseEntity.ok(cart);
    }
}
