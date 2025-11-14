package run.prizm.chat_translate_demo.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TranslationRequest {
    private Long messageId;
    private String targetLang;
}