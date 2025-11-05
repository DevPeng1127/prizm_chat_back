package run.prizm.chat_translate_demo.controller;

import run.prizm.chat_translate_demo.model.ChatMessage;
import run.prizm.chat_translate_demo.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/chat.send")
    public void sendMessage(ChatMessage message) {
        System.out.println("📩 Received message for roomId=" + message.getRoomId());
        chatService.sendMessage(message);
    }
}
