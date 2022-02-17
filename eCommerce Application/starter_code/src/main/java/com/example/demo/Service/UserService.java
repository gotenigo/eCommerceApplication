package com.example.demo.Service;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Transactional
@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;





    public User save(User user){

        return userRepository.save(user);
    }





    public List<User> findAll(){

        return userRepository.findAll();
    }




    public User findByUserName(String username){

        return userRepository.findByUsername(username);
    }




    public Optional<User> findByUserById(Long id){

        return userRepository.findById(id);
    }







}
