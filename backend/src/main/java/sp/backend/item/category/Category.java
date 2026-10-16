package sp.backend.item.category;

import jakarta.persistence.*;

@Entity
@Table(name="item_categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}
