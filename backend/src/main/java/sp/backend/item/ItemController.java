/**
 * @author Trung Kien Huynh
 */
package sp.backend.item;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/items")
@CrossOrigin(origins = "http://localhost:5173")
public class ItemController {

    private final ItemService itemService;

    private ItemController(ItemService itemService){
        this.itemService = itemService;
    }

    @GetMapping("/{itemId}")
    private ResponseEntity<ItemDto> findById(@PathVariable Long itemId,@RequestParam(required = false) Long loggedInUserId){
        ItemDto item = itemService.findByItemId(itemId,loggedInUserId);
        if(item != null){
            return ResponseEntity.ok(item);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    private ResponseEntity<Page<ItemWithDistanceDto>> search(
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) Short categoryId,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) Integer locationId,
            @RequestParam(required = false) Integer radiusKm,@RequestParam(required = false) Long loggedInUserId,
            Pageable pageable) {
        Page<ItemWithDistanceDto> page = itemService.search(searchTerm,categoryId,minPrice,maxPrice,locationId,radiusKm,loggedInUserId,pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/user/{userId}")
    private ResponseEntity<Page<ItemDto>> findByUserId(@PathVariable Long userId, @RequestParam(required = false) Long loggedInUserId, Pageable pageable){
        Page<ItemDto> page = itemService.findByUserId(userId,loggedInUserId, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/user/{userId}/favorites")
    private ResponseEntity<Page<ItemDto>> findFavoriteItemsByUserId(@PathVariable Long userId, Pageable pageable){
        Page<ItemDto> page = itemService.findFavoriteItemsByUserId(userId, pageable);
        return ResponseEntity.ok(page);
    }

    @DeleteMapping("/{itemId}")
    private ResponseEntity<Void> markItemAsDeleted(@PathVariable Long itemId){
        try{
            itemService.markItemAsDeletedByItemId(itemId);
            return ResponseEntity.noContent().build();
        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Void> updateItemStatus(
            @PathVariable Long itemId,
            @RequestBody UpdateItemStatusRequest request) {
        try{
            itemService.updateItemStatus(itemId, request.getStatus());
            return ResponseEntity.noContent().build();
        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }
}




















