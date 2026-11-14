/**
 * @author Trung Kien Huynh
 */
package sp.backend.item.item_favorite;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ItemFavoriteRepository extends JpaRepository<ItemFavorite, Long> {
    Optional<ItemFavorite> findByUser_IdAndItem_Id(Long userId, Long itemId);

}
