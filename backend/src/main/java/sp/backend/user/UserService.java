/**
 * @author Trung Kien Huynh
 */
package sp.backend.user;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository){this.userRepository=userRepository;}

    public UserPublicDto findByIdPublic(Long userId){
        User user = userRepository.findById(userId).orElseThrow(()->new EntityNotFoundException("User not found: "+userId));
        return new UserPublicDto(user);
    }

    public UserPrivateDto findByIdPrivate(Long userId){
        User user = userRepository.findById(userId).orElseThrow(()->new EntityNotFoundException("User not found: "+userId));
        return new UserPrivateDto(user);
    }
}
