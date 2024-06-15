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
import java.util.HashMap;
import java.util.Map;

@Order(1)
public class CustomAuthenticationEntrypoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // Set the response status to 401
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // Set the content type to JSON
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // Create a map to hold the exception details
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("message", "Access denied: code 401");
        errorDetails.put("exception", authException.getClass().getName());
        errorDetails.put("stackTrace", authException.getStackTrace());

        // Write the exception details to the response body as JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(errorDetails);
        response.getWriter().write(json);
    }
}
