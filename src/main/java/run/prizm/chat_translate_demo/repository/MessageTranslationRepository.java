package run.prizm.chat_translate_demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import run.prizm.chat_translate_demo.model.MessageTranslation;

import java.util.Optional;

public interface MessageTranslationRepository extends JpaRepository<MessageTranslation, Long> {
    Optional<MessageTranslation> findByMessageIdAndLanguageCode(Long messageId, String languageCode);
}
