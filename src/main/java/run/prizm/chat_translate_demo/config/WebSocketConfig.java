package run.prizm.chat_translate_demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // ✅ 내장(Simple) 브로커 사용 — RabbitMQ 통하지 않음
        //    프론트에서 /topic, /queue prefix로 구독하면 바로 Spring이 브로드캐스트 처리함
        registry.enableSimpleBroker("/topic", "/queue");

        // ✅ 클라이언트가 publish할 때 사용할 prefix
        //    예: stompClient.publish({ destination: "/pub/chat.send", body: ... })
        registry.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
}
