package sp.backend.item;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class ItemService {
    private final ItemRepository itemRepository;
    public ItemService(ItemRepository itemRepository){this.itemRepository=itemRepository;}

    public List<ItemWithDistanceDto> findWithinRadius(Integer locationId, Integer radiusKm){
        List<ItemWithDistance> itemWithDistanceList = itemRepository.findWithinRadius(locationId, radiusKm);
        return itemWithDistanceList.stream().map(ItemWithDistanceDto::new).toList();
    }

}
