package com.example.demo.controllers;

import java.util.List;

import com.example.demo.Service.OrderService;
import com.example.demo.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;

@Slf4j
@RestController
@RequestMapping("/api/order")
public class OrderController {
	
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrderService orderService;
	
	
	@PostMapping("/submit/{username}")
	public ResponseEntity<UserOrder> submit(@PathVariable String username) {

		log.info("Post /api/order/submit/"+username);

		User user = userService.findByUsername(username);
		if(user == null) {
			log.error("Order submit failed as username ("+user+") was not found");
			return ResponseEntity.notFound().build();
		}

		UserOrder order = UserOrder.createFromCart(user.getCart());
		UserOrder userOrder = orderService.save(order);

		if (userOrder!=null){
			log.debug("=>Order was successfully saved into the Database ");
		}
		else{
			log.error("=>Order creating failed ");
		}


		ResponseEntity<UserOrder> response = ResponseEntity.ok(userOrder);


		//log.debug("=>Order submit response.Header - return  "+ response);
		log.debug("=>Order submit response.getStatusCodeValue - return  "+ response.getStatusCodeValue());

		return response;
	}



	
	@GetMapping("/history/{username}")
	public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {

		log.info("Get /api/order/history/"+username);


		User user = userService.findByUsername(username);
		if(user == null) {

			log.error("=> User not found ");
			return ResponseEntity.notFound().build();
		}

		log.debug("=> getOrdersForUser return  "+ user);

		return ResponseEntity.ok(orderService.findByUser(user));
	}
}
