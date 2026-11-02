package sp.backend.utils;

import org.springframework.security.core.Authentication;
import sp.backend.user.User;
import sp.backend.user.UserRepository;

public class Utils {
    public static String getLoggedInAccountName(Authentication authentication) {
        return authentication != null ? authentication.getName() : null;
    }

    public static User getLoggedInUser(Authentication authentication, UserRepository userRepository) {
        if (authentication == null) {
            return null;
        }
        return userRepository.findByAccountName(authentication.getName()).orElse(null);
    }

    public static Long getLoggedInUserId(Authentication authentication, UserRepository userRepository) {
        if (authentication == null) {
            return null;
        }
        User user = userRepository.findByAccountName(authentication.getName()).orElse(null);
        return user != null ? user.getId() : null;
    }

    public enum ExceptionMessage {
        USER_NOT_FOUND("Nutzer wurde nicht gefunden."),
        ACCESS_DENIED("Zugriff verweigert."),
        ITEM_NOT_FOUND("Anzeige wurde nicht gefunden."),
        CATEGORY_NOT_FOUND("Kategorie wurde nicht gefunden."),
        CHAT_NOT_FOUND("Chat wurde nicht gefunden."),
        MESSAGE_NOT_FOUND("Nachricht wurde nicht gefunden."),
        LOCATION_NOT_FOUND("Ort wurde nicht gefunden."),
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
}
