/**
 * @author Trung Kien Huynh
 */
package sp.backend.item;


import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import sp.backend.dto.*;

import java.net.URI;


@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;

    private ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/{itemId}")
    private ResponseEntity<ItemResponse> findById(@PathVariable Long itemId, Authentication authentication) {
        ItemResponse item = itemService.findByItemId(itemId, authentication);
        return ResponseEntity.ok(item);
    }

    @GetMapping("/{itemId}/edit")
    private ResponseEntity<ItemResponse> findByIdToEdit(@PathVariable Long itemId, Authentication authentication) {
        ItemResponse item = itemService.findByItemIdToEdit(itemId, authentication);
        return ResponseEntity.ok(item);
    }

    @GetMapping
    private ResponseEntity<Page<ItemWithDistanceResponse>> search(
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) Short categoryId,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) Integer locationId,
            @RequestParam(required = false) Integer radiusKm, Authentication authentication,
            Pageable pageable) {
        Page<ItemWithDistanceResponse> page = itemService.search(searchTerm, categoryId, minPrice, maxPrice, locationId, radiusKm, authentication, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/user/{userId}")
    private ResponseEntity<Page<ItemResponse>> findByUserId(@PathVariable Long userId, Authentication authentication, Pageable pageable) {
        Page<ItemResponse> page = itemService.findByUserId(userId, authentication, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/user/favorites")
    private ResponseEntity<Page<ItemResponse>> findFavoriteItems(Authentication authentication, Pageable pageable) {
        Page<ItemResponse> page = itemService.findFavoriteItems(authentication, pageable);
        return ResponseEntity.ok(page);
    }

    @DeleteMapping("/{itemId}")
    private ResponseEntity<Void> markItemAsDeleted(@PathVariable Long itemId, Authentication authentication) {
        itemService.markItemAsDeletedByItemId(itemId, authentication);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{itemId}")
    private ResponseEntity<Void> updateItemStatus(
            @PathVariable Long itemId,
            @Valid @RequestBody ItemStatusUpdateRequest request, Authentication authentication) {
        itemService.updateItemStatus(itemId, request.getStatus(), authentication);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    private ResponseEntity<Void> createItem(@Valid @ModelAttribute ItemCreateRequest request, UriComponentsBuilder ucb, Authentication authentication) {
        Long itemId = itemService.createItem(request, authentication);

        URI createdItemLocation = ucb.path("/{itemId}").buildAndExpand(itemId).toUri();

        return ResponseEntity.created(createdItemLocation).build();
    }

    @PutMapping(value = "/{itemId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    private ResponseEntity<Void> updateItem(@PathVariable Long itemId, @Valid @ModelAttribute ItemUpdateRequest request, Authentication authentication) {
        itemService.updateItem(itemId, request, authentication);
        return ResponseEntity.noContent().build();
    }
}




















