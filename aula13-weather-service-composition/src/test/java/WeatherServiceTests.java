import model.DayInfo;
import model.Location;
import org.junit.Test;
import queries.lazy.Queries;
import utils.FileRequest;
import utils.HttpRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static queries.naive.EagerQueries.count;
import static queries.lazy.Queries.*;


public class WeatherServiceTests {

    @Test
    public void retrieveLocationsNamedLisbonTest() {

        final int expectedCount = 2;

        WeatherService weather = new WeatherService(new WeatherWebApi(new HttpRequest()));
        Iterable<Location> locations = weather.search("Lisbon");
        int count=0;
        for(Location loc : locations) {
            System.out.println(loc);
            count++;
        }

        assertEquals(expectedCount, count);
    }

    @Test
    public void getMoonPhaseOfDateAtLisbon2019_02_12() {
        String expectedPhase = "First Quarter";
        fail();
        // To implement
    }

    @Test
    public void getDays2and3Feb() {
        WeatherService weather =
                new WeatherService(new WeatherWebApi(new HttpRequest()));

        Optional<Location> lisbon =
            first(
                filter(
                    weather.search("Lisbon"),
                    l -> l.getCountry()
                            .equalsIgnoreCase("portugal")
                )
            );

        assertTrue(lisbon.isPresent());
        LocalDate day1 = LocalDate.of(2019, 2, 2);
        LocalDate day2 = LocalDate.of(2019, 2, 3);

        Iterable<DayInfo> days = lisbon.get().getDays(day1,day2);


        long count =
            count(
                flatMap( days,
                    d -> d.getTemperatures()
                )
            );

        assertEquals(48, count);
    }


    @Test
    public void maxThermicAmplitudeAtLisbonInMarch2019() {
        LocalDate expectedDate = LocalDate.of(2019,3,15);
        WeatherService weather =
                new WeatherService(new WeatherWebApi(new HttpRequest()));

        Optional<Location> lisbonOpt =
            first(
                filter(
                    weather.search("Lisbon"),
                    l -> l.getCountry()
                            .equalsIgnoreCase("portugal")
                )
            );

        assertTrue(lisbonOpt.isPresent());
        Location lisbon = lisbonOpt.get();
        LocalDate first = LocalDate.of(2019,3,1);
        LocalDate last = LocalDate.of(2019,3,20);


        Optional<DayInfo> maxThermalAmplitude=null;


        /* use aqui a operação reduce realizada anteriormente para
           obter o dia com a amplitude térmica máxima
        */

        Optional<DayInfo> maxTemp = Queries.reduce(
            lisbon.getDays(first,last),
            (DayInfo d1,DayInfo d2) ->  {
                if (d1.getMaxTemp() > d2.getMaxTemp()) return d1;
                return d2;
            });

        assertTrue(maxThermalAmplitude.isPresent());
        DayInfo max = maxThermalAmplitude.get();

        assertEquals(expectedDate, max.getDate());

    }

}
