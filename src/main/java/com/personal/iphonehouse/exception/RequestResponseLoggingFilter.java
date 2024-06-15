package com.personal.iphonehouse.exception;

import jakarta.servlet.FilterChain;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Order(Ordered.LOWEST_PRECEDENCE - 8)
public class RequestResponseLoggingFilter extends OncePerRequestFilter {
    @Autowired
    private ErrorAttributes errorAttributes;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        Map<String, String[]> parameterMap = request.getParameterMap();
        String formData = mapToJson(parameterMap);

        logger.debug("HTTP Request  " + request.getMethod() + " : " + request.getRequestURI() + ", parameters="
                + formData);

        filterChain.doFilter(wrappedRequest, response);
        status = response.getStatus();

        if ((request.getMethod().equalsIgnoreCase("POST") || request.getMethod().equalsIgnoreCase("PUT"))
                && !request.getRequestURI().endsWith("login")) {
            Map<String, Object> trace = getTrace(wrappedRequest, status);
            getBody(wrappedRequest, trace);
            logger.trace(String.format("HTTP Request %s", trace));
        }

        if (status == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            Map<String, Object> trace = getTrace(wrappedRequest, status);
            getBody(wrappedRequest, trace);
            logError(wrappedRequest, trace);
        }

        logger.debug("HTTP Response : " + response.getStatus() + " " + response.getContentType());

    }

    private void logError(HttpServletRequest request, Map<String, Object> trace) {
        Object method = trace.get("method");
        Object path = trace.get("path");
        Object statusCode = trace.get("statusCode");

        logger.error(String.format("%s %s produced an error status code '%s'. Trace: '%s'", method, path, statusCode,
                trace));
    }

    private void getBody(ContentCachingRequestWrapper request, Map<String, Object> trace) {
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                String payload;
                try {
                    payload = new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
                } catch (UnsupportedEncodingException ex) {
                    payload = "[unknown]";
                }

                trace.put("body", payload);
            }
        }
    }

    protected Map<String, Object> getTrace(HttpServletRequest request, int status) {
        Throwable exception = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        WebRequest webRequest = new ServletWebRequest(request);

        Principal principal = request.getUserPrincipal();

        Map<String, Object> trace = new LinkedHashMap<>();
        trace.put("method", request.getMethod());
        trace.put("path", request.getRequestURI());
        if (principal != null) {
            trace.put("principal", principal.getName());
        }
        trace.put("query", request.getQueryString());
        trace.put("statusCode", status);

        if (exception != null && this.errorAttributes != null) {
            trace.put("error", this.errorAttributes);
        }

        return trace;
    }

    private String mapToJson(Map<String, String[]> map) {
        return map.entrySet().stream()
                .map(entry -> "\"" + entry.getKey() + "\": " + arrayToJson(entry.getValue()))
                .collect(Collectors.joining(", ", "{", "}"));
    }

    private String arrayToJson(String[] array) {
        return "[" + String.join(", ", array) + "]";
    }
}
