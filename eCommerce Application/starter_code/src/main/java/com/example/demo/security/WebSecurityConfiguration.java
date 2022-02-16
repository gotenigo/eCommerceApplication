package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


//Main Class to extend on it allows us to define SpringSecurity rule
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	private UserDetailsServiceImpl userDetailsService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;



    public WebSecurityConfiguration(UserDetailsServiceImpl userDetailsService,
			BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userDetailsService = userDetailsService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}



    @Override
    //After defining the authentication and authorization modules, we need to configure them on the Spring Security filter chain.
    // The WebSecurity class is a custom implementation of the default web security configuration provided by Spring Security.
    // In this class, we have overridden two overloaded methods:
    //
    //configure(HttpSecurity) - Defines public resources. Below, we have set the SIGN_UP_URL endpoint as public.
    // The http.cors() is used to make the Spring Security support the CORS (Cross-Origin Resource Sharing) and CSRF (Cross-Site Request Forgery)
    protected void configure(HttpSecurity http) throws Exception {


        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL).permitAll() // We allow only POST on SIGN_UP_URL (defined in bespoke Class SecurityConstants)
                .anyRequest().authenticated()   // Any request requires authentication
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager())) //Adds a Filter that must be an instance of or extend one of the Filters provided within the Security framework. The method ensures that the ordering of the Filters is automatically taken care of. Here it's UsernamePasswordAuthenticationFilter
                .addFilter(new JWTAuthenticationVerficationFilter(authenticationManager()))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }





    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }




    @Override
    //configure(AuthenticationManagerBuilder) - It declares the BCryptPasswordEncoder as the encoding technique,
    // and loads user-specific data.
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.parentAuthenticationManager(authenticationManagerBean())
            .userDetailsService(userDetailsService) // Define the userService class to use - (implemented by us via Override) the UserService is implemented by us (userDetailsServiceImpl). It pulls users details from the Database
            .passwordEncoder(bCryptPasswordEncoder); // define the password encoder class to use
    }



}
