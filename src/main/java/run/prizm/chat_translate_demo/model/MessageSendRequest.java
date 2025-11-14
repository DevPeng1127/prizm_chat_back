package run.prizm.chat_translate_demo.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageSendRequest {
    private Long channelId;
    private Long workspaceUserId;
    private String contentType; // e.g., "STRING", "LINK"
    private String content;
}
