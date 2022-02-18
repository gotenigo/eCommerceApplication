package com.example.demo.Service;


import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;


    public UserOrder save(UserOrder order){

        return orderRepository.save(order);

    }



    public List<UserOrder> findByUser(User user){

        return orderRepository.findByUser(user);

    }




}
