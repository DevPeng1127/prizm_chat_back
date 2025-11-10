package run.prizm.chat_translate_demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // ✅ STOMP 브로커 사용 (RabbitMQ)
        //    /topic, /queue prefix를 사용하는 메시지를 브로커로 라우팅
        registry.enableStompBrokerRelay("/exchange","/topic", "/queue")
                .setRelayHost("192.168.0.221") // application.yml에서 설정한 값과 동일하게
                .setRelayPort(61613)       // RabbitMQ STOMP 기본 포트
                .setClientLogin("admin")   // application.yml에서 설정한 값
                .setClientPasscode("1234")
                .setSystemLogin("admin")
                .setSystemPasscode("1234");

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

    @Bean
    public ThreadPoolTaskScheduler messageBrokerTaskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(1);
        taskScheduler.setThreadNamePrefix("wss-heartbeat-thread-");
        taskScheduler.initialize();
        return taskScheduler;
    }
}
