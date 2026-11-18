/**
 * @author Trung Kien Huynh
 */
package sp.backend.location;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import sp.backend.dto.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class GeodbLocationServiceTest {
    @Autowired
    private GeodbLocationService geodbLocationService;

    @MockitoBean
    private GeodbLocationRepository geodbLocationRepository;

    private final Pageable pageable = PageRequest.of(0, 10);

    // ---------------------------------------------------------
    // findByPostcode()
    // ---------------------------------------------------------

    @Nested
    class FindByPostcodeTests {
        private final String postcode = "11111";


        @Test
        void findByPostcode_shouldReturnGeodbLocationResponses() {
            GeodbLocation location1 = mock(GeodbLocation.class);
            GeodbLocation location2 = mock(GeodbLocation.class);

            when(location1.getId()).thenReturn(1);
            when(location2.getId()).thenReturn(2);
            when(location1.getPostcode()).thenReturn("11111");
            when(location2.getPostcode()).thenReturn("11111");
            when(location1.getType()).thenReturn(6);
            when(location2.getType()).thenReturn(7);
            when(location1.getFederalState()).thenReturn("BE");
            when(location2.getFederalState()).thenReturn("BE");

            List<GeodbLocation> locations = List.of(location1, location2);

            when(geodbLocationRepository.findByPostcodeOrderByNameAsc(postcode, pageable)).thenReturn(locations);

            List<GeodbLocationResponse> result = geodbLocationService.findByPostcode(postcode, pageable);

            assertNotNull(result);
            assertEquals(location1.getId(), result.get(0).getId());
            assertEquals(location2.getId(), result.get(1).getId());
        }
    }

}
