package sp.backend.location;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSources;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.multipart.MultipartFile;
import sp.backend.dto.*;
import sp.backend.exception.DeletedItemModificationNotAllowedException;
import sp.backend.exception.ExceptionMessage;
import sp.backend.exception.ImageSavingException;
import sp.backend.exception.ImageSizeExceededException;
import sp.backend.item_category.Category;
import sp.backend.item_category.CategoryRepository;
import sp.backend.item_image.ItemImageRepository;
import sp.backend.location.GeodbLocation;
import sp.backend.location.GeodbLocationRepository;
import sp.backend.user.User;
import sp.backend.user.UserRepository;
import sp.backend.websocket.DataChangePublisher;
import sp.backend.websocket.DataChangePublisher.Action;
import sp.backend.websocket.DataChangePublisher.Entity;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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
