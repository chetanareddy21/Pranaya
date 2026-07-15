package com.ecom.pranaya.config;

import com.ecom.pranaya.model.Cart;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.SessionScope;

@Configuration
public class CartConfig {

    @Bean
    @SessionScope // This binds the lifecycle of this object to the user's browser session
    public Cart shoppingCart() {
        return new Cart();
    }
}