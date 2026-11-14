/**
 * @author Trung Kien Huynh
 */
package sp.backend.location.areacode;

import jakarta.persistence.*;

@Entity
@Table(name = "geodb_areacodes")
@IdClass(GeodbAreacodeId.class)
public class GeodbAreacode {
    @Id
    private Integer id;

    @Id
    @Column(name = "plz")
    private String postcode;

    protected GeodbAreacode() {
    }

    public Integer getId() {
        return id;
    }

    public String getPostcode() {
        return postcode;
    }
}
