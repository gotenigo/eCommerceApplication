package com.example.demo.Service;


import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class ItemService {

    @Autowired
    ItemRepository itemRepository;



    public Optional<Item>  findById(Long id){

        return itemRepository.findById(id);
    }


    public List<Item> findAll(){
        return  itemRepository.findAll();
    }


    public List<Item> findByName(String name){
        return  itemRepository.findByName(name);
    }






}
