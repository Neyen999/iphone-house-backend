package com.personal.iphonehouse.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Order(2)
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // Set the response status to 403
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        // Set the content type to JSON
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // Create a map to hold the exception details
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("message", "Access denied: code 403");
        errorDetails.put("exception", accessDeniedException.getClass().getName());
        errorDetails.put("stackTrace", accessDeniedException.getStackTrace());

        // Write the exception details to the response body as JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(errorDetails);
        response.getWriter().write(json);
    }
}
