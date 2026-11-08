/**
 * @author Trung Kien Huynh
 */
package sp.backend.location;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sp.backend.dto.GeodbLocationResponse;


import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class GeodbLocationController {
    private final GeodbLocationService geodbLocationService;

    private GeodbLocationController(GeodbLocationService geodbLocationService) {
        this.geodbLocationService = geodbLocationService;
    }

    @GetMapping
    private ResponseEntity<List<GeodbLocationResponse>> findByPostcode(@RequestParam String postcode, Pageable pageable) {
        List<GeodbLocationResponse> locationList = geodbLocationService.findByPostcode(postcode, pageable);

        if (locationList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(locationList);
    }

}
