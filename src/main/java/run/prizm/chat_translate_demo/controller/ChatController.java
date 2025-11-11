package run.prizm.chat_translate_demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import reactor.core.publisher.Mono;
import run.prizm.chat_translate_demo.model.ChatMessage;
import run.prizm.chat_translate_demo.model.TranslationRequest;
import run.prizm.chat_translate_demo.model.TranslationResponse;
import run.prizm.chat_translate_demo.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;
import run.prizm.chat_translate_demo.service.TranslationService;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
    private final ChatService chatService;
    private final TranslationService translationService;

    @MessageMapping("/chat.send")
    public void sendMessage(ChatMessage message) {
        logger.info("📩 Received message for roomId={}", message.getRoomId());
        chatService.sendMessage(message);
    }

    @MessageMapping("/chat.translate")
    @SendToUser("/queue/translate")
    public Mono<TranslationResponse> translate(TranslationRequest request) {
        logger.info("Received translation request for: {}", request.getMessage());
        return translationService.translate(request.getMessage())
                .map(translatedText -> new TranslationResponse(translatedText, request.getMessage(), request.getRoomId(), request.getSender()));
    }
}
