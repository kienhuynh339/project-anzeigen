/**
 * @author Trung Kien Huynh
 */
package sp.backend.location.areacode;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/postcodes")
public class GeodbAreaCodeController {
    private final GeodbAreaCodeRepository geodbAreaCodeRepository;

    private GeodbAreaCodeController(GeodbAreaCodeRepository geodbAreaCodeRepository) {
        this.geodbAreaCodeRepository = geodbAreaCodeRepository;
    }

    @GetMapping
    private ResponseEntity<List<String>> findByPostcodeStartingWith(@RequestParam String postcodePrefix, Pageable pageable) {
        List<String> areacodeList = geodbAreaCodeRepository.findDistinctByPostcodeStartingWithOrderByPostcodeAsc(postcodePrefix, pageable);
        return ResponseEntity.ok(areacodeList);
    }

}
