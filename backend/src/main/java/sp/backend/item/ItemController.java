package sp.backend.item;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/items")
@CrossOrigin(origins = "http://localhost:5173")
public class ItemController {

    private final ItemService itemService;

    private ItemController(ItemService itemService){
        this.itemService = itemService;
    }

    @GetMapping("/{itemId}")
    private ResponseEntity<ItemDto> findById(@PathVariable Long itemId){
        ItemDto item = itemService.findById(itemId);
        if(item != null){
            return ResponseEntity.ok(item);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<Page<ItemWithDistanceDto>> search(
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) Short categoryId,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) Integer locationId,
            @RequestParam(required = false) Integer radiusKm,
            Pageable pageable) {
        Page<ItemWithDistanceDto> page = itemService.search(searchTerm,categoryId,minPrice,maxPrice,locationId,radiusKm,pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<ItemDto>> findByUserId(@PathVariable Long userId, Pageable pageable){
        Page<ItemDto> page = itemService.findByUserId(userId, pageable);
        return ResponseEntity.ok(page);
    }
}




















