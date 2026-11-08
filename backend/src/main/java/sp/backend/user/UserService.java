/**
 * @author Trung Kien Huynh
 */
package sp.backend.user;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sp.backend.dto.*;
import sp.backend.utils.Utils.ExceptionMessage;

import static sp.backend.utils.Utils.getLoggedInUser;

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

    public Long register(RegisterRequest request) {
        if (userRepository.existsByAccountName(request.getAccountName())) {
            throw new IllegalArgumentException(ExceptionMessage.ACCOUNT_NAME_ALREADY_EXISTS);
        }

        User user = new User();
        user.setProfileName(request.getProfileName());
        user.setAccountName(request.getAccountName());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        return user.getId();
    }

    public User findByAccountName(String accountName) {
        return userRepository.findByAccountName(accountName).orElseThrow(() ->
                new EntityNotFoundException(ExceptionMessage.USER_NOT_FOUND));
    }

    public void updateProfileName(UserProfileNameUpdateRequest request, Authentication authentication) {
        User user = getLoggedInUser(authentication, userRepository);

        user.setProfileName(request.getProfileName());
        userRepository.save(user);
    }

    public void updateAccountName(UserAccountNameUpdateRequest request, Authentication authentication) {
        User user = getLoggedInUser(authentication, userRepository);

        if (userRepository.existsByAccountName(request.getAccountName())) {
            throw new IllegalArgumentException(ExceptionMessage.ACCOUNT_NAME_ALREADY_EXISTS);
        }

        user.setAccountName(request.getAccountName());
        userRepository.save(user);
    }

    public void updatePassword(UserPasswordUpdateRequest request, Authentication authentication) {
        User user = getLoggedInUser(authentication, userRepository);

        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }

}
