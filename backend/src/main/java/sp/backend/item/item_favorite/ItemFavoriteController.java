package sp.backend.item.item_favorite;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sp.backend.item.ItemDto;

@RestController
@RequestMapping("/api/favorites")
@CrossOrigin(origins = "http://localhost:5173")
public class ItemFavoriteController {
    private final ItemFavoriteRepository itemFavoriteRepository;
    private ItemFavoriteController(ItemFavoriteRepository itemFavoriteRepository){this.itemFavoriteRepository=itemFavoriteRepository;}

    @GetMapping
    private ResponseEntity<Page<ItemDto>> findByUserId(@RequestParam Long userId, Pageable pageable){
        Page<ItemDto> page = itemFavoriteRepository.findByUserId(userId,pageable);
        return ResponseEntity.ok(page);
    }
}
