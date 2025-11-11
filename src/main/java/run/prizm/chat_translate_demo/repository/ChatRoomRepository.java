package run.prizm.chat_translate_demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import run.prizm.chat_translate_demo.model.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
