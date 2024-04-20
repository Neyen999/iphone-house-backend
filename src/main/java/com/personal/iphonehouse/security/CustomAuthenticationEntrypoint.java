package com.personal.iphonehouse.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

@Order(1)
public class CustomAuthenticationEntrypoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // Set the response status to 403
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // Set the content type to JSON
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        // Write the exception message to the response body
        ObjectMapper objectMapper = new ObjectMapper();
        // Create a custom exception object
//        CustomException customException = new CustomException(authException, HttpStatus.UNAUTHORIZED);
//        customException.setErrorCode(401);
        RuntimeException exception = new RuntimeException("Acces denied code: 401");
        // Convert the custom exception object to a JSON string
        String json = objectMapper.writeValueAsString(exception);
//        String json = objectMapper.writeValueAsString(new Exception());
        response.getWriter().write(json);
    }
}
