/**
 * @author Trung Kien Huynh
 */
package sp.backend.item;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;

    private ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/{itemId}")
    private ResponseEntity<ItemDto> findById(@PathVariable Long itemId, Authentication authentication) {

        ItemDto item = itemService.findByItemId(itemId, getLoggedInAccountName(authentication));
        if (item != null) {
            return ResponseEntity.ok(item);
        } else {
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
            @RequestParam(required = false) Integer radiusKm, Authentication authentication,
            Pageable pageable) {
        Page<ItemWithDistanceDto> page = itemService.search(searchTerm, categoryId, minPrice, maxPrice, locationId, radiusKm, getLoggedInAccountName(authentication), pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/user/{userId}")
    private ResponseEntity<Page<ItemDto>> findByUserId(@PathVariable Long userId, Authentication authentication, Pageable pageable) {
        Page<ItemDto> page = itemService.findByUserId(userId, getLoggedInAccountName(authentication), pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/user/favorites")
    private ResponseEntity<Page<ItemDto>> findFavoriteItems(Authentication authentication, Pageable pageable) {
        Page<ItemDto> page = itemService.findFavoriteItems(getLoggedInAccountName(authentication), pageable);
        return ResponseEntity.ok(page);
    }

    @DeleteMapping("/{itemId}")
    private ResponseEntity<Void> markItemAsDeleted(@PathVariable Long itemId, Authentication authentication) {
        try {
            itemService.markItemAsDeletedByItemId(itemId, getLoggedInAccountName(authentication));
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{itemId}")
    private ResponseEntity<Void> updateItemStatus(
            @PathVariable Long itemId,
            @RequestBody UpdateItemStatusRequest request, Authentication authentication) {
        try {
            itemService.updateItemStatus(itemId, request.getStatus(), getLoggedInAccountName(authentication));
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    private ResponseEntity<Void> createItem(@ModelAttribute CreateItemRequest request, UriComponentsBuilder ucb, Authentication authentication) {
        Long itemId = itemService.createItem(request, getLoggedInAccountName(authentication));
        URI createdItemLocation = ucb.path("/{itemId}").buildAndExpand(itemId).toUri();
        return ResponseEntity.created(createdItemLocation).build();
    }

    @PutMapping(value = "/{itemId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    private ResponseEntity<Void> updateItem(@PathVariable Long itemId, @ModelAttribute UpdateItemRequest request, Authentication authentication) {
        boolean result = itemService.updateItem(itemId, request, getLoggedInAccountName(authentication));
        return ResponseEntity.noContent().build();
    }

    private String getLoggedInAccountName(Authentication authentication) {
        return authentication != null
                ? authentication.getName()
                : "";
    }
}




















