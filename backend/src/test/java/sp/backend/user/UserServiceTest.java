/**
 * @author Trung Kien Huynh
 */
package sp.backend.user;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import sp.backend.dto.*;
import sp.backend.exception.ExceptionMessage;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    private Authentication authentication;
    private User loggedInUser;

    @BeforeEach
    void setUp() {
        loggedInUser = mock(User.class);
        when(loggedInUser.getId()).thenReturn(1L);
        when(loggedInUser.getAccountName()).thenReturn("samwise");
        when(loggedInUser.getPasswordHash()).thenReturn("hash");
        when(loggedInUser.getProfileName()).thenReturn("Samwise");

        when(userRepository.findByAccountName(loggedInUser.getAccountName())).thenReturn(Optional.of(loggedInUser));

        authentication = new UsernamePasswordAuthenticationToken(loggedInUser.getAccountName(), loggedInUser.getPasswordHash());
    }


    // ---------------------------------------------------------
    // findPublicById()
    // ---------------------------------------------------------

    @Nested
    class FindPublicByIdTests {
        private final Long userId = 1L;

        @Test
        void findPublicById_shouldThrowWhenUserDoesNotExist() {
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> userService.findPublicById(userId));
            assertEquals(ExceptionMessage.USER_NOT_FOUND, exception.getMessage());
        }

        @Test
        void findPublicById_shouldReturnUserPublicResponse() {
            User user = mock(User.class);

            when(user.getId()).thenReturn(userId);

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));

            UserPublicResponse result = userService.findPublicById(userId);

            assertNotNull(result);
            assertEquals(user.getId(), result.getId());
        }
    }

    // ---------------------------------------------------------
    // createUser()
    // ---------------------------------------------------------

    @Nested
    class CreateUserTests {
        private RegisterRequest request;

        @BeforeEach
        void setUp() {
            request = new RegisterRequest();
            request.setAccountName("samwise");
        }

        @Test
        void createUser_shouldThrowWhenAccountNameAlreadyExists() {
            when(userRepository.existsByAccountName(request.getAccountName())).thenReturn(true);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> userService.createUser(request));
            assertEquals(ExceptionMessage.ACCOUNT_NAME_ALREADY_EXISTS, exception.getMessage());

            verify(userRepository, never()).save(any());
        }

        @Test
        void createUser_shouldCreateUser() {
            when(userRepository.existsByAccountName(request.getAccountName())).thenReturn(false);

            when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
                User savedUser = invocation.getArgument(0);
                savedUser.setId(11L);
                return savedUser;
            });

            Long result = userService.createUser(request);

            assertEquals(11L, result);
            verify(userRepository).save(any(User.class));
        }
    }

    // ---------------------------------------------------------
    // findByAccountName()
    // ---------------------------------------------------------

    @Nested
    class FindByAccountNameTests {
        private final String accountName = "samwise";

        @Test
        void findByAccountName_shouldThrowWhenUserDoesNotExist() {
            when(userRepository.findByAccountName(accountName)).thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> userService.findByAccountName(accountName));
            assertEquals(ExceptionMessage.USER_NOT_FOUND, exception.getMessage());
        }

        @Test
        void findByAccountName_shouldReturnUser() {
            User user = mock(User.class);

            when(user.getId()).thenReturn(1L);

            when(userRepository.findByAccountName(accountName)).thenReturn(Optional.of(user));

            User result = userService.findByAccountName(accountName);

            assertNotNull(result);
            assertEquals(user.getId(), result.getId());
        }
    }

    // ---------------------------------------------------------
    // updateProfileName()
    // ---------------------------------------------------------

    @Nested
    class UpdateProfileNameTests {
        private UserProfileNameUpdateRequest request;

        @BeforeEach
        void setUp() {
            request = new UserProfileNameUpdateRequest();
            request.setProfileName("Gandalf");
        }

        @Test
        void updateProfileName_shouldThrowWhenUserIsNotAuthenticated() {
            AuthenticationException exception = assertThrows(AuthenticationException.class,
                    () -> userService.updateProfileName(request, null));
            assertEquals(ExceptionMessage.USER_NOT_AUTHENTICATED, exception.getMessage());

            verify(userRepository, never()).save(any());
        }

        @Test
        void updateProfileName_shouldUpdateProfileName() {
            userService.updateProfileName(request, authentication);

            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
            verify(userRepository).save(userCaptor.capture());

            User user = userCaptor.getValue();
            verify(user).setProfileName(request.getProfileName());
        }
    }

    // ---------------------------------------------------------
    // updateAccountName()
    // ---------------------------------------------------------

    @Nested
    class UpdateAccountNameTests {
        private UserAccountNameUpdateRequest request;

        @BeforeEach
        void setUp() {
            request = new UserAccountNameUpdateRequest();
            request.setAccountName("gandalf");
        }

        @Test
        void updateAccountName_shouldThrowWhenUserIsNotAuthenticated() {
            AuthenticationException exception = assertThrows(AuthenticationException.class,
                    () -> userService.updateAccountName(request, null));
            assertEquals(ExceptionMessage.USER_NOT_AUTHENTICATED, exception.getMessage());

            verify(userRepository, never()).existsByAccountName(any());
        }

        @Test
        void updateAccountName_shouldThrowWhenAccountNameAlreadyExists() {
            when(userRepository.existsByAccountName(request.getAccountName())).thenReturn(true);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> userService.updateAccountName(request, authentication));
            assertEquals(ExceptionMessage.ACCOUNT_NAME_ALREADY_EXISTS, exception.getMessage());

            verify(userRepository, never()).save(any());
        }

        @Test
        void updateAccountName_shouldUpdateAccountName() {
            when(userRepository.existsByAccountName(request.getAccountName())).thenReturn(false);

            userService.updateAccountName(request, authentication);

            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
            verify(userRepository).save(userCaptor.capture());

            User user = userCaptor.getValue();
            verify(user).setAccountName(request.getAccountName());
        }
    }

    // ---------------------------------------------------------
    // updatePassword()
    // ---------------------------------------------------------

    @Nested
    class UpdatePasswordTests {
        private UserPasswordUpdateRequest request;

        @BeforeEach
        void setUp() {
            request = new UserPasswordUpdateRequest();
            request.setPassword("mimimi");
        }

        @Test
        void updatePassword_shouldThrowWhenUserIsNotAuthenticated() {
            AuthenticationException exception = assertThrows(AuthenticationException.class,
                    () -> userService.updatePassword(request, null));
            assertEquals(ExceptionMessage.USER_NOT_AUTHENTICATED, exception.getMessage());

            verify(userRepository, never()).save(any());
        }

        @Test
        void updatePassword_shouldUpdatePassword() {
            when(passwordEncoder.encode(request.getPassword())).thenReturn("hash");

            userService.updatePassword(request, authentication);

            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
            verify(userRepository).save(userCaptor.capture());

            User user = userCaptor.getValue();
            verify(user).setPasswordHash("hash");
        }
    }


}
