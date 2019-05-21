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
    /*
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
    */

}
