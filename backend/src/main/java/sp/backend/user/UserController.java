package sp.backend.user;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import sp.backend.dto.UserPrivateResponse;
import sp.backend.dto.UserPublicResponse;

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
    public ResponseEntity<UserPrivateResponse> getCurrentUser(Authentication authentication) {
        User user = userService.findByAccountName(authentication.getName());

        return ResponseEntity.ok(new UserPrivateResponse(user));
    }
}
