package sp.backend.location;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="geodb_adm0")
public class GeodbAdm0 {
    @Id
    @Column(name="adm0")
    private String adm0;

    private String name;

    protected GeodbAdm0(){};

    public String getAdm0() {
        return adm0;
    }

    public String getName() {
        return name;
    }
}
