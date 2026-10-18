package sp.backend.location.areacode;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GeodbAreaCodeRepository extends JpaRepository<GeodbAreacode, GeodbAreacodeId> {
//    List<GeodbAreacode> findById(Integer id);
    List<GeodbAreacode> findByPlzStartingWith(String plz);
}
