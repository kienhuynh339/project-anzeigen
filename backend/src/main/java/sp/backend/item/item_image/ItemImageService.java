package sp.backend.item.item_image;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sp.backend.utils.Utils.ExceptionMessage;

import java.util.List;

@Service
public class ItemImageService {
    private final ItemImageRepository itemImageRepository;

    public ItemImageService(ItemImageRepository itemImageRepository) {
        this.itemImageRepository = itemImageRepository;
    }

    public ItemImage findById(Long imageId) {
        return itemImageRepository.findById(imageId).orElseThrow(() ->
                new EntityNotFoundException(ExceptionMessage.IMAGE_NOT_FOUND));
    }

    public ItemImage findFirstByItemId(Long itemId) {
        return itemImageRepository.findFirstByItemIdOrderByDisplayOrderAsc(itemId);
    }

    public List<Long> findImageIdsByItemId(Long itemId) {
        List<ItemImage> itemImages = itemImageRepository.findByItemIdOrderByDisplayOrderAsc(itemId);

        return itemImages.stream().map(ItemImage::getId).toList();
    }
}
