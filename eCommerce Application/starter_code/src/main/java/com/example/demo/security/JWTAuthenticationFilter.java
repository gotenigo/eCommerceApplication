package com.example.demo.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // "5": "org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter",

import com.auth0.jwt.JWT;
import com.example.demo.model.persistence.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

/*******
 *
 *  This filter is added via the config of WebSecurityConfigurerAdapter under Configure method.
 *   It extends from  UsernamePasswordAuthenticationFilter that is the Spring Security level 4th of the filter chain proxy which is the "Authentication processing mechanisms"
 *
 * This custom class is responsible for the authentication process.
 * This class extends the UsernamePasswordAuthenticationFilter class,
 * which is available under both spring-security-web and spring-boot-starter-web dependency.
 * The Base class parses the user credentials (username and a password).
 *
 * All the method available here :
 * https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/web/authentication/UsernamePasswordAuthenticationFilter.html
 *
 * We have overridden the following two methods:
 *
 * 1) attemptAuthentication() - It performs actual authentication by parsing (also called filtering) the user credentials
 *
 * 2) successfulAuthentication() - This method is originally present in the parent of the Base class.
 *                                          After overriding, this method will be called after a user logs in successfully.
 *                                          Below, it is generating a String token (JWT) for this user.
 *
 *
 **********************/
@Slf4j
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
	private AuthenticationManager authenticationManager;


    //Constructor
    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        super.setAuthenticationFailureHandler(new CustomAuthenticationFailureHandler());
        super.setAuthenticationSuccessHandler(new CustomAuthenticationSuccessHandler());
        super.setAuthenticationSuccessHandler(new CustomAuthenticationSuccessHandler());
    }



    @Override
    // attemptAuthentication() - It performs actual authentication by parsing (also called filtering) the user credentials
    //Returns:a fully authenticated object including credentials
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
    	try {
    		User credentials = new ObjectMapper()
                    .readValue(req.getInputStream(), User.class);
    		
    		return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getUsername(),
                            credentials.getPassword(),
                            new ArrayList<>()));
    	} catch (IOException e) {

            log.error("Problem with Login, user trying to login with invalid input");
    		throw new RuntimeException(e);

            //throw new Exception(e);
            //return null;
    	}
    }




    
    @Override
    //This method is originally present in the parent of the Base class. After overriding,
    // this method will be called after a user logs in successfully.
    // Below, it is generating a String token (JWT) for this user.
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        String token = JWT.create()
                .withSubject(((org.springframework.security.core.userdetails.User) auth.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .sign(HMAC512(SecurityConstants.SECRET.getBytes()));
        res.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
    }






    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse res, AuthenticationException failed) throws IOException, ServletException {
        res.addHeader("Access-Control-Allow-Origin", "*");
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode message = mapper.createObjectNode();
        message.put("success", false);
        message.put("message", "Invalid credentials");
        log.error("Invalid credential !");
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(message);

        PrintWriter out = res.getWriter();
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        out.print(json);
        out.flush();
    }







}
