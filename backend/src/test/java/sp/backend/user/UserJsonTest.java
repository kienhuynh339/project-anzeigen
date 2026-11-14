package sp.backend.user;

import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

@JsonTest
public class UserJsonTest {
    private JacksonTester<User> json;
}
