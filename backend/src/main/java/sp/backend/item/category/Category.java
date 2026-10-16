package sp.backend.item.category;

import jakarta.persistence.*;

@Entity
@Table(name="item_categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name", length = 100, nullable = false)
    private String name;
}
