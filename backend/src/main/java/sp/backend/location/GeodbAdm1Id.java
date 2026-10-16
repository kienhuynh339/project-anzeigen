package sp.backend.location;

import java.io.Serializable;
import java.util.Objects;

public class GeodbAdm1Id implements Serializable {
    private String adm0;
    private String adm1;

    // erforderlich für @IdClass
    public GeodbAdm1Id() {
    }

    public GeodbAdm1Id(String adm0, String adm1) {
        this.adm0 = adm0;
        this.adm1 = adm1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GeodbAdm1Id that)) return false;

        return Objects.equals(adm0, that.adm0)
                && Objects.equals(adm1, that.adm1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(adm0, adm1);
    }
}
