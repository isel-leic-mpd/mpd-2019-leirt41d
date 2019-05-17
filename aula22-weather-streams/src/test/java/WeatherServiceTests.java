import model.DayInfo;
import model.Location;
import model.WeatherInfo;
import org.junit.Assert;
import org.junit.Test;
import utils.FileRequest;
import utils.HttpRequest;
import utils.iterators.Queries;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static utils.EagerQueries.count;
import static utils.iterators.Queries.*;


public class WeatherServiceTests {

    @Test
    public void retrieveLocationsNamedLisbonTest() {

        final int expectedCount = 2;

        WeatherService weather =
                new WeatherService(new WeatherWebApi(new HttpRequest()));
        Stream<Location> locations =
                weather.search("Lisbon");
        long count= locations.count();

        assertEquals(expectedCount, count);
    }

    @Test
    public void getMoonPhaseOfDate() {
        WeatherService weather =
                new WeatherService(new WeatherWebApi(new HttpRequest()));

        Optional<Location> lisbon =
            weather.search("Lisbon")
            .filter(  l -> l.getCountry()
                            .equalsIgnoreCase("portugal"))
            .findFirst();

        assertTrue(lisbon.isPresent());

        LocalDate day = LocalDate.of(2019, 2, 12);

        Stream<DayInfo> days =
                lisbon.get().getDays(day,day);

        Optional<DayInfo> firstDay = days.findFirst();

        assertTrue(firstDay.isPresent());
        System.out.println(firstDay.get().getMoonPhase());
    }


    @Test
    public void getDays2and3Feb() {
        WeatherService weather = new WeatherService(new WeatherWebApi(new HttpRequest()));

        Optional<Location> lisbon =
                weather.search("Lisbon")
                .filter(  l ->
                            l.getCountry()
                            .equalsIgnoreCase("portugal"))
                .findFirst();

        assertTrue(lisbon.isPresent());
        LocalDate day1 = LocalDate.of(2019, 2, 2);
        LocalDate day2 = LocalDate.of(2019, 2, 3);
        Stream<DayInfo> days =
                lisbon.get().getDays(day1,day2);


        long temps =
                days.
                mapToLong(d -> {
                    return d.getTemperatures().count();

                })
               .sum();

        assertEquals(48, temps);

    }


    public static class PlaceAmplitude {
        private final String place;
        private final int amplitude;

        public PlaceAmplitude(String place, int amplitude) {
            this.place=place;
            this.amplitude = amplitude;
        }
        public String getPlace() { return place; }
        public int getAmplitude() { return amplitude; }

        public String toString() {
            return "{ place= " + place + ", amplitude = " + amplitude + " }";
        }

        @Override
        public boolean equals(Object o) {
            if (o.getClass() != PlaceAmplitude.class) return false;
            PlaceAmplitude other = (PlaceAmplitude) o;
            return place.equals(other.place) && amplitude == other.amplitude;
        }

        @Override
        public int hashCode() {
            return amplitude + place.length();
        }
    }

    @Test
    public void maxOfThermicAmplitudeAtPortugalPlacesOnMarch2019() {
        WeatherService weather =
                new WeatherService(new WeatherWebApi(new HttpRequest()));
        PlaceAmplitude[] expected = {
                new PlaceAmplitude("Lisbon", 16 ),
                new PlaceAmplitude("Oporto", 10 ),
                new PlaceAmplitude("Coimbra", 16),
                new PlaceAmplitude("Faro", 9)
        };
        List<String> names =
                List.of("Lisbon", "Oporto", "Coimbra", "Faro");

        Stream<Location> locs =
                names.stream()
                .map(n ->
                    weather.search(n)
                    .filter(l -> l.getCountry()
                                .equalsIgnoreCase("portugal"))
                    .findFirst()
                    .get()
                );


        PlaceAmplitude[] maxAmpliPlaces =
            locs.map(loc -> {

                LocalDate first = LocalDate.of(2019, 3, 1);
                LocalDate last = LocalDate.of(2019, 3, 20);
                DayInfo day  =
                        loc.getDays(first, last)
                        .reduce((d1, d2) -> {
                                int amp1 = d1.getMaxTemp() - d1.getMinTemp();
                                int amp2 = d2.getMaxTemp() - d2.getMinTemp();
                                return (amp1 >= amp2) ? d1 : d2;
                            }).get();
                return new PlaceAmplitude(loc.getName(),
                                day.getMaxTemp()-day.getMinTemp());
            })
            .toArray(sz -> new PlaceAmplitude[sz]);

        assertArrayEquals(expected, maxAmpliPlaces);

    }

}
