package run.prizm.chat_translate_demo.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TranslationRequest {
    private String message;
    private String roomId;
    private String sender;
}
