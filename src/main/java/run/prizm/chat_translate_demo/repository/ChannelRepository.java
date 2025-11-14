package run.prizm.chat_translate_demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import run.prizm.chat_translate_demo.model.Channel;

public interface ChannelRepository extends JpaRepository<Channel, Long> {
}