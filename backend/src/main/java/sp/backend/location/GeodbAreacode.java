package sp.backend.location;

import jakarta.persistence.*;

@Entity
@Table(name="geodb_areacodes")
@IdClass(GeodbAreacodeId.class)
public class GeodbAreacode {
    @Id
    private Integer id;

    @Id
    @Column(name="plz")
    private String plz;

    protected GeodbAreacode() {    }

    public Integer getId() {
        return id;
    }

    public String getPlz() {
        return plz;
    }
}
