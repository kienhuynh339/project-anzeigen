package sp.backend.chat;

import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

@JsonTest
public class ChatJsonTest {
    private JacksonTester<Chat> json;
    private JacksonTester<Chat[]> jsonList;
    private Chat[] chats;
}
