package com.cotede.interns.task.chatgpt;

import com.cotede.interns.task.exceptions.CustomExceptions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Data
public class ChatGPTService {
    private static final Logger logger = LoggerFactory.getLogger(ChatGPTService.class);

    private final String apiUrl;
    private final String apiKey;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public ChatGPTService(RestTemplateBuilder builder,
                          ObjectMapper objectMapper,
                          @Value("${chatgpt.api.url}") String apiUrl,
                          @Value("${chatgpt.api.key}") String apiKey) {
        this.restTemplate = builder
                .setConnectTimeout(Duration.ofSeconds(50))
                .setReadTimeout(Duration.ofSeconds(50))
                .build();
        this.objectMapper = objectMapper;
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
    }

    public CaseChatGptResponse getChatGptResponse(String difficulty) {
        try {
            HttpHeaders headers = createHeaders();
            String requestBody = createRequestBodyForChat(difficulty);
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, requestEntity, String.class);
            return parseCaseChatGptResponse(response);
        } catch (Exception e) {
            logger.error("Error calling ChatGPT API", e);
            throw new CustomExceptions.ChatGptResponseException();        }
    }

    private String createRequestBodyForChat(String diff) throws JsonProcessingException {

        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-3.5-turbo");
        body.put("messages", List.of(
                Map.of("role", "system", "content", "You are an assistant that generates detailed case structures with characters."),
                Map.of("role", "user", "content", ChatGPTConstants.CHAT_GPT_PROMPT + " Please generate a detailed case in JSON format including characters, and the difficulty of it is" + diff)
        ));
        body.put("max_tokens", 700);
        return objectMapper.writeValueAsString(body);
    }

    private CaseChatGptResponse parseCaseChatGptResponse(ResponseEntity<String> response) {
        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                Map<String, Object> responseMap = objectMapper.readValue(response.getBody(), Map.class);

                List<Map<String, Object>> choices = (List<Map<String, Object>>) responseMap.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    String content = (String) message.get("content");

                    CaseChatGptResponse caseChatGptResponse = objectMapper.readValue(content, CaseChatGptResponse.class);
                    return caseChatGptResponse;
                }
            } catch (JsonProcessingException e) {
                logger.error("Error parsing ChatGPT API response", e);
            }
        } else {
            logger.error("Received non-OK response: {} - {}", response.getStatusCode(), response.getBody());
        }
        return null;
    }

    public Optional<Double> compareAnswers(String userAnswer, String expectedAnswer) {
        try {
            HttpHeaders headers = createHeaders();
            String requestBody = createRequestBodyForComparison(userAnswer, expectedAnswer);
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, requestEntity, String.class);
            return parseChatGptComparisonResponse(response);
        } catch (Exception e) {
            logger.error("Error calling ChatGPT API for answer comparison", e);
            return Optional.empty();
        }
    }

    private String createRequestBodyForComparison(String userAnswer, String expectedAnswer) throws JsonProcessingException {
        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-3.5-turbo");
        body.put("messages", List.of(
                Map.of("role", "system", "content", "You are an assistant that evaluates the meaning of answers."),
                Map.of("role", "user", "content", "Compare these two answers for similarity of meaning:\n" +
                        "Answer 1: " + expectedAnswer + "\n" +
                        "Answer 2: " + userAnswer + "\n" +
                        "Give me a similarity score between 0 and 1. (it's not matter to be the same 100% to get 1 be a little bit kind), just return the number without any words Just the number, Example of the number 0.65")
        ));
        body.put("max_tokens", 100);
        return objectMapper.writeValueAsString(body);
    }

    private Optional<Double> parseChatGptComparisonResponse(ResponseEntity<String> response) {
        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                Map<String, Object> responseMap = objectMapper.readValue(response.getBody(), Map.class);
                List<Map<String, Object>> choices = (List<Map<String, Object>>) responseMap.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    String content = (String) message.get("content");
                    return Optional.of(Double.parseDouble(content.trim()));
                }
            } catch (JsonProcessingException e) {
                logger.error("Error parsing ChatGPT API response for comparison", e);
            }
        } else {
            logger.error("Received non-OK response: {} - {}", response.getStatusCode(), response.getBody());
        }
        return Optional.empty();
    }



    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);
        return headers;
    }
}
