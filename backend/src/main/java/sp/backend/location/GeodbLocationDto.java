/**
 * @author Trung Kien Huynh
 */
package sp.backend.location;

public class GeodbLocationDto {
    private Integer id;
    private String name;
    // Für die Spalte postcode in items. In geodb_locations kann ein Ort mehrere PLZs haben. Eine Anzeige soll nur eine PLZ haben.
    private String postcode;
    // Für die Spalte extraInfo in items.
    private String extraInfo;

    public GeodbLocationDto(GeodbLocation geodbLocation, String extraInfo) {
        this.id = geodbLocation.getId();
        this.name = geodbLocation.getName();
        this.postcode = geodbLocation.getPostcode();
        this.extraInfo = extraInfo;
    }

    public GeodbLocationDto(Integer id, String name, String postcode, String extraInfo) {
        this.id = id;
        this.name = name;
        this.postcode = postcode;
        this.extraInfo = extraInfo;
    }

    public Integer getId() {
        return id;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public String getName() {
        return name;
    }

    public String getPostcode() {
        return postcode;
    }
}