package com.simplifymoney.casaextracter.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplifymoney.casaextracter.dto.AccountDetailsDTO;
import com.simplifymoney.casaextracter.service.ICasaExtractorService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.simplifymoney.casaextracter.util.FileUtil.downloadFile;
import static com.simplifymoney.casaextracter.util.FileUtil.extractTextFromPDF;

@Service
public class CasaExtractorService implements ICasaExtractorService {

    private static final Logger log = LoggerFactory.getLogger(CasaExtractorService.class);

    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";
    private static final String GEMINI_API_KEY = "AIzaSyDjpL47rQaNMkLf74NOAiBulHcQggyW6_s";

    @Override
    public AccountDetailsDTO extractDetailsFromPdf(MultipartFile file, String password) {
        try {
            // Save the file temporarily
            Path tempFile = downloadFile(file);

            // Extract text from PDF
            String extractedText = extractTextFromPDF(tempFile.toFile(), password);

            // Call OpenAI API for structured data extraction
            AccountDetailsDTO structuredData = extractDataUsingLLM(extractedText);

            // Delete temporary file
            Files.delete(tempFile);

            return structuredData;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private AccountDetailsDTO extractDataUsingLLM(String extractedText) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        Map<String, Object> requestBody = new HashMap<>();
        Map<String, Object> content = new HashMap<>();
        Map<String, Object> part = new HashMap<>();

        part.put("text", "Extract Name, Email, Opening Balance, and Closing Balance from the following text:\n" + extractedText +" and give response in json structure with key and values");
        content.put("parts", new Object[]{part});
        requestBody.put("contents", new Object[]{content});

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.exchange(GEMINI_API_URL+"?key="+GEMINI_API_KEY, HttpMethod.POST, entity, Map.class);

        if (response.getBody() != null && response.getBody().containsKey("candidates")) {
            try {
                List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.getBody().get("candidates");
                if (!candidates.isEmpty()) {
                    Map<String, Object> contentData = (Map<String, Object>) candidates.get(0).get("content");
                    List<Map<String, Object>> parts = (List<Map<String, Object>>) contentData.get("parts");

                    if (!parts.isEmpty() && parts.get(0).containsKey("text")) {
                        ObjectMapper objectMapper = new ObjectMapper();

                        // Extract actual JSON inside backticks
                        String jsonString = parts.get(0).get("text").toString()
                                .replaceAll("```json", "")
                                .replaceAll("```", "")
                                .trim();

                        // Convert extracted JSON string to HashMap
                        AccountDetailsDTO extractedMap = objectMapper.readValue(jsonString, AccountDetailsDTO.class);
                        return extractedMap;
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                throw e;
            }
        }
        return null;
    }

}
