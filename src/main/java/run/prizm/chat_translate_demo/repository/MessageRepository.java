package run.prizm.chat_translate_demo.repository;

import run.prizm.chat_translate_demo.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}