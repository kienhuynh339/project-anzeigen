/**
 * @author Trung Kien Huynh
 */
package sp.backend.item_favorite;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemFavoriteRepository extends JpaRepository<ItemFavorite, Long> {
    Optional<ItemFavorite> findByUser_IdAndItem_Id(Long userId, Long itemId);

}
