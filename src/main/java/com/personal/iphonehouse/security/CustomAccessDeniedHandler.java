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

@Order(2)
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // Set the response status to 403
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        // Set the content type to JSON
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        // Write the exception message to the response body
        ObjectMapper objectMapper = new ObjectMapper();
        // Create a custom exception object
//        CustomException customException = new CustomException(accessDeniedException, HttpStatus.FORBIDDEN);
//        customException.setErrorCode(403);
        RuntimeException exception = new RuntimeException("Acces denied code: 403");
//         Convert the custom exception object to a JSON string
        String json = objectMapper.writeValueAsString(exception);
//        String json = objectMapper.writeValueAsString(new Object());
        response.getWriter().write(json);

    }
}
