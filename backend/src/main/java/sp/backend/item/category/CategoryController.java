/**
 * @author Trung Kien Huynh
 */
package sp.backend.item.category;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryRepository categoryRepository;

    private CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    private ResponseEntity<List<Category>> findAll() {
        List<Category> categoryList = categoryRepository.findAll(Sort.by("name"));
        return ResponseEntity.ok(categoryList);
    }
}
