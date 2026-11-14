/**
 * @author Trung Kien Huynh
 */
package sp.backend.chat_message;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import sp.backend.dto.MessageResponse;

import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("""
            SELECT new sp.backend.dto.MessageResponse(m)
            FROM Message m
            WHERE :chatId = m.chat.id
            """)
    Page<MessageResponse> findByChatId(Long chatId, Pageable pageable);

    Optional<Message> findFirstByChat_IdOrderByCreatedAtDesc(Long chatId);

    void deleteByChat_Id(Long chatId);

    @Modifying
    @Query("""
            UPDATE Message m SET m.isRead = true WHERE (m.chat.id = :chatId) AND (m.sender.id = :senderId)
            """)
    int markMessagesAsReadByChat_IdAndSender_Id(Long chatId, Long senderId);

}
