package sp.backend.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    // Standort des Items: "JOIN i.location l" = "JOIN GeodbLocation l ON i.id = l.id"
    // Ausgangsstandort: JOIN GeodbLocation o ON o.id = :locationId
    @Query("""
        SELECT new sp.backend.item.ItemWithDistance(i,6371.0 * 2 * ASIN(
                SQRT(
                    POWER(
                        SIN(RADIANS(l.breite - o.breite) / 2),
                        2
                    )
                    + COS(RADIANS(o.breite))
                    * COS(RADIANS(l.breite))
                    * POWER(
                        SIN(RADIANS(l.laenge - o.laenge) / 2),
                        2
                    )
                )
            ))
        FROM Item i
        JOIN i.location l
        JOIN GeodbLocation o ON o.id = :locationId
        WHERE
            6371.0 * 2 * ASIN(
                SQRT(
                    POWER(
                        SIN(RADIANS(l.breite - o.breite) / 2),
                        2
                    )
                    + COS(RADIANS(o.breite))
                    * COS(RADIANS(l.breite))
                    * POWER(
                        SIN(RADIANS(l.laenge - o.laenge) / 2),
                        2
                    )
                )
            ) <= :radiusKm
        """)
    List<ItemWithDistance> findWithinRadius(
            @Param("locationId") Integer locationId,
            @Param("radiusKm") Integer radiusKm
    );
}
