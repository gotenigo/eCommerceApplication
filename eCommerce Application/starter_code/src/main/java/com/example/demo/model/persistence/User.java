package com.example.demo.model.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.security.SecureRandom;
import java.util.Arrays;


@Slf4j
@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty
	private long id;

	@Column(nullable = false, unique = true)
	@JsonProperty
	private String username;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(nullable = false)
	private String password;


	/*@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(nullable = false)
	private byte[] salt;*/


	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "cart_id", referencedColumnName = "id")
	@JsonIgnore
	private Cart cart;



	public User(){
		this.cart=new Cart();
	}

	public User(String username, String password) {
		this.username=username;
		this.password=password;
	}



	/*public byte[] getSalt() {
		return salt;
	}*/


	public String getPassword(){
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}



	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}


	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", username='" + username + '\'' +
				", password='" + password + '\'' +
				", cart=" + cart.getId() +
				'}';
	}
}