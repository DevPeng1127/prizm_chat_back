package run.prizm.chat_translate_demo.repository;

import run.prizm.chat_translate_demo.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}
