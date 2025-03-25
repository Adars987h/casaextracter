package com.simplifymoney.casaextracter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import javax.naming.ServiceUnavailableException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GeminiExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Map<String, Object>> handleHttpClientErrorException(HttpClientErrorException ex) throws JsonProcessingException {
        Map<String, Object> errorResponse = new HashMap<>();
        String jsonResponse = ex.getResponseBodyAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);

        String message = rootNode.path("error").path("message").asText();
        errorResponse.put("error", message);

        return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<Map<String, Object>> handleServiceUnavailableExceptions(HttpServerErrorException ex) throws JsonProcessingException {
        Map<String, Object> errorResponse = new HashMap<>();
        if (ex.getStatusCode().equals(HttpStatus.SERVICE_UNAVAILABLE)) {
            errorResponse.put("error", "Too many requests sent to gemini server. Wait for a few minutes....");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }else{
            String jsonResponse = ex.getResponseBodyAsString();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            String message = rootNode.path("error").path("message").asText();
            errorResponse.put("error", message);

            return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleExceptions(Exception ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

}
