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
    @JoinColumn(name="poster_id")
    private User poster;

    private String title;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="category_id")
    private Category category;

    private String description;

    private int price = 0;


}



//location_id INTEGER REFERENCES geodb_locations(id) NOT NULL,
//status SMALLINT DEFAULT 1 NOT NULL,
//created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
//);


