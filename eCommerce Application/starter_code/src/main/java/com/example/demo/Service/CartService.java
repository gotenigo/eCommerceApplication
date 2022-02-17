package com.example.demo.Service;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.repositories.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Service
public class CartService {


    @Autowired
    private CartRepository cartRepository;




    public Cart save(Cart cart){
        return cartRepository.save(cart);
    }









}
