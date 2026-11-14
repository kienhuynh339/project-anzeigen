/**
 * @author Trung Kien Huynh
 */
package sp.backend.item_category;

import jakarta.persistence.*;

@Entity
@Table(name = "item_categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    private String name;

    protected Category() {
    }

    ;

    public Short getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
