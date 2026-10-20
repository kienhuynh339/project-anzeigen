package sp.backend.item;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/anzeigen")
@CrossOrigin(origins = "http://localhost:5173")
public class ItemController {

    private ItemRepository itemRepository;
    private ItemService itemService;

    private ItemController(ItemRepository itemRepository, ItemService itemService){
        this.itemRepository = itemRepository;
        this.itemService = itemService;
    }

    @GetMapping
    private ResponseEntity<List<Item>> findAll(){
        return ResponseEntity.ok(itemRepository.findAll());
    }

    @GetMapping("/{itemId}")
    private ResponseEntity<Item> findById(@PathVariable Long itemId){
        Optional<Item> item = itemRepository.findById(itemId);
        if(item.isPresent()){
            return ResponseEntity.ok(item.get());
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ItemWithDistance>> search(
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) Short categoryId,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) Integer locationId,
            @RequestParam(required = false) Integer radiusKm,
            Pageable pageable) {
        Page<ItemWithDistance> page = itemService.search(searchTerm,categoryId,minPrice,maxPrice,locationId,radiusKm,pageable);
        return ResponseEntity.ok(page);
    }
}




















