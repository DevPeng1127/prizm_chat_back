package run.prizm.chat_translate_demo.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import run.prizm.chat_translate_demo.model.Language;
import run.prizm.chat_translate_demo.model.Message;
import run.prizm.chat_translate_demo.model.MessageTranslation;
import run.prizm.chat_translate_demo.repository.LanguageRepository;
import run.prizm.chat_translate_demo.repository.MessageRepository;
import run.prizm.chat_translate_demo.repository.MessageTranslationRepository;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TranslationService {

    private static final Logger logger = LoggerFactory.getLogger(TranslationService.class);
    private final WebClient.Builder webClientBuilder;
    private final MessageRepository messageRepository;
    private final MessageTranslationRepository messageTranslationRepository;
    private final LanguageRepository languageRepository; // Assuming this repository is created

    @Value("${translation.api.url}")
    private String apiUrl;

    @Transactional
    public Mono<String> getOrTranslateMessage(Long messageId, String targetLangCode) {
        return Mono.fromCallable(() -> messageTranslationRepository.findByMessageIdAndLanguageCode(messageId, targetLangCode))
                .flatMap(existingTranslationOpt -> {
                    if (existingTranslationOpt.isPresent()) {
                        logger.info("Found existing translation for messageId: {}", messageId);
                        return Mono.just(existingTranslationOpt.get().getContent());
                    } else {
                        logger.info("No translation found for messageId: {}. Fetching and translating.", messageId);
                        Message message = messageRepository.findById(messageId)
                                .orElseThrow(() -> new RuntimeException("Message not found with id: " + messageId));

                        return callExternalTranslationApi(message.getContent(), targetLangCode)
                                .flatMap(translatedContent -> {
                                    Language targetLanguage = languageRepository.findById(targetLangCode)
                                            .orElseGet(() -> {
                                                Language newLang = new Language();
                                                newLang.setCode(targetLangCode);
                                                return languageRepository.save(newLang);
                                            });

                                    MessageTranslation newTranslation = MessageTranslation.builder()
                                            .message(message)
                                            .language(targetLanguage)
                                            .content(translatedContent)
                                            .build();
                                    messageTranslationRepository.save(newTranslation);
                                    logger.info("Saved new translation for messageId: {}", messageId);
                                    return Mono.just(translatedContent);
                                });
                    }
                });
    }

    private Mono<String> callExternalTranslationApi(String text, String targetLang) {
        WebClient webClient = webClientBuilder.baseUrl(apiUrl).build();
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("text", text);
        if (targetLang != null && !targetLang.isEmpty()) {
            requestBody.put("target_lang", targetLang);
        }

        return webClient.post()
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (String) response.get("result"))
                .doOnError(error -> logger.error("Translation API call failed", error))
                .onErrorReturn("Error: Translation failed.");
    }
}