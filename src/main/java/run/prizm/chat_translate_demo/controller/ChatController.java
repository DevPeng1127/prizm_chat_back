package run.prizm.chat_translate_demo.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import run.prizm.chat_translate_demo.model.*;
import run.prizm.chat_translate_demo.repository.MessageTypeRepository;
import run.prizm.chat_translate_demo.service.ChatService;
import run.prizm.chat_translate_demo.service.TranslationService;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
    private final ChatService chatService;
    private final TranslationService translationService;
    private final MessageTypeRepository messageTypeRepository; // To fetch MessageType entity

    /**
     * Handles incoming chat messages from clients via WebSocket.
     * @param request The request object containing message details.
     */
    @MessageMapping("/chat.send")
    public void sendMessage(MessageSendRequest request) {
        logger.info("📩 Received message for channelId={}", request.getChannelId());

        MessageType messageType = messageTypeRepository.findById(request.getContentType())
                .orElseThrow(() -> new RuntimeException("Invalid message type: " + request.getContentType()));

        Message message = Message.builder()
                .channelId(request.getChannelId())
                .workspaceUserId(request.getWorkspaceUserId())
                .type(messageType)
                .content(request.getContent())
                .build();

        chatService.sendMessage(message);
    }

    /**
     * Handles translation requests from clients via WebSocket.
     * @param request The request object containing the message ID and target language.
     * @return A Mono containing the translation response.
     */
    @MessageMapping("/chat.translate")
    @SendToUser("/queue/translate")
    public Mono<TranslationResponse> translate(TranslationRequest request) {
        logger.info("Received translation request for messageId: {}", request.getMessageId());
        // Original message content is not available here directly, would need another fetch if required for the response.
        // For simplicity, returning null for original message.
        return translationService.getOrTranslateMessage(request.getMessageId(), request.getTargetLang())
                .map(translatedText -> new TranslationResponse(
                        request.getMessageId(),
                        translatedText,
                        null, // Original message would require another DB lookup
                        request.getTargetLang()
                ));
    }

    /**
     * Handles translation requests via a REST API endpoint.
     * @param request The request object containing the message ID and target language.
     * @return A Mono containing the translation response.
     */
    @PostMapping("/api/translate")
    public Mono<TranslationResponse> handleTranslateApi(@RequestBody TranslationRequest request) {
        logger.info("Received API translation request for messageId: {}", request.getMessageId());
        return translationService.getOrTranslateMessage(request.getMessageId(), request.getTargetLang())
                .map(translatedText -> new TranslationResponse(
                        request.getMessageId(),
                        translatedText,
                        null, // Original message would require another DB lookup
                        request.getTargetLang()
                ));
    }
}