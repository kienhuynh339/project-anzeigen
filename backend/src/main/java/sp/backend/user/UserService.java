/**
 * @author Trung Kien Huynh
 */
package sp.backend.user;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sp.backend.dto.RegisterRequest;
import sp.backend.dto.UserPublicResponse;
import sp.backend.utils.Utils.ExceptionMessage;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserPublicResponse findPublicById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException(ExceptionMessage.USER_NOT_FOUND));
        return new UserPublicResponse(user);
    }

    public User register(RegisterRequest request) {
        if (userRepository.existsByAccountName(request.getAccountName())) {
            throw new IllegalArgumentException(ExceptionMessage.ACCOUNT_NAME_ALREADY_EXISTS);
        }

        User user = new User();
        user.setProfileName(request.getProfileName());
        user.setAccountName(request.getAccountName());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        return userRepository.save(user);
    }

    public User findByAccountName(String accountName) {
        return userRepository.findByAccountName(accountName).orElseThrow(() ->
                new EntityNotFoundException(ExceptionMessage.USER_NOT_FOUND));
    }

}
