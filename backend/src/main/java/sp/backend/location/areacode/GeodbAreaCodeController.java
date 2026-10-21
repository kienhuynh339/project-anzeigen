package sp.backend.location.areacode;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/postcodes")
@CrossOrigin(origins = "http://localhost:5173")
public class GeodbAreaCodeController {
    private final GeodbAreaCodeRepository geodbAreaCodeRepository;
    private GeodbAreaCodeController(GeodbAreaCodeRepository geodbAreaCodeRepository){this.geodbAreaCodeRepository=geodbAreaCodeRepository;}

    @GetMapping
    private ResponseEntity<List<String>> findByPlzStartingWith(@RequestParam String postcodePrefix, @RequestParam(defaultValue = "15") int limit){
        int pageSize = Math.clamp(limit, 1,100);
        Pageable pageable = PageRequest.of(0,pageSize);

        List<String> areacodeList = geodbAreaCodeRepository.findDistinctByPlzStartingWithOrderByPlzAsc(postcodePrefix, pageable);
        return ResponseEntity.ok(areacodeList);
    }

}
