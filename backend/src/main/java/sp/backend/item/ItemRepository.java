package sp.backend.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("""
            SELECT new sp.backend.item.ItemDto(
            i.id,
            new sp.backend.user.UserPublicDto(u),
            i.title,
            i.description,
            i.price,
            new sp.backend.location.GeodbLocationDto(l.id,l.name,i.plz,i.locationExtraInfo),
            i.status,
            i.createdAt
            )
            FROM Item i
            JOIN i.poster u
            JOIN i.location l
            WHERE :id = i.id
            """)
    ItemDto findItemById(Long id);


    @Query("""
            SELECT new sp.backend.item.ItemWithDistanceDto(
                i.id,
                i.title,
                i.description,
                i.price,
                new sp.backend.location.GeodbLocationDto(l.id,l.name,i.plz,i.locationExtraInfo),
                i.createdAt,
                CASE
                    WHEN :latitude IS NULL OR :longitude IS NULL OR l.id = :locationId
                    THEN NULL
                    ELSE
                        6371.0 * 2 * ASIN(SQRT(
                            POWER(
                                SIN(RADIANS(l.breite - :latitude) / 2),
                                2
                            )
                            + COS(RADIANS(:latitude))
                            * COS(RADIANS(l.breite))
                            * POWER(
                                SIN(RADIANS(l.laenge - :longitude) / 2),
                                2
                            )
                        ))
                END
            )
            FROM Item i
            JOIN i.location l
            WHERE
                (:searchTerm IS NULL OR
                    LOWER(i.title) LIKE
                        LOWER(CONCAT('%', :searchTerm, '%'))
                    OR LOWER(i.description) LIKE
                        LOWER(CONCAT('%', :searchTerm, '%'))
                )
            AND (:categoryId IS NULL OR i.category.id = :categoryId OR :categoryId = 1)
            AND (:minPrice IS NULL OR i.price >= :minPrice)
            AND (:maxPrice IS NULL OR i.price <= :maxPrice)
            AND (:locationId IS NULL OR (l.id = :locationId AND :radiusKm IS NULL) OR (
                :radiusKm IS NOT NULL 
                AND :latitude IS NOT NULL 
                AND :longitude IS NOT NULL
                AND 6371.0 * 2 * ASIN(SQRT(
                        POWER(
                            SIN(RADIANS(l.breite - :latitude) / 2),
                            2
                        )
                        +
                        COS(RADIANS(:latitude))
                        * COS(RADIANS(l.breite))
                        * POWER(
                            SIN(RADIANS(l.laenge - :longitude) / 2),
                            2
                        )
                    )) <= :radiusKm) 
                )         
            """)
    Page<ItemWithDistanceDto> search(
            @Param("searchTerm") String searchTerm,
            @Param("categoryId") Short categoryId,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("locationId") Integer locationId,
            @Param("latitude") Float latitude,
            @Param("longitude") Float longitude,
            @Param("radiusKm") Integer radiusKm,
            Pageable pageable
    );

    @Query("""
            SELECT new sp.backend.item.ItemDto(
            i.id,
            new sp.backend.user.UserPublicDto(u),
            i.title,
            i.description,
            i.price,
            new sp.backend.location.GeodbLocationDto(l.id,l.name,i.plz,i.locationExtraInfo),
            i.status,
            i.createdAt
            )
            FROM Item i
            JOIN i.poster u
            JOIN i.location l
            WHERE :id = u.id
            """)
    Page<ItemDto> findByUserId(Long id, Pageable pageable);
}