package sp.backend.location.areacode;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/plz")
public class GeodbAreaCodeController {
    private final GeodbAreaCodeRepository geodbAreaCodeRepository;
    private GeodbAreaCodeController(GeodbAreaCodeRepository geodbAreaCodeRepository){this.geodbAreaCodeRepository=geodbAreaCodeRepository;}

    @GetMapping("/{plz}")
    private ResponseEntity<List<GeodbAreacode>> findByPlzStartingWith(@PathVariable String plz){
        List<GeodbAreacode> areacodeList = geodbAreaCodeRepository.findByPlzStartingWith(plz);
        return ResponseEntity.ok(areacodeList);
    }

}
