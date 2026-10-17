package sp.backend.item.item_image;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class ItemImageController {
    private final ItemImageRepository itemImageRepository;
    private ItemImageController(ItemImageRepository itemImageRepository){
        this.itemImageRepository = itemImageRepository;
    }

    // ist dies nötig?
    @GetMapping("/anzeige-bilder/{imageId}")
    private ResponseEntity<ItemImage> findById(@PathVariable Long imageId){
        Optional<ItemImage> itemImage =itemImageRepository.findById(imageId);
        if(itemImage.isPresent()){
            return ResponseEntity.ok(itemImage.get());
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/anzeigen/{itemId}/bilder/erstes")
    private ResponseEntity<byte[]> findFirstByItemId(@PathVariable Long itemId){
        ItemImage itemImage = itemImageRepository.findFirstByItemIdOrderByDisplayOrderAsc(itemId);

        MediaType mediaType = MediaType.parseMediaType(itemImage.getContentType());

        return ResponseEntity.ok().contentType(mediaType).body(itemImage.getImage());
    }

    @GetMapping("/anzeigen/{itemId}/bilder")
    private ResponseEntity<List<ItemImage>> findByItemId(@PathVariable Long itemId){
        List<ItemImage> itemImages = itemImageRepository.findByItemIdOrderByDisplayOrderAsc(itemId);

        return ResponseEntity.ok(itemImages);
    }
}
