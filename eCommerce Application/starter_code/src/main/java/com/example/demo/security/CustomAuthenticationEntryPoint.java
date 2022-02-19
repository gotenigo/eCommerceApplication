package com.example.demo.security;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/******************************************
 *
 *
 *  Please note, Security layer comes before anything in the controllers and @ControllerAdvice.
 *  Hence @ControllerAdvice isn't an option since UsernameNotFoundException which is a subclass of AuthenticationException
 *  is thrown during authenticaton, making your exception handlers in @ControllerAdvice unreachable.
 *
 *
 * You can only use @ControllerAdvice and ResponseEntityExceptionHandler if you are throwing UsernameNotFoundException
 * inside controller or any others beans referenced from the controllers.
 *
 *
 *
 * You have to mplement AuthenticationFailureHandler and use it with AuthenticationFilter that you are using for your security configuration.
 * Spring boot security comes with about 4 handler interfaces for security related issues
 *
 * 1) AccessDeniedHandler - this handles issues like when a user not having required roles.
 * 2) AuthenticationEntryPoint - this handles issues like when a user tries to access a resource without appropriate authentication elements.
 *
 * 3) AuthenticationFailureHandler - this handles issues like when a user is not found(i.e. UsernameNotFoundException) or other exceptions thrown inside authentication provider. In fact, this handles other authentication exceptions that are not handled by AccessDeniedException and AuthenticationEntryPoint.
 *
 * 4) AuthenticationSuccessHandler - this helps to do stuff like redirection after a user is successfully authenticated.
 ***************************************/


@ControllerAdvice
public class CustomAuthenticationEntryPoint  implements AuthenticationEntryPoint {



    @ExceptionHandler (value = {AccessDeniedException.class})
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AccessDeniedException accessDeniedException) throws IOException {
        // 403
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Authorization Failed : " + accessDeniedException.getMessage());
    }

    @ExceptionHandler (value = {Exception.class})
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         Exception exception) throws IOException {
        // 500
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error : " + exception.getMessage());
    }


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {

        // 401
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication Failed");
    }


}
