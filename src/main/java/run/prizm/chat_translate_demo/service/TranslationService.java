package run.prizm.chat_translate_demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

// @Service
@RequiredArgsConstructor
public class TranslationService {

    private final RestTemplate restTemplate;

    @Value("${translation.api.url}")
    private String apiUrl;

    public String translate(String message) {
        try {
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("message", message);

            Map<String, String> response = restTemplate.postForObject(apiUrl, requestBody, Map.class);

            if (response != null && response.containsKey("translated_message")) {
                return response.get("translated_message");
            }
            return "Error: Translation failed.";
        } catch (Exception e) {
            // Log the error
            e.printStackTrace();
            return "Error: Could not connect to translation service.";
        }
    }
}
