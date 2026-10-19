package sp.backend.location.areacode;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GeodbAreaCodeRepository extends JpaRepository<GeodbAreacode, GeodbAreacodeId> {
//    List<GeodbAreacode> findById(Integer id);
    @Query("""
SELECT DISTINCT g.plz FROM GeodbAreacode g
WHERE g.plz LIKE CONCAT(:plz, '%')
ORDER BY g.plz ASC
""")
    List<String> findDistinctByPlzStartingWithOrderByPlzAsc(@Param("plz") String plz, Pageable pageable);
}
