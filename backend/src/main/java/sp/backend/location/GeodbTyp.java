package sp.backend.location;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="geodb_typ")
public class GeodbTyp {
    @Id
    @Column(name="type")
    private int type;

    private String fld;

    private String name;

    protected GeodbTyp(){};

    public Integer getType() {
        return type;
    }

    public String getFld() {
        return fld;
    }

    public String getName() {
        return name;
    }

}
