/**
 * @author Trung Kien Huynh
 */
package sp.backend.item_category;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Short> {
}
