/**
 * @author Trung Kien Huynh
 */
package sp.backend.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sp.backend.dto.ItemResponse;
import sp.backend.dto.ItemWithDistanceResponse;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("""
            SELECT new sp.backend.dto.ItemResponse(
            i.id,
            i.category.id,
            new sp.backend.dto.UserPublicResponse(u),
            i.title,
            i.description,
            i.price,
            new sp.backend.dto.GeodbLocationResponse(l.id,l.name,i.postcode,i.locationExtraInfo),
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
    Optional<ItemResponse> findItemById(Long itemId, Long loggedInUserId);


    @Query("""
            SELECT new sp.backend.dto.ItemWithDistanceResponse(
                i.id,
                i.poster.id,
                i.title,
                i.description,
                i.price,
                new sp.backend.dto.GeodbLocationResponse(l.id,l.name,i.postcode,i.locationExtraInfo),
                i.createdAt,
                CASE
                    WHEN :latitude IS NULL OR :longitude IS NULL OR l.id = :locationId
                    THEN NULL
                    ELSE
                        6371.0 * 2 * ASIN(SQRT(
                            POWER(
                                SIN(RADIANS(l.latitude - :latitude) / 2),
                                2
                            )
                            + COS(RADIANS(:latitude))
                            * COS(RADIANS(l.latitude))
                            * POWER(
                                SIN(RADIANS(l.longitude - :longitude) / 2),
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
                            SIN(RADIANS(l.latitude - :latitude) / 2),
                            2
                        )
                        +
                        COS(RADIANS(:latitude))
                        * COS(RADIANS(l.latitude))
                        * POWER(
                            SIN(RADIANS(l.longitude - :longitude) / 2),
                            2
                        )
                    )) <= :radiusKm)
                )
            """)
    Page<ItemWithDistanceResponse> search(
            String searchTerm,
            Short categoryId,
            Integer minPrice,
            Integer maxPrice,
            Integer locationId,
            Float latitude,
            Float longitude,
            Integer radiusKm,
            Long loggedInUserId,
            Pageable pageable
    );

    @Query("""
            SELECT new sp.backend.dto.ItemResponse(
            i.id,
            i.category.id,
            new sp.backend.dto.UserPublicResponse(u),
            i.title,
            i.description,
            i.price,
            new sp.backend.dto.GeodbLocationResponse(l.id,l.name,i.postcode,i.locationExtraInfo),
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
    Page<ItemResponse> findByUserId(Long userId, Long loggedInUserId, Pageable pageable);


    @Query("""
            SElECT new sp.backend.dto.ItemResponse(
                        i.id,
                        i.category.id,
                        new sp.backend.dto.UserPublicResponse(u),
                        i.title,
                        i.description,
                        i.price,
                        new sp.backend.dto.GeodbLocationResponse(l.id,l.name,i.postcode,i.locationExtraInfo),
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
    Page<ItemResponse> findFavoriteItemsByUserId(Long userId, Pageable pageable);

}