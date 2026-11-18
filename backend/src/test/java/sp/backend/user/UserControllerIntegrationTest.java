/**
 * @author Trung Kien Huynh
 */
package sp.backend.user;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import sp.backend.TestUtils;
import sp.backend.dto.*;
import sp.backend.exception.ExceptionMessage;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static sp.backend.TestUtils.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@ActiveProfiles("test")
public class UserControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    // ---------------------------------------------------------
    // findById()
    // ---------------------------------------------------------

    @Nested
    class FindByIdTests {
        @Test
        void findById_userDoesNotExist_shouldThrow() {
            TestRestTemplate template = tRTemplate();

            ResponseEntity<ErrorResponse> response = template
                    .exchange("/api/users/{userId}",
                            HttpMethod.GET,
                            requestEntity(template),
                            ErrorResponse.class,
                            5);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(response.getBody()).isNotNull().extracting(ErrorResponse::getMessage)
                    .isEqualTo(ExceptionMessage.USER_NOT_FOUND);
        }

        @Test
        void findById_shouldReturnUserPublicResponse() {
            TestRestTemplate template = tRTemplate();

            ResponseEntity<UserPublicResponse> response = template
                    .exchange("/api/users/{userId}",
                            HttpMethod.GET,
                            requestEntity(template),
                            UserPublicResponse.class,
                            1);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull().extracting(
                    UserPublicResponse::getId,
                    UserPublicResponse::getProfileName,
                    u -> u.getCreatedAt().toInstant()
            ).containsExactly(1L, "Samwise", OffsetDateTime.parse("2020-01-01T01:01:00+02:00").toInstant());
        }
    }

    // ---------------------------------------------------------
    // findCurrentUser()
    // ---------------------------------------------------------

    @Nested
    class FindCurrentUserTests {
        @Test
        void findCurrentUser_unauthenticated_shouldThrow() {
            TestRestTemplate template = tRTemplate();

            ResponseEntity<ErrorResponse> response = template
                    .exchange("/api/users/me",
                            HttpMethod.GET,
                            requestEntity(template),
                            ErrorResponse.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
            assertThat(response.getBody()).isNotNull().extracting(ErrorResponse::getMessage)
                    .isEqualTo(ExceptionMessage.USER_NOT_AUTHENTICATED);
        }

        @Test
        void findCurrentUser_shouldReturnUserPrivateResponse() {
            TestRestTemplate template = tRTemplate(UserEnum.SAMWISE);

            ResponseEntity<UserPrivateResponse> response = template
                    .exchange("/api/users/me",
                            HttpMethod.GET,
                            requestEntity(template),
                            UserPrivateResponse.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull().extracting(
                    UserPrivateResponse::getId,
                    UserPrivateResponse::getAccountName,
                    UserPrivateResponse::getProfileName,
                    u -> u.getCreatedAt().toInstant()
            ).containsExactly(1L, "samwise", "Samwise", OffsetDateTime.parse("2020-01-01T01:01:00+02:00").toInstant());
        }
    }

    // ---------------------------------------------------------
    // updateProfileName()
    // ---------------------------------------------------------

    @Nested
    class UpdateProfileNameTests {
        private UserProfileNameUpdateRequest requestBody;

        @BeforeEach
        void setUp() {
            requestBody = new UserProfileNameUpdateRequest();
            requestBody.setProfileName("Mimimi");
        }

        @Test
        void updateProfileName_unauthenticated_shouldThrow() {
            TestRestTemplate template = tRTemplate();

            ResponseEntity<ErrorResponse> response = template
                    .exchange("/api/users/profile-name",
                            HttpMethod.PATCH,
                            requestEntity(template, requestBody, MediaType.APPLICATION_JSON),
                            ErrorResponse.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
            assertThat(response.getBody()).isNotNull().extracting(ErrorResponse::getMessage)
                    .isEqualTo(ExceptionMessage.USER_NOT_AUTHENTICATED);
        }

        @Test
        @Sql(scripts = "/reset-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
        void updateProfileName_shouldUpdateProfileName() {
            TestRestTemplate template = tRTemplate(UserEnum.SAMWISE);

            ResponseEntity<Void> response = template
                    .exchange("/api/users/profile-name",
                            HttpMethod.PATCH,
                            requestEntity(template, requestBody, MediaType.APPLICATION_JSON),
                            Void.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

            User user = userRepository.findById(1L).orElseThrow();
            assertThat(user.getProfileName()).isEqualTo("Mimimi");
        }
    }


    // ---------------------------------------------------------
    // updateAccountName()
    // ---------------------------------------------------------

    @Nested
    class UpdateAccountNameTests {
        private UserAccountNameUpdateRequest requestBody;

        @BeforeEach
        void setUp() {
            requestBody = new UserAccountNameUpdateRequest();
            requestBody.setAccountName("mimimi");
        }

        @Test
        void updateAccountName_unauthenticated_shouldThrow() {
            TestRestTemplate template = tRTemplate();

            ResponseEntity<ErrorResponse> response = template
                    .exchange("/api/users/account-name",
                            HttpMethod.PATCH,
                            requestEntity(template, requestBody, MediaType.APPLICATION_JSON),
                            ErrorResponse.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
            assertThat(response.getBody()).isNotNull().extracting(ErrorResponse::getMessage)
                    .isEqualTo(ExceptionMessage.USER_NOT_AUTHENTICATED);
        }

        @Test
        void updateAccountName_accountNameAlreadyExists_shouldThrow() {
            TestRestTemplate template = tRTemplate(UserEnum.SAMWISE);

            requestBody.setAccountName("pippin");

            ResponseEntity<ErrorResponse> response = template
                    .exchange("/api/users/account-name",
                            HttpMethod.PATCH,
                            requestEntity(template, requestBody, MediaType.APPLICATION_JSON),
                            ErrorResponse.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(response.getBody()).isNotNull().extracting(ErrorResponse::getMessage)
                    .isEqualTo(ExceptionMessage.ACCOUNT_NAME_ALREADY_EXISTS);
        }

        @Test
        @Sql(scripts = "/reset-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
        void updateAccountName_shouldUpdateAccountName() {
            TestRestTemplate template = tRTemplate(UserEnum.SAMWISE);

            ResponseEntity<Void> response = template
                    .exchange("/api/users/account-name",
                            HttpMethod.PATCH,
                            requestEntity(template, requestBody, MediaType.APPLICATION_JSON),
                            Void.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

            User user = userRepository.findById(1L).orElseThrow();
            assertThat(user.getAccountName()).isEqualTo("mimimi");
        }
    }


    // ---------------------------------------------------------
    // updatePassword()
    // ---------------------------------------------------------

    @Nested
    class UpdatePasswordTests {
        private UserPasswordUpdateRequest requestBody;

        @BeforeEach
        void setUp() {
            requestBody = new UserPasswordUpdateRequest();
            requestBody.setPassword("mimimi123456");
        }

        @Test
        void updatePassword_unauthenticated_shouldThrow() {
            TestRestTemplate template = tRTemplate();

            ResponseEntity<ErrorResponse> response = template
                    .exchange("/api/users/password",
                            HttpMethod.PATCH,
                            requestEntity(template, requestBody, MediaType.APPLICATION_JSON),
                            ErrorResponse.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
            assertThat(response.getBody()).isNotNull().extracting(ErrorResponse::getMessage)
                    .isEqualTo(ExceptionMessage.USER_NOT_AUTHENTICATED);
        }

        @Test
        @Sql(scripts = "/reset-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
        void updatePassword_shouldUpdatePassword() {
            TestRestTemplate template = tRTemplate(UserEnum.SAMWISE);

            ResponseEntity<Void> response = template
                    .exchange("/api/users/password",
                            HttpMethod.PATCH,
                            requestEntity(template, requestBody, MediaType.APPLICATION_JSON),
                            Void.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

            User user = userRepository.findById(1L).orElseThrow();
            assertThat(passwordEncoder.matches("mimimi123456", user.getPasswordHash())).isTrue();
        }
    }

    // ---------------------------------------------------------
    // createUser()
    // ---------------------------------------------------------

    @Nested
    class CreateUserTests {
        private RegisterRequest requestBody;

        @BeforeEach
        void setUp() {
            requestBody = new RegisterRequest();
            requestBody.setProfileName("Mimimi");
            requestBody.setAccountName("mimimi");
            requestBody.setPassword("mimimi123456");
        }

        @Test
        void createUser_accountNameAlreadyExists_shouldThrow() {
            TestRestTemplate template = tRTemplate();

            requestBody.setAccountName("samwise");

            ResponseEntity<ErrorResponse> response = template
                    .exchange("/api/users/register",
                            HttpMethod.POST,
                            requestEntity(template, requestBody, MediaType.APPLICATION_JSON),
                            ErrorResponse.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(response.getBody()).isNotNull().extracting(ErrorResponse::getMessage)
                    .isEqualTo(ExceptionMessage.ACCOUNT_NAME_ALREADY_EXISTS);
        }

        @Test
        @Sql(scripts = "/reset-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
        void createUser_shouldCreateUser() {
            TestRestTemplate template = tRTemplate();

            ResponseEntity<ErrorResponse> response = template
                    .exchange("/api/users/register",
                            HttpMethod.POST,
                            requestEntity(template, requestBody, MediaType.APPLICATION_JSON),
                            ErrorResponse.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(response.getHeaders().getLocation()).isNotNull().hasPath("/api/users/5");

            User user5 = userRepository.findById(5L).orElseThrow();
            assertThat(user5).extracting(
                            User::getId,
                            User::getProfileName,
                            User::getAccountName)
                    .containsExactly(5L, "Mimimi", "mimimi");
            assertThat(passwordEncoder.matches("mimimi123456", user5.getPasswordHash())).isTrue();
        }

    }


    private TestRestTemplate tRTemplate() {
        return TestUtils.tRTemplate(restTemplate);
    }

    private TestRestTemplate tRTemplate(UserEnum userEnum) {
        return TestUtils.tRTemplate(restTemplate, userEnum);
    }
}
