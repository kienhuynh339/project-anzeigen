/**
 * @author Trung Kien Huynh
 */
package sp.backend.user;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import sp.backend.dto.*;

import java.net.URI;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    private UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    private ResponseEntity<UserPublicResponse> findById(@PathVariable Long userId) {
        UserPublicResponse user = userService.findPublicById(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/me")
    private ResponseEntity<UserPrivateResponse> findCurrentUser(Authentication authentication) {
        User user = userService.findByAccountName(authentication.getName());
        return ResponseEntity.ok(new UserPrivateResponse(user));
    }

    @PatchMapping("/profile-name")
    private ResponseEntity<Void> updateProfileName(@Valid @RequestBody UserProfileNameUpdateRequest request, Authentication authentication) {
        userService.updateProfileName(request, authentication);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/account-name")
    private ResponseEntity<Void> updateAccountName(@Valid @RequestBody UserAccountNameUpdateRequest request, Authentication authentication) {
        userService.updateAccountName(request, authentication);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/password")
    private ResponseEntity<Void> updatePassword(@Valid @RequestBody UserPasswordUpdateRequest request, Authentication authentication) {
        userService.updatePassword(request, authentication);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/register")
    private ResponseEntity<Void> createUser(@Valid @RequestBody RegisterRequest request, UriComponentsBuilder ucb) {
        Long userId = userService.createUser(request);

        URI createdUserLocation = ucb
                .path("/{userId}")
                .buildAndExpand(userId).toUri();

        return ResponseEntity.created(createdUserLocation).build();
    }
}
