package com.example.demo.controllers;

import java.util.Optional;
import java.util.stream.IntStream;

import com.example.demo.Service.CartService;
import com.example.demo.Service.ItemService;
import com.example.demo.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
	private UserService userService;



	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private ItemService itemService;




	@PostMapping("/addToCart")
	public ResponseEntity<Cart> addToCart(@RequestBody ModifyCartRequest request) {

		log.info("POST /api/cart/addToCart "+ request);


		User user = userService.findByUserName(request.getUsername());


		if(user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Optional<Item> item = itemService.findById(request.getItemId());


		if(!item.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Cart cart = user.getCart(); // a Cart is a list of item



		IntStream.range(0, request.getQuantity())
			.forEach(i -> cart.addItem(item.get())); // we load item into the Cart (into the List)

		cart.setUser(user); // cart needs to be re-set  for persistence

		Cart cVar = cartService.save(cart);  // we save into the database

		log.debug("=> addToCart   return "+ cVar);

		return ResponseEntity.ok(cVar);
	}




	
	@PostMapping("/removeFromCart")
	public ResponseEntity<Cart> removeFromCart(@RequestBody ModifyCartRequest request) {

		log.debug("POST /api/cart/removeFromCart "+ request);


		User user = userService.findByUserName(request.getUsername());
		if(user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Optional<Item> item = itemService.findById(request.getItemId());
		if(!item.isPresent()) {
			log.error("removeFromCart failed as Item was not found ");

			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Cart cart = user.getCart();
		IntStream.range(0, request.getQuantity())
			.forEach(i -> cart.removeItem(item.get()));
		Cart cVar = cartService.save(cart);

		log.debug("=> removeFromCart return  "+ cVar);

		return ResponseEntity.ok(cVar);
	}




		
}
