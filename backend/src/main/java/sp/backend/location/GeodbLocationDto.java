/**
 * @author Trung Kien Huynh
 */
package sp.backend.location;

public class GeodbLocationDto {
    private Integer id;
    private String name;
    private String plz;
    private String extraInfo; // für die Spalte extraInfo in items.

    public GeodbLocationDto(GeodbLocationProjection geodbLocation, String extraInfo){
        this.id=geodbLocation.getId();this.name=geodbLocation.getName();this.plz=geodbLocation.getPlz();this.extraInfo=extraInfo;
    }
    public GeodbLocationDto(GeodbLocation geodbLocation, String extraInfo){
        this.id=geodbLocation.getId();this.name=geodbLocation.getName();this.plz=geodbLocation.getPlz();this.extraInfo=extraInfo;
    }
    public GeodbLocationDto(Integer id, String name, String plz, String extraInfo){
        this.id=id;this.name=name;this.plz=plz;this.extraInfo=extraInfo;
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

    public String getPlz() {
        return plz;
    }
}