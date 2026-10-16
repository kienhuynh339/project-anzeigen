package sp.backend.location;

import jakarta.persistence.*;

@Entity
@Table(name="geodb_adm1")
@IdClass(GeodbAdm1Id.class)
public class GeodbAdm1 {

    @Id
    @Column(name="adm0", columnDefinition = "char(2)")
    private String adm0;

    @Id
    @Column(name="adm1", columnDefinition = "char(2)")
    private String adm1;

    @Column(columnDefinition = "char(2)")
    private String gs;

    private String name;

    protected GeodbAdm1(){}

    public String getAdm0() {
        return adm0;
    }

    public String getAdm1() {
        return adm1;
    }

    public String getGs() {
        return gs;
    }

    public String getName() {
        return name;
    }
}


