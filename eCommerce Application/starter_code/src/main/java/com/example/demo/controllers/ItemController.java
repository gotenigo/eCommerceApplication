package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;

import com.example.demo.Service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;


@Slf4j
@RestController
@RequestMapping("/api/item")
public class ItemController {

	@Autowired
	private ItemService itemService;



	
	@GetMapping
	public ResponseEntity<List<Item>> getAllItems() {

		log.debug("Get /api/item ");

		List<Item> itemList= itemService.findAll();

		log.debug("=> getAllItems return  "+ itemList);

		return ResponseEntity.ok(itemList);
	}



	@GetMapping("/{id}")
	public ResponseEntity<Item> getItemById(@PathVariable Long id) {

		log.debug("Get /api/item/id "+id);

		Optional<Item> item = itemService.findById(id);

		log.debug("=> getItemById return  "+ item);
		return ResponseEntity.of(item);
	}


	
	@GetMapping("/name/{name}")
	public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) {

		log.debug("Get /api/item/name/"+name);


		List<Item> items = itemService.findByName(name);

		log.debug("=> getItemsByName return  "+ items);

		return items == null || items.isEmpty() ? ResponseEntity.notFound().build()
				: ResponseEntity.ok(items);
			
	}
	
}
