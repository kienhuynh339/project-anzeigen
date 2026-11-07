package sp.backend.user;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import sp.backend.dto.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    private UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    private ResponseEntity<UserPublicResponse> findPublicById(@PathVariable Long userId) {
        try {
            UserPublicResponse user = userService.findPublicById(userId);
            return ResponseEntity.ok(user);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/me")
    private ResponseEntity<UserPrivateResponse> getCurrentUser(Authentication authentication) {
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

}
