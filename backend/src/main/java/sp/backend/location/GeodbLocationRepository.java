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
SELECT g.id AS id, g.typ AS typ, g.name AS name, g.plz AS plz, g.federalState as federalState
FROM GeodbLocation g
WHERE CONCAT(',', g.plz, ',') LIKE CONCAT('%,', :plz, ',%')
ORDER BY g.name ASC
""")
    List<GeodbLocationProjection> findByPlzOrderByNameAsc(String plz, Pageable pageable);
}
