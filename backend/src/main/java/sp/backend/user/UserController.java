package sp.backend.user;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {
    private final UserService userService;

    private UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    private ResponseEntity<UserPublicDto> findPublicById(@PathVariable Long userId){
        try{
            UserPublicDto user = userService.findByIdPublic(userId);
            return ResponseEntity.ok(user);
        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{userId}/private")
    private ResponseEntity<UserPrivateDto> findPrivateById(@PathVariable Long userId){
        try{
            UserPrivateDto user = userService.findByIdPrivate(userId);
            return ResponseEntity.ok(user);
        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }


}
