/**
 * @author Trung Kien Huynh
 */
package sp.backend.chat;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("""
SELECT new sp.backend.chat.ChatDto(
c.id,
c.item.id,
c.item.title,
c.item.status,
new sp.backend.user.UserPublicDto(c.poster),
new sp.backend.user.UserPublicDto(c.interestedUser),
null,
c.firstDeletedByUser.id
)
FROM Chat c
WHERE (c.firstDeletedByUser IS NULL OR (c.firstDeletedByUser.id != 0 AND c.firstDeletedByUser.id != :userId))
AND (:userId = c.poster.id OR :userId = c.interestedUser.id)
""")
    Page<ChatDto> findByUserId(Long userId, Pageable pageable);

    Optional<Chat> findByItem_IdAndInterestedUser_Id(Long itemId, Long interestedUserId);
}