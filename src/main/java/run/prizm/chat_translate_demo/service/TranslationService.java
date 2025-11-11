package run.prizm.chat_translate_demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class TranslationService {

    private static final Logger logger = LoggerFactory.getLogger(TranslationService.class);
    private final WebClient webClient;

    @Value("${translation.api.url}")
    private String apiUrl;

    public TranslationService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(apiUrl).build();
    }

    public Mono<String> translate(String message) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("text", message);
        // TODO: Make target language configurable
        requestBody.put("target_lang", "en");

        return webClient.post()
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (String) response.get("result"))
                .doOnError(error -> logger.error("Translation API call failed", error))
                .onErrorReturn("Error: Translation failed.");
    }
}

