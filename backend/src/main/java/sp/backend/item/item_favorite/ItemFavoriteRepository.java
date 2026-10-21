package sp.backend.item.item_favorite;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sp.backend.item.ItemDto;

public interface ItemFavoriteRepository extends JpaRepository<ItemFavorite, Long> {

    @Query("""
SELECT new sp.backend.item.ItemDto(f.item)
FROM ItemFavorite f
WHERE :userId = f.user.id
""")
    Page<ItemDto> findByUserId(Long userId, Pageable pageable);
}
