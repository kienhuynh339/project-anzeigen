/**
 * @author Trung Kien Huynh
 */
package sp.backend.location;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GeodbLocationRepository extends JpaRepository<GeodbLocation, Integer> {
    @Query("""
            SELECT g
            FROM GeodbLocation g
            WHERE CONCAT(',', g.postcode, ',') LIKE CONCAT('%,', :postcode, ',%')
            ORDER BY g.name ASC
            """)
    List<GeodbLocation> findByPostcodeOrderByNameAsc(String postcode, Pageable pageable);
}
