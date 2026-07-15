package com.ecom.pranaya.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<CartItem> items = new ArrayList<>();

    public List<CartItem> getItems() { return items; }

    public void addItem(SareeProduct product, int quantity) {
        // Check if product is already in the cart
        for (CartItem item : items) {
            if (item.getProduct().getFolderName().equalsIgnoreCase(product.getFolderName())) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        // Otherwise, add it fresh
        items.add(new CartItem(product, quantity));
    }

    public double getTotalPrice() {
        return items.stream().mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity()).sum();
    }

    public int getTotalQuantity() {
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }

    // Nested Helper Class for individual item entries
    public static class CartItem {
        private SareeProduct product;
        private int quantity;

        public CartItem(SareeProduct product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }
        public SareeProduct getProduct() { return product; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }
}