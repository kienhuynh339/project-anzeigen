package sp.backend.security;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSources;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.multipart.MultipartFile;
import sp.backend.dto.*;
import sp.backend.exception.DeletedItemModificationNotAllowedException;
import sp.backend.exception.ExceptionMessage;
import sp.backend.exception.ImageSavingException;
import sp.backend.exception.ImageSizeExceededException;
import sp.backend.item_category.Category;
import sp.backend.item_category.CategoryRepository;
import sp.backend.item_image.ItemImageRepository;
import sp.backend.location.GeodbLocation;
import sp.backend.location.GeodbLocationRepository;
import sp.backend.user.User;
import sp.backend.user.UserRepository;
import sp.backend.websocket.DataChangePublisher;
import sp.backend.websocket.DataChangePublisher.Action;
import sp.backend.websocket.DataChangePublisher.Entity;

import java.io.IOException;
import java.util.List;
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
