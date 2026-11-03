package sp.backend.item.item_favorite;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/item-favorites")
public class ItemFavoriteController {
    private final ItemFavoriteService itemFavoriteService;

    private ItemFavoriteController(ItemFavoriteService itemFavoriteService) {
        this.itemFavoriteService = itemFavoriteService;
    }

    @GetMapping("/{itemFavoriteId}")
    private ResponseEntity<ItemFavoriteDto> findById(@PathVariable Long itemFavoriteId, Authentication authentication) {
        try {
            ItemFavoriteDto itemFavoriteDto = itemFavoriteService.findById(itemFavoriteId, authentication);
            return ResponseEntity.ok(itemFavoriteDto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    private ResponseEntity<Void> createItemFavorite(@Valid @RequestBody CreateItemFavoriteRequest request, UriComponentsBuilder ucb, Authentication authentication) {
        ItemFavorite itemFavorite = itemFavoriteService.createItemFavorite(request.getItemId(), authentication);
        URI locationOfItemFavorite = ucb
                .path("/api/item-favorites/{itemFavoriteId}")
                .buildAndExpand(itemFavorite.getId()).toUri();
        return ResponseEntity.created(locationOfItemFavorite).build();
    }

    @DeleteMapping("/item/{itemId}")
    private ResponseEntity<Void> deleteItemFavorite(@PathVariable Long itemId, Authentication authentication) {
        itemFavoriteService.deleteItemFavorite(itemId, authentication);
        return ResponseEntity.noContent().build();
    }
}
