package run.prizm.chat_translate_demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TranslationResponse {
    private String translatedMessage;
    private String originalMessage;
    private String roomId;
    private String sender;
}
