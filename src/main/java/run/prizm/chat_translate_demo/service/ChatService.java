package run.prizm.chat_translate_demo.service;

import run.prizm.chat_translate_demo.model.ChatMessage;
import run.prizm.chat_translate_demo.model.ChatRoom;
import run.prizm.chat_translate_demo.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import run.prizm.chat_translate_demo.repository.ChatRoomRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final TranslationService translationService;

    public void sendMessage(ChatMessage message) {
        message.setCreatedAt(LocalDateTime.now());
        chatMessageRepository.save(message); // 1. 원본 메시지 DB에 저장

        ChatRoom chatRoom = chatRoomRepository.findById(message.getRoomId()).orElse(null);

        if (chatRoom == null) {
            logger.error("Chat room not found for id: {}", message.getRoomId());
            // Optionally, send an error message back to the user
            return;
        }

        if (chatRoom.isAutoTranslate()) {
            // 자동 번역이 켜진 경우
            translationService.translate(message.getContent())
                .subscribe(translatedContent -> {
                    ChatMessage broadcastMessage = new ChatMessage(
                            message.getId(),
                            message.getRoomId(),
                            message.getSender(),
                            translatedContent, // 번역된 내용으로 설정
                            message.getType(),
                            message.getCreatedAt()
                    );
                    messagingTemplate.convertAndSend("/topic/chatroom/" + message.getRoomId(), broadcastMessage);
                });
        } else {
            // 자동 번역이 꺼진 경우, 원본 메시지 전송
            messagingTemplate.convertAndSend("/topic/chatroom/" + message.getRoomId(), message);
        }
    }
}
