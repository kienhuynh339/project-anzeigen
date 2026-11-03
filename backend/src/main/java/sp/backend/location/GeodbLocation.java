/**
 * @author Trung Kien Huynh
 */
package sp.backend.location;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "geodb_locations")
public class GeodbLocation {
    @Id
    private Integer id;

    @Column(name = "typ")
    private Integer type;

    private String name;

    @Column(name = "adm1", columnDefinition = "char(2)")
    private String federalState;

    @Column(name = "breite")
    private Float latitude;

    @Column(name = "laenge")
    private Float longitude;

    @Column(name = "plz")
    private String postcode;

    protected GeodbLocation() {
    }

    public Integer getId() {
        return id;
    }

    public Integer getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getFederalState() {
        return federalState;
    }

    public Float getLatitude() {
        return latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public String getPostcode() {
        return postcode;
    }
}