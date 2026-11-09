package sp.backend.chat;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import sp.backend.user.User;
import sp.backend.user.UserRepository;
import sp.backend.exception.ExceptionMessage;

import static sp.backend.utils.Utils.getLoggedInUser;

public class ChatWithUser {
    private Chat chat;
    private User user;

    public ChatWithUser(Chat chat, User user) {
        this.chat = chat;
        this.user = user;
    }

    public static ChatWithUser checkIfUserIsParticipantOfChat(Long chatId, Authentication authentication, ChatRepository chatRepository, UserRepository userRepository) {
        User loggedInUser = getLoggedInUser(authentication, userRepository, true);

        Chat chat = chatRepository.findById(chatId).orElseThrow(() ->
                new EntityNotFoundException(ExceptionMessage.CHAT_NOT_FOUND));

        // Prüfe, ob der User Teilnehmer des Chats ist.
        if (!chat.isParticipant(loggedInUser)) {
            throw new AccessDeniedException(ExceptionMessage.ACCESS_DENIED);
        }
        return new ChatWithUser(chat, loggedInUser);
    }

    public Chat getChat() {
        return chat;
    }

    public User getUser() {
        return user;
    }
}
