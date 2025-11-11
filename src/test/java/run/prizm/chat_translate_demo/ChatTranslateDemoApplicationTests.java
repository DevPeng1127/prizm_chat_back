package run.prizm.chat_translate_demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.password=1234",
        "spring.rabbitmq.password=12.34",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class ChatTranslateDemoApplicationTests {

    @Test
    void contextLoads() {
    }

}
