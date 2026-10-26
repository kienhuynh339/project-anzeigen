/**
 * @author Trung Kien Huynh
 */
package sp.backend.item.item_image;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImageRepository extends JpaRepository<ItemImage, Long> {
    ItemImage findFirstByItemIdOrderByDisplayOrderAsc(Long itemId);
    List<ItemImage> findByItemIdOrderByDisplayOrderAsc(Long itemId);
}
