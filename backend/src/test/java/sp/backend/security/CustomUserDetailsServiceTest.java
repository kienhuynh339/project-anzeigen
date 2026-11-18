/**
 * @author Trung Kien Huynh
 */
package sp.backend.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import sp.backend.dto.*;
import sp.backend.user.User;
import sp.backend.user.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CustomUserDetailsServiceTest {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private UserRepository userRepository;

    private final String accountName = "samwise";
    private final String passwortHash = "hash";

    // ---------------------------------------------------------
    // loadUserByUsername()
    // ---------------------------------------------------------

    @Test
    void loadUserByUsername_shouldThrowWhenUserDoesNotExist() {
        when(userRepository.findByAccountName(accountName)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername(accountName));
        assertEquals("User not found", exception.getMessage());

        verify(userRepository).findByAccountName(accountName);
    }

    @Test
    void loadUserByUsername_shouldReturnUserDetails() {
        User user = mock(User.class);

        when(user.getAccountName()).thenReturn(accountName);
        when(user.getPasswordHash()).thenReturn(passwortHash);

        when(userRepository.findByAccountName(accountName)).thenReturn(Optional.of(user));

        UserDetails result = customUserDetailsService.loadUserByUsername(accountName);

        assertNotNull(result);
        assertEquals(user.getAccountName(), result.getUsername());
        assertEquals(user.getPasswordHash(), result.getPassword());
    }
}
