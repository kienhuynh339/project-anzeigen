/**
 * @author Trung Kien Huynh
 */
package sp.backend.item.item_favorite;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import sp.backend.dto.ItemFavoriteCreateRequest;
import sp.backend.dto.ItemFavoriteResponse;

import java.net.URI;

@RestController
@RequestMapping("/api/item-favorites")
public class ItemFavoriteController {
    private final ItemFavoriteService itemFavoriteService;

    private ItemFavoriteController(ItemFavoriteService itemFavoriteService) {
        this.itemFavoriteService = itemFavoriteService;
    }

    @GetMapping("/{itemFavoriteId}")
    private ResponseEntity<ItemFavoriteResponse> findById(@PathVariable Long itemFavoriteId, Authentication authentication) {
        ItemFavoriteResponse itemFavoriteResponse = itemFavoriteService.findById(itemFavoriteId, authentication);
        return ResponseEntity.ok(itemFavoriteResponse);
    }

    @PostMapping
    private ResponseEntity<Void> createItemFavorite(@Valid @RequestBody ItemFavoriteCreateRequest request, UriComponentsBuilder ucb, Authentication authentication) {
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
