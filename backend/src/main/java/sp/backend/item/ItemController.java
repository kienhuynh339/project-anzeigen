package sp.backend.item;


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

    @GetMapping("/suche")
    private ResponseEntity<List<ItemWithDistanceDto>> findWithinRadius(@RequestParam Integer locationId, @RequestParam Integer radiusKm){
        List<ItemWithDistanceDto> itemList = itemService.findWithinRadius(locationId, radiusKm);
        return ResponseEntity.ok(itemList);
    }
}




















