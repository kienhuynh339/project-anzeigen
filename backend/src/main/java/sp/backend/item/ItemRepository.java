/**
 * @author Trung Kien Huynh
 */
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
            i.category.id,
            new sp.backend.user.UserPublicDto(u),
            i.title,
            i.description,
            i.price,
            new sp.backend.location.GeodbLocationDto(l.id,l.name,i.postcode,i.locationExtraInfo),
            i.status,
            i.createdAt,
            CASE WHEN :loggedInUserId = ifavorite.user.id THEN true ELSE false END
            )
            FROM Item i
            JOIN i.poster u
            JOIN i.location l
            LEFT JOIN ItemFavorite ifavorite ON i.id = ifavorite.item.id AND :loggedInUserId = ifavorite.user.id
            WHERE :itemId = i.id
            """)
    ItemDto findItemById(Long itemId, Long loggedInUserId);


    @Query("""
            SELECT new sp.backend.item.ItemWithDistanceDto(
                i.id,
                i.title,
                i.description,
                i.price,
                new sp.backend.location.GeodbLocationDto(l.id,l.name,i.postcode,i.locationExtraInfo),
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
                END,
                CASE WHEN :loggedInUserId = ifavorite.user.id THEN true ELSE false END
            )
            FROM Item i
            JOIN i.location l
            LEFT JOIN ItemFavorite ifavorite ON i.id = ifavorite.item.id  AND :loggedInUserId = ifavorite.user.id
            WHERE (i.status = ItemStatus.ACTIVE)
            AND (:searchTerm IS NULL OR
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
            @Param("radiusKm") Integer radiusKm, Long loggedInUserId,
            Pageable pageable
    );

    @Query("""
            SELECT new sp.backend.item.ItemDto(
            i.id,
            i.category.id,
            new sp.backend.user.UserPublicDto(u),
            i.title,
            i.description,
            i.price,
            new sp.backend.location.GeodbLocationDto(l.id,l.name,i.postcode,i.locationExtraInfo),
            i.status,
            i.createdAt,
            CASE WHEN :loggedInUserId = ifavorite.user.id THEN true ELSE false END
            )
            FROM Item i
            JOIN i.poster u
            JOIN i.location l
            LEFT JOIN ItemFavorite ifavorite ON i.id = ifavorite.item.id  AND :loggedInUserId = ifavorite.user.id
            WHERE (:userId = u.id)
            AND (i.status = ItemStatus.ACTIVE OR (:userId = :loggedInUserId
                                                 AND i.status != ItemStatus.DELETED))
            """)
    Page<ItemDto> findByUserId(Long userId, Long loggedInUserId, Pageable pageable);



    @Query("""
SElECT new sp.backend.item.ItemDto(
            i.id,
            i.category.id,
            new sp.backend.user.UserPublicDto(u),
            i.title,
            i.description,
            i.price,
            new sp.backend.location.GeodbLocationDto(l.id,l.name,i.postcode,i.locationExtraInfo),
            i.status,
            i.createdAt,
            true
            )
FROM Item i
JOIN i.poster u
JOIN i.location l
JOIN ItemFavorite f ON i.id = f.item.id
WHERE f.user.id = :userId
""")
    Page<ItemDto> findFavoriteItemsByUserId(Long userId, Pageable pageable);

}