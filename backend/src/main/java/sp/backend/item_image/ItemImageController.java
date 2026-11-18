/**
 * @author Trung Kien Huynh
 */
package sp.backend.item_image;

import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/images")
public class ItemImageController {
    private final ItemImageService itemImageService;

    private ItemImageController(ItemImageService itemImageService) {
        this.itemImageService = itemImageService;
    }

    @GetMapping("/{imageId}")
    private ResponseEntity<byte[]> findById(@PathVariable Long imageId) {
        ItemImage itemImage = itemImageService.findById(imageId);

        MediaType mediaType = MediaType.parseMediaType(itemImage.getContentType());

        return ResponseEntity.ok()
                .contentType(mediaType)
                .cacheControl(CacheControl.maxAge(5, TimeUnit.MINUTES).cachePublic())
                .body(itemImage.getImage());
    }

    @GetMapping("/item/{itemId}/first")
    private ResponseEntity<byte[]> findFirstByItemId(@PathVariable Long itemId) {
        ItemImage itemImage = itemImageService.findFirstByItemId(itemId);

        if (itemImage != null) {
            MediaType mediaType = MediaType.parseMediaType(itemImage.getContentType());
            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .body(itemImage.getImage());
        }

        // Anzeige ohne Bilder ist erlaubt.
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/item/{itemId}")
    private ResponseEntity<List<Long>> findImageIdsByItemId(@PathVariable Long itemId) {
        List<Long> imageIds = itemImageService.findImageIdsByItemId(itemId);

        if (imageIds.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(imageIds);
    }
}
