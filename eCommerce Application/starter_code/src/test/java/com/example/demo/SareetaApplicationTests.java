package com.example.demo;

import com.example.demo.controllers.CartController;
import com.example.demo.controllers.ItemController;
import com.example.demo.controllers.OrderController;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // to reinit  the state of an embedded database. This annotation tells Spring that the ApplicationContext associated with a test is dirty and should therefore be closed and removed from the context cache.
@SpringBootTest(classes = SareetaApplication.class)//(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class SareetaApplicationTests {


	@Autowired
	private UserController userController;

	@Autowired
	private ItemController itemController;

	@Autowired
	private OrderController orderController;

	@Autowired
	private CartController cartController;

	@Autowired
	private MockMvc mvc;

	//@Autowired
	//RestTemplateBuilder restTemplateBuilder;



	@Test
	public void TestCreateUser() {

		//1.check no user exist
		ResponseEntity<List<User>> findALlResponse = userController.findAll();
		assertEquals(0, findALlResponse.getBody().size());

		//2.create a new user
		CreateUserRequest createUserRequest = CreateUser();
		ResponseEntity<User> response = userController.createUser(createUserRequest);

		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());


		User user = response.getBody();
		System.out.println("=>user="+user);
		assertNotNull(user);
		assertEquals(1, user.getId());
		assertEquals("test", user.getUsername());
		assertNotNull(user.getPassword());

		//3.find user by name
		response = userController.findByUserName(user.getUsername());
		assertEquals(user.getUsername(), response.getBody().getUsername());

	}




	@Test
	public void TestBadLogon() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/addToCart")).andExpect(status().isForbidden());
	}



	@Test
	public void TestLogon() throws Exception {

		//1.create a new user
		CreateUserRequest createUserRequest = CreateUser();
		ResponseEntity<User> response = userController.createUser(createUserRequest);

		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());


		String username = createUserRequest.getUsername();
		String password = createUserRequest.getPassword();

		//2. Logon
		String body = "{\"username\":\"" + username + "\", \"password\":\""+ password + "\"}";

		System.out.println("=>body="+body);

		MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/login")
						.content(body))
						.andExpect(status().isOk()).andReturn();


		String token= result.getResponse().getHeader("Authorization");
		System.out.println("=>result.getResponse() 1 ="+result.getResponse().getHeader("Authorization") );


		mvc.perform(MockMvcRequestBuilders.get("/api/user/all")
						.header("Authorization", token))
						.andExpect(status().isOk());

	}




	@Test
	public void TestAddCart() {


		//1.check no user exist
		ResponseEntity<List<User>> findALlResponse = userController.findAll();
		assertEquals(0, findALlResponse.getBody().size());

		//2.create a new user
		CreateUserRequest createUserRequest = CreateUser();
		ResponseEntity<User> response = userController.createUser(createUserRequest);

		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());





	}
















	private  static CreateUserRequest CreateUser(){

		CreateUserRequest userRequest = new CreateUserRequest();
		userRequest.setUsername("test");
		userRequest.setPassword("testPassword");
		userRequest.setConfirmPassword("testPassword");

		return userRequest;
	}



	private  static ModifyCartRequest CartRequest(){

		ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
		modifyCartRequest.setUsername("test");
		modifyCartRequest.setQuantity(2);
		modifyCartRequest.setItemId(1);

		return modifyCartRequest;
	}









}
