package sp.backend.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sp.backend.location.GeodbLocation;
import sp.backend.location.GeodbLocationRepository;

import java.util.Optional;

@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final GeodbLocationRepository geodbLocationRepository;

    public ItemService(ItemRepository itemRepository, GeodbLocationRepository geodbLocationRepository) {
        this.itemRepository = itemRepository;
        this.geodbLocationRepository = geodbLocationRepository;
    }

    public ItemDto findById(Long id){
        return itemRepository.findItemById(id);
    }

    public Page<ItemWithDistanceDto> search(
            String searchTerm,
            Short categoryId,
            Integer minPrice,
            Integer maxPrice,
            Integer locationId,
            Integer radiusKm,
            Pageable pageable) {

        Float latitude = null;
        Float longtitude = null;
        if (locationId != null && radiusKm != null) {
            Optional<GeodbLocation> location = geodbLocationRepository.findById(locationId);
            if (location.isPresent()) {
                latitude = location.get().getBreite();
                longtitude = location.get().getLaenge();
            }
        }
        return itemRepository.search(searchTerm, categoryId, minPrice, maxPrice, locationId, latitude, longtitude, radiusKm, pageable);
    }
}
