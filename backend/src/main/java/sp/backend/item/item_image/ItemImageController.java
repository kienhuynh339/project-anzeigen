/**
 * @author Trung Kien Huynh
 */
package sp.backend.item.item_image;

import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/images")
@CrossOrigin(origins = "http://localhost:5173")
public class ItemImageController {
    private final ItemImageRepository itemImageRepository;

    private ItemImageController(ItemImageRepository itemImageRepository) {
        this.itemImageRepository = itemImageRepository;
    }

    @GetMapping("/{imageId}")
    private ResponseEntity<byte[]> findById(@PathVariable Long imageId) {
        Optional<ItemImage> itemImage = itemImageRepository.findById(imageId);
        if (itemImage.isPresent()) {
            MediaType mediaType = MediaType.parseMediaType(itemImage.get().getContentType());
            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .cacheControl(CacheControl.maxAge(5, TimeUnit.MINUTES).cachePublic())
                    .body(itemImage.get().getImage());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/item/{itemId}/first")
    private ResponseEntity<byte[]> findFirstByItemId(@PathVariable Long itemId) {
        ItemImage itemImage = itemImageRepository.findFirstByItemIdOrderByDisplayOrderAsc(itemId);
        if (itemImage != null) {
            MediaType mediaType = MediaType.parseMediaType(itemImage.getContentType());
            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .cacheControl(CacheControl.maxAge(5, TimeUnit.MINUTES).cachePublic())
                    .body(itemImage.getImage());
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/item/{itemId}")
    private ResponseEntity<List<Long>> findByItemId(@PathVariable Long itemId) {
        List<ItemImage> itemImages = itemImageRepository.findByItemIdOrderByDisplayOrderAsc(itemId);
        List<Long> imageIds = itemImages.stream().map(ItemImage::getId).toList();

        return ResponseEntity.ok(imageIds);
    }
}
