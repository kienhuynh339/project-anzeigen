package sp.backend.item.category;

import jakarta.persistence.*;

@Entity
@Table(name="item_categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    private String name;

    protected Category(){};

    public String getName(){
        return name;
    }

    public void setName(String name){this.name=name;}
}
