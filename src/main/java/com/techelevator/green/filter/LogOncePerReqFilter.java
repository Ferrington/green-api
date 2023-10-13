package com.techelevator.green.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techelevator.green.model.log.Action;
import com.techelevator.green.repository.ActionRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.security.Principal;


public class LogOncePerReqFilter extends OncePerRequestFilter {

    @Autowired
    ActionRepository actionRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        ContentCachingRequestWrapper cachingRequestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper cachingResponseWrapper = new ContentCachingResponseWrapper(response);

        filterChain.doFilter(cachingRequestWrapper, cachingResponseWrapper);

        String requestBody = getValueAsString(cachingRequestWrapper.getContentAsByteArray(),
                cachingRequestWrapper.getCharacterEncoding());
        String responseBody = getValueAsString(cachingResponseWrapper.getContentAsByteArray(),
                cachingResponseWrapper.getCharacterEncoding());

        logReqRes(requestBody, responseBody, cachingRequestWrapper.getRequestURI(), cachingRequestWrapper.getMethod(),
                cachingRequestWrapper.getUserPrincipal());

        cachingResponseWrapper.copyBodyToResponse();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getRequestURI().startsWith("/api/student");
    }

    private String getValueAsString(byte[] contentAsByteArray, String characterEncoding) {
        String dataAsString = "";
        try {
            dataAsString = new String(contentAsByteArray, characterEncoding);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataAsString;
    }

    private void logReqRes(String request, String response, String uri, String httpMethod, Principal principal) {
        Action action = new Action();

        action.setRequestBody(unprettifyJson(request));
        action.setResponseBody(response);
        action.setUri(uri);
        action.setHttpMethod(httpMethod);
        if (principal != null) {
            action.setUserName(principal.getName());
        }

        actionRepository.save(action);
    }

    private String unprettifyJson(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonString, JsonNode.class).toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
