package sp.backend.chat;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("""
SELECT new sp.backend.chat.ChatDto(
c.id,
c.item.id,
c.item.title,
c.item.status,
new sp.backend.user.UserPublicDto(c.poster),
new sp.backend.user.UserPublicDto(c.interestedUser)
)
FROM Chat c
WHERE c.firstDeletedByUser IS NULL AND (:userId = c.poster.id OR :userId = c.interestedUser.id)
""")
    Page<ChatDto> findByUserId(Long userId, Pageable pageable);
}