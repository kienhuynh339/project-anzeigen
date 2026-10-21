package sp.backend.location;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/ort")
@CrossOrigin(origins = "http://localhost:5173")
public class GeodbLocationController {
    private final GeodbLocationService geodbLocationService;
    private GeodbLocationController(GeodbLocationService geodbLocationService){this.geodbLocationService=geodbLocationService;}

    @GetMapping
    private ResponseEntity<List<GeodbLocationDto>> findByPlzAndNameStartingWith(@RequestParam String plz, @RequestParam String name, @RequestParam(defaultValue="15") int limit){
        int pageSize = Math.clamp(limit, 1, 100);
        Pageable pageable = PageRequest.of(0,pageSize);

        List<GeodbLocationDto> locationList = geodbLocationService.findByPlzAndName(plz, name, pageable);
        return ResponseEntity.ok(locationList);
    }
    
}
