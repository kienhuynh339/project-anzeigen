package sp.backend.location;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="geodb_locations")
public class GeodbLocation {
    @Id
    private Integer id;

    private Integer typ;

    private String name;

    @Column(name="name_int")
    private String nameInt;

    private String gs;

    @Column(columnDefinition = "char(2)")
    private String adm0;

    @Column(columnDefinition = "char(2)")
    private String adm1;

    private String adm2;

    private String adm3;

    private String adm4;

    private String ort;

    private String ortsteil;

    private String gemteil;

    private String wohnplatz;

    private Float breite;

    private Float laenge;

    @Column(columnDefinition = "char(3)")
    private String kfz;

    private String plz;

    protected GeodbLocation(){}

    public Integer getId() {
        return id;
    }

    public Integer getTyp() {
        return typ;
    }

    public String getName() {
        return name;
    }

    public String getNameInt() {
        return nameInt;
    }

    public String getGs() {
        return gs;
    }

    public String getAdm0() {
        return adm0;
    }

    public String getAdm1() {
        return adm1;
    }

    public String getAdm2() {
        return adm2;
    }

    public String getAdm3() {
        return adm3;
    }

    public String getAdm4() {
        return adm4;
    }

    public String getOrt() {
        return ort;
    }

    public String getOrtsteil() {
        return ortsteil;
    }

    public String getGemteil() {
        return gemteil;
    }

    public String getWohnplatz() {
        return wohnplatz;
    }

    public Float getBreite() {
        return breite;
    }

    public Float getLaenge() {
        return laenge;
    }

    public String getKfz() {
        return kfz;
    }

    public String getPlz() {
        return plz;
    }

}