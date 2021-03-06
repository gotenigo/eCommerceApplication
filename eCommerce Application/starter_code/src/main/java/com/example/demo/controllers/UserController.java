package com.example.demo.controllers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

import com.example.demo.Service.CartService;
import com.example.demo.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;


@Slf4j
@RestController
@Transactional
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	private UserService userService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	//@Autowired
	//private CartService CartService;





	public UserController(UserService userService, com.example.demo.Service.CartService cartService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userService = userService;
		///CartService = cartService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}


	@GetMapping("/all")
	public ResponseEntity<  List<User> > findAll() {

		log.info("Get /api/user/all ");

		List<User> userList = userService.findAll();

		log.debug("=> findAll return  "+ userList);

		return ResponseEntity.of(  Optional.of(userList) );

	}


	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {

		log.info("Get /api/user/id/"+id);

		Optional<User> user = userService.findByUserById(id);

		log.debug("=> findById return  "+ user);

		return ResponseEntity.of( user );

	}



	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {

		log.info("Get /api/user/"+ username);

		User user = userService.findByUserName(username);

		log.debug("=> findByUserName return  "+ user.getId());

		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}






	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {

		log.info("POST /api/user/create "+ createUserRequest);

		User user = new User();
		user.setUsername(createUserRequest.getUsername());
		Cart cart = new Cart();
		//CartService.save(cart);  // ??????This should not be needed with hibernate ???
		user.setCart(cart);

		String password = createUserRequest.getPassword();
		String username = createUserRequest.getUsername();


		if(password.length()<7 || !password.equals(createUserRequest.getConfirmPassword())){

			log.error("=> issue with User "+username+" -  Password is not compliant with Internal Policy. Either password length is less than 7 or Password & Confirmation password do not match");

			return ResponseEntity.badRequest().build();
		}


		//bcrypt has salts built into the generated hashes to prevent rainbow table attacks. bcrypt = the hash and the salt concatenated.
		String HashedPassword =bCryptPasswordEncoder.encode(password); // Use password-hashing function "bcrypt" to Hash the password

		user.setPassword(HashedPassword);
		//log.debug("HashedPassword="+HashedPassword);


		User vUser = userService.save(user);
		if(vUser==null){
			log.warn("it appears user "+user.getUsername()+" already exists!");
		}

		ResponseEntity<User> response = ResponseEntity.ok(vUser);

		log.debug("=> createUser return  "+ vUser);
		log.debug("=> createUser return  "+ response);


		return response;
	}















}
