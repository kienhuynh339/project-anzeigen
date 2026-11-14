/**
 * @author Trung Kien Huynh
 */
package sp.backend.location_areacode;

import java.io.Serializable;
import java.util.Objects;

public class GeodbAreacodeId implements Serializable {
    private Integer id;
    private String postcode;

    GeodbAreacodeId() {
    }

    ;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GeodbAreacodeId that)) return false;
        return Objects.equals(id, that.id)
                && Objects.equals(postcode, that.postcode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, postcode);
    }
}
