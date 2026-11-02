package sp.backend.utils;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import sp.backend.user.User;
import sp.backend.user.UserRepository;

public class Utils {
    public enum ExceptionMessage {
        USER_NOT_FOUND("Der Nutzer wurde nicht gefunden."),
        USER_NOT_AUTHENTICATED("Der Nutzer ist nicht angemeldet."),
        ACCESS_DENIED("Zugriff verweigert."),
        ACCOUNT_NAME_ALREADY_EXISTS("Der Benutzername existiert bereits."),
        ITEM_NOT_FOUND("Die Anzeige wurde nicht gefunden."),
        FAVORITE_ITEM_NOT_FOUND("Die Anzeige existiert nicht in der Merkliste."),
        PASSWORD_TOO_SHORT("Das Passwort muss mindestens 6 Zeichen enthalten."),
        FAVORITE_ITEM_ALREADY_EXISTS("Die Anzeige existiert bereits in der Merkliste."),
        CATEGORY_NOT_FOUND("Die Kategorie wurde nicht gefunden."),
        CHAT_NOT_FOUND("Der Chat wurde nicht gefunden."),
        MESSAGE_NOT_FOUND("Die Nachricht wurde nicht gefunden."),
        LOCATION_NOT_FOUND("Der Ort wurde nicht gefunden."),
        ERROR_SAVING_IMAGE("Fehler beim Speichern des Bildes."),
        DELETED_ITEM_EDITING_NOT_ALLOWED("Eine gelöschte Anzeige darf nicht geändert werden.");

        private final String message;

        ExceptionMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

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
            throw new EntityNotFoundException(ExceptionMessage.USER_NOT_AUTHENTICATED.getMessage());
        } else {
            return null;
        }
    }
}
