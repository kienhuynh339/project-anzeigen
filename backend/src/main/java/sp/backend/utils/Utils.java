/**
 * @author Trung Kien Huynh
 */
package sp.backend.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import sp.backend.user.User;
import sp.backend.user.UserRepository;
import sp.backend.exception.ExceptionMessage;

public class Utils {

    public static String getLoggedInAccountName(Authentication authentication) {
        return getLoggedInAccountName(authentication, false);
    }

    public static String getLoggedInAccountName(Authentication authentication, boolean throwOnUnauthenticatedUser) {
        return authentication != null ? authentication.getName() : (String) handleUnauthenticatedUser(throwOnUnauthenticatedUser);
    }

    public static User getLoggedInUser(Authentication authentication, UserRepository userRepository) {
        return getLoggedInUser(authentication, userRepository, false);
    }

    public static User getLoggedInUser(Authentication authentication, UserRepository userRepository, boolean throwOnUnauthenticatedUser) {
        if (authentication == null) {
            return (User) handleUnauthenticatedUser(throwOnUnauthenticatedUser);
        }
        return userRepository.findByAccountName(authentication.getName()).orElseGet(() ->
                (User) handleUnauthenticatedUser(throwOnUnauthenticatedUser));
    }

    public static Long getLoggedInUserId(Authentication authentication, UserRepository userRepository) {
        return getLoggedInUserId(authentication, userRepository, false);
    }

    public static Long getLoggedInUserId(Authentication authentication, UserRepository userRepository, boolean throwOnUnauthenticatedUser) {
        if (authentication == null) {
            return (Long) handleUnauthenticatedUser(throwOnUnauthenticatedUser);
        }

        return userRepository.findByAccountName(authentication.getName())
                .map(User::getId)
                .orElseGet(() -> (Long) handleUnauthenticatedUser(throwOnUnauthenticatedUser));
    }


    private static Object handleUnauthenticatedUser(boolean throwOnUnauthenticatedUser) {
        if (throwOnUnauthenticatedUser) {
            throw new AuthenticationException(ExceptionMessage.USER_NOT_AUTHENTICATED) {
            };
        } else {
            return null;
        }
    }
}
