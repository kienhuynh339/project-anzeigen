package sp.backend;

import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class TestUtils {
    public enum UserEnum {SAMWISE, MERRY, PIPPIN, FRODO,}

    public static TestRestTemplate tRTemplate(TestRestTemplate restTemplate) {
        return restTemplate;
    }

    public static TestRestTemplate tRTemplate(TestRestTemplate restTemplate, UserEnum userEnum) {
        if (userEnum == null) return restTemplate;
        return switch (userEnum) {
            case SAMWISE -> restTemplate.withBasicAuth("samwise", "sam123456");
            case MERRY -> restTemplate.withBasicAuth("merry", "mer123456");
            case PIPPIN -> restTemplate.withBasicAuth("pippin", "pip123456");
            case FRODO -> restTemplate.withBasicAuth("frodo", "fro123456");
            default -> restTemplate;
        };
    }

    public static HttpEntity<Void> requestEntity(TestRestTemplate template) {
        return requestEntity(template, null, null);
    }

    public static <T> HttpEntity<T> requestEntity(TestRestTemplate template, T body, MediaType contentType) {
        ResponseEntity<String> response = template.getForEntity(
                "/api/auth/csrf",
                String.class
        );

        List<String> cookies = response.getHeaders().get(HttpHeaders.SET_COOKIE);

        String csrfCookie = cookies.stream()
                .filter(cookie -> cookie.startsWith("XSRF-TOKEN="))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("CSRF-Cookie fehlt"));

        // Cookie inklusive Name und Wert
        String cookieValue = csrfCookie.split(";")[0];

        // Den rohen Token aus dem Cookie auslesen
        String token = cookieValue.substring("XSRF-TOKEN=".length());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, cookieValue);
        headers.add("X-XSRF-TOKEN", token);

        if (contentType != null) {
            headers.setContentType(contentType);
        }

        return new HttpEntity<>(body, headers);
    }
}
