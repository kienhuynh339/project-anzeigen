package sp.backend.item;

import jakarta.persistence.*;
import sp.backend.item.category.Category;
import sp.backend.user.User;

@Entity
@Table(name="items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name="poster_id", nullable = false)
    private User poster;

    @Column(name="title", length=100, nullable = false)
    private String title;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="category_id", nullable = false)
    private Category category;

    @Column(name="description")
    private String description;

    @Column(name="price", nullable = false)
    private int price = 0;


}



//location_id INTEGER REFERENCES geodb_locations(id) NOT NULL,
//status SMALLINT DEFAULT 1 NOT NULL,
//created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
//);


