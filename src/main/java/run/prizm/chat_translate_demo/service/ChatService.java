package run.prizm.chat_translate_demo.service;

import run.prizm.chat_translate_demo.model.ChatMessage;
import run.prizm.chat_translate_demo.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageRepository chatMessageRepository;

    public void sendMessage(ChatMessage message) {
        message.setCreatedAt(LocalDateTime.now());
        chatMessageRepository.save(message); // DB 저장
        messagingTemplate.convertAndSend("/exchange/amq.topic/chatroom/" + message.getRoomId(), message);
        // RabbitMQ 브로드캐스트
    }
}
