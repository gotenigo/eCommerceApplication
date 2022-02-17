package com.example.demo.controllers;

import java.util.Optional;
import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;

@Slf4j
@RestController
@RequestMapping("/api/cart")
public class CartController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private ItemRepository itemRepository;
	
	@PostMapping("/addToCart")
	public ResponseEntity<Cart> addToCart(@RequestBody ModifyCartRequest request) {

		log.debug("POST /api/cart/addToCart "+ request);


		User user = userRepository.findByUsername(request.getUsername());
		if(user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Optional<Item> item = itemRepository.findById(request.getItemId());
		if(!item.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Cart cart = user.getCart(); // a Cart is a list of item

		IntStream.range(0, request.getQuantity())
			.forEach(i -> cart.addItem(item.get())); // we load item into the Cart (into the List)

		Cart cVar = cartRepository.save(cart);  // we save into the database

		log.debug("=> addToCart return  "+ cVar);

		return ResponseEntity.ok(cVar);
	}




	
	@PostMapping("/removeFromCart")
	public ResponseEntity<Cart> removeFromCart(@RequestBody ModifyCartRequest request) {

		log.debug("POST /api/cart/removeFromCart "+ request);


		User user = userRepository.findByUsername(request.getUsername());
		if(user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Optional<Item> item = itemRepository.findById(request.getItemId());
		if(!item.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Cart cart = user.getCart();
		IntStream.range(0, request.getQuantity())
			.forEach(i -> cart.removeItem(item.get()));
		Cart cVar = cartRepository.save(cart);

		log.debug("=> removeFromCart return  "+ cVar);

		return ResponseEntity.ok(cVar);
	}




		
}
