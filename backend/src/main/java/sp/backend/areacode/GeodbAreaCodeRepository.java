/**
 * @author Trung Kien Huynh
 */
package sp.backend.areacode;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GeodbAreaCodeRepository extends JpaRepository<GeodbAreacode, GeodbAreacodeId> {
    //    List<GeodbAreacode> findById(Integer id);
    @Query("""
            SELECT DISTINCT g.postcode FROM GeodbAreacode g
            WHERE g.postcode LIKE CONCAT(:postcode, '%')
            ORDER BY g.postcode ASC
            """)
    List<String> findDistinctByPostcodeStartingWithOrderByPostcodeAsc(String postcode, Pageable pageable);
}
