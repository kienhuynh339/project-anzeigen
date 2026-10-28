package sp.backend.item.item_favorite;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/item-favorites")
@CrossOrigin(origins = "http://localhost:5173")
public class ItemFavoriteController {
    private final ItemFavoriteService itemFavoriteService;

    private ItemFavoriteController(ItemFavoriteService itemFavoriteService) {
        this.itemFavoriteService = itemFavoriteService;
    }

    @GetMapping("/{itemFavoriteId}")
    private ResponseEntity<ItemFavoriteDto> findById(@PathVariable Long itemFavoriteId){
        try{
            ItemFavoriteDto itemFavoriteDto = itemFavoriteService.findById(itemFavoriteId);
            return ResponseEntity.ok(itemFavoriteDto);
        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    private ResponseEntity<Void> createItemFavorite(@RequestBody CreateItemFavoriteRequest createItemFavoriteRequest, UriComponentsBuilder ucb){
        ItemFavorite itemFavorite = itemFavoriteService.createItemFavorite(createItemFavoriteRequest.getUserId(),createItemFavoriteRequest.getItemId());
        URI locationOfItemFavorite = ucb.path("/api/item-favorites/{itemFavoriteId}").buildAndExpand(itemFavorite.getId()).toUri();
        return ResponseEntity.created(locationOfItemFavorite).build();
    }

    @DeleteMapping("/user/{userId}/item/{itemId}")
    private ResponseEntity<Void> deleteItemFavorite(@PathVariable Long userId, @PathVariable Long itemId){
        itemFavoriteService.deleteItemFavorite(userId, itemId);
        return ResponseEntity.noContent().build();
    }
}
