package sp.backend.location;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class GeodbLocationService {
    private final GeodbLocationRepository geodbLocationRepository;

    private static final Map<Integer, String> TYPES = Map.ofEntries(
            Map.entry(1, "Staat"),
            Map.entry(2, "Bundesland / Kanton"),
            Map.entry(3, "Regierungsbezirk / Bezirk"),
            Map.entry(4, "Landkreis"),
            Map.entry(5, "Verwaltungszusammenschluss"),
            Map.entry(6, "Stadt / Gemeinde"),
            Map.entry(7, "Orts- / Stadtteil"),
            Map.entry(8, "unspezifizierter Gemeindeteil"),
            Map.entry(9, "Wohnplatz")
    );

    private static final Map<String, String> FEDERALSTATES = Map.ofEntries(
            Map.entry("HB", "Bremen"),
            Map.entry("BW", "Baden-Württemberg"),
            Map.entry("HE", "Hessen"),
            Map.entry("SL", "Saarland"),
            Map.entry("BB", "Brandenburg"),
            Map.entry("HH", "Hamburg"),
            Map.entry("BY", "Bayern"),
            Map.entry("NW", "Nordrhein-Westfalen"),
            Map.entry("ST", "Sachsen-Anhalt"),
            Map.entry("TH", "Thüringen"),
            Map.entry("BE", "Berlin"),
            Map.entry("RP", "Rheinland-Pfalz"),
            Map.entry("NI", "Niedersachsen"),
            Map.entry("MV", "Mecklenburg-Vorpommern"),
            Map.entry("SN", "Sachsen"),
            Map.entry("SH", "Schleswig-Holstein")
    );

    public GeodbLocationService(GeodbLocationRepository geodbLocationRepository) {
        this.geodbLocationRepository = geodbLocationRepository;
    }

    public List<GeodbLocationDto> findByPlz(String plz, Pageable pageable) {
        List<GeodbLocationProjection> locations = geodbLocationRepository.findByPlzOrderByNameAsc(plz, pageable);

        // Anzahl der Treffer pro Kombination aus Name, PLZ und Bundesland ermitteln.
        Map<String, Long> counts = locations.stream()
                .flatMap(location -> Arrays.stream(location.getPlz().split(","))
                        .map(String::trim)
                        .map(singlePlz -> key(location.getName(), singlePlz, location.getFederalState())))
                .collect(Collectors.groupingBy(key -> key
                        ,
                        Collectors.counting()
                ));

        return locations.stream()
                .map(location -> {
                    boolean hasDuplicate = Arrays.stream(location.getPlz().split(","))
                            .map(String::trim)
                            .map(singlePlz -> key(location.getName(), singlePlz, location.getFederalState()))
                            .anyMatch(key -> counts.getOrDefault(key, 0L) > 1);

                    String extraInfo = "";

                    if (hasDuplicate) {
                        extraInfo = "(" + TYPES.get(location.getTyp()) + ")" + " - " + FEDERALSTATES.get(location.getFederalState());
                    }

                    return new GeodbLocationDto(location,extraInfo);     })
                .toList();
    }

    private String key(String name, String plz, String federalState) {
        return Objects.toString(name, "") + "|" + Objects.toString(plz, "")+ "|" + Objects.toString(federalState, "");
    }
}
