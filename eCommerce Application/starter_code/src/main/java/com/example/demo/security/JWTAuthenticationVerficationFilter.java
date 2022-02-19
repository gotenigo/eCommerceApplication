package com.example.demo.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;


/*************************************************
 *
 *  This filter is added via the config of WebSecurityConfigurerAdapter under Configure method
 *
 *   It extends from  BasicAuthenticationFilter that is the Spring Security level 4th of the filter chain proxy which is the "Authentication processing mechanisms"
 *
 *
 *
 ********************************************/
@Component
//This class is responsible for the authorization process. This class extends the BasicAuthenticationFilter class.
// It overrides on method, and defines another custom method.
public class JWTAuthenticationVerficationFilter extends BasicAuthenticationFilter {



    //Constructor
	public JWTAuthenticationVerficationFilter(AuthenticationManager authManager) {
        super(authManager);
    }



	@Override
    //Overridden method - doFilterInternal()- This method is used when we have multiple roles, and a policy for RBAC
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) 
    		throws IOException, ServletException {
        String header = req.getHeader(SecurityConstants.HEADER_STRING);

        if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req); // chain is a FilterChain Object. FilterChain is provided by the servlet container to the developer giving a view into the invocation chain of a filtered request for a resource. Filters use the FilterChain to invoke the next filter in the chain, or if the calling filter is the last filter in the chain, to invoke the resource at the end of the chain.

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res); // chain is a FilterChain Object. FilterChain is provided by the servlet container to the developer giving a view into the invocation chain of a filtered request for a resource. Filters use the FilterChain to invoke the next filter in the chain, or if the calling filter is the last filter in the chain, to invoke the resource at the end of the chain.
    }




    //Custom method - getAuthentication() - It validates the token read from the Authorization header
	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest req) {
		String token = req.getHeader(SecurityConstants.HEADER_STRING);
        if (token != null) {
            String user = JWT.require(HMAC512(SecurityConstants.SECRET.getBytes())).build()
                    .verify(token.replace(SecurityConstants.TOKEN_PREFIX, ""))
                    .getSubject();
            if (user != null) {
                return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
            }
            return null;
        }
        return null;
	}




}
