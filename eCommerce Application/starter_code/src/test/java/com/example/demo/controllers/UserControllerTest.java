package com.example.demo.controllers;

import com.example.demo.SareetaApplication;
import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/*@Transactional(propagation = Propagation.NOT_SUPPORTED)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // to reinit  the state of an embedded database. This annotation tells Spring that the ApplicationContext associated with a test is dirty and should therefore be closed and removed from the context cache.
@SpringBootTest(classes = SareetaApplication.class)*/
public class UserControllerTest {


    private UserController userController;
    private UserRepository userRepository= mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);




    @Before
    public void setUP() throws NoSuchFieldException, IllegalAccessException {

        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder",bCryptPasswordEncoder );

    }



    @Test
    public void create_user_happy_path() {

        //stubbing
        when(bCryptPasswordEncoder.encode(isA(String.class))).thenReturn("thisIsHashed"); //the password is salted, so we need to  check the String

        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("test");
        userRequest.setPassword("testPassword");
        userRequest.setConfirmPassword("testPassword");

        ResponseEntity<User> response = userController.createUser(userRequest);
        assertNotNull(response);
        assertEquals("# User Created #",200, response.getStatusCodeValue());

        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("test", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());

    }



    @Test
    public void findAll() {

        User user = new User();
        Cart cart = new Cart();
        user.setUsername("gg");
        user.setPassword("password");
        user.setCart(cart);
        List<User> userList = Arrays.asList(user);

        //stubbing
        when(userRepository.findAll()).thenReturn(userList); //the password is salted, so we need to  check the String
        //System.out.println("userController.findAll() ="+userController.findAll());
        ResponseEntity<List<User>> response = userController.findAll();

        assertNotNull(response);
        assertEquals("# Find ALl User #",200, response.getStatusCodeValue());

    }




    @Test
    public void findById() {

        User user = new User();
        Cart cart = new Cart();
        user.setUsername("gg");
        user.setPassword("password");
        user.setCart(cart);

        //stubbing
        when(userRepository.findById(1L)).thenReturn(Optional.of(user)); //the password is salted, so we need to  check the String
        //System.out.println("userController.findAll() ="+userController.findAll());
        ResponseEntity<User> response = userController.findById(1L);

        assertNotNull(response);
        assertEquals("# Find ALl User #",200, response.getStatusCodeValue());

    }






}
