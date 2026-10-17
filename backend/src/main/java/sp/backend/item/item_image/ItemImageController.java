package sp.backend.item.item_image;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ItemImageController {
    private final ItemImageRepository itemImageRepository;
    private ItemImageController(ItemImageRepository itemImageRepository){
        this.itemImageRepository = itemImageRepository;
    }

    // ist dies nötig?
    @GetMapping("/anzeige-bilder/{imageId}")
    private ResponseEntity<ItemImage> getById(@PathVariable Long imageId){
        Optional<ItemImage> itemImage =itemImageRepository.findById(imageId);
        if(itemImage.isPresent()){
            return ResponseEntity.ok(itemImage.get());
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/anzeigen/{itemId}/bilder/erstes")
    private ResponseEntity<ItemImage> getFirstByItemId(@PathVariable Long itemId){
        ItemImage itemImage = itemImageRepository.findFirstByItemIdOrderByDisplayOrderAsc(itemId);
        if(itemImage != null){
            return ResponseEntity.ok(itemImage);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/anzeigen/{itemId}/bilder")
    private ResponseEntity<List<ItemImage>> getByItemId(@PathVariable Long itemId){
        List<ItemImage> itemImages = itemImageRepository.findByItemIdOrderByDisplayOrderAsc(itemId);

        return ResponseEntity.ok(itemImages);
    }
}
