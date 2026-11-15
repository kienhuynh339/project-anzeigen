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

    public GeodbLocation() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFederalState() {
        return federalState;
    }

    public void setFederalState(String federalState) {
        this.federalState = federalState;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }
}