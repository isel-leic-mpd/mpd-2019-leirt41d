import dto.DayInfo;
import dto.Location;
import dto.WeatherInfo;
import org.junit.Test;
import utils.FileRequest;


import static queries.lazy.QueriesLazy.*;
import static java.time.LocalDate.of;
import static org.junit.Assert.*;


public class WeatherTests {
    private  final double lisbonLat = 38.71667, lisbonLong = -9.13333;

    @Test
    public void filterTest() {

        final int expectedCount = 11;

        WeatherWebApi weather = new WeatherWebApi(new FileRequest());

        Iterable<WeatherInfo> past =
                weather.pastWeather(lisbonLat, lisbonLong,
                        of(2019, 1, 1),
                        of(2019, 2, 26),
                        24);

        int res = count(
                    filter(past,
                        wi -> wi.getDescription().contains("Sunny")
                    )
                  );

        assertEquals(expectedCount, res);
    }

    @Test
    public void testMap() {

        final double expectedCount = 2.0;

        WeatherWebApi weather = new WeatherWebApi(new FileRequest());

        Iterable<WeatherInfo> past =
                weather.pastWeather(lisbonLat, lisbonLong,
                        of(2019, 1, 1),
                        of(2019, 2, 26),
                        24);
        double res = sumDouble(
             map(
                     filter(past, wi-> wi.getTempC() > 15),
                     wi-> wi.getPrecipMM()
             )
        );


        assertEquals(expectedCount, res,0.001);

    }

    @Test
    public void testFilterMapMax() {

        final int expectedMax = 18;

        WeatherWebApi weather = new WeatherWebApi(new FileRequest());

        Iterable<WeatherInfo> past =
                weather.pastWeather(lisbonLat, lisbonLong,
                        of(2019, 1, 1),
                        of(2019, 2, 26),
                        24);


        /*
        You must understand that this commented code is equivalent to the used code below!

        Iterable<WeatherInfo> rainy = filter(past, wi-> wi.getPrecipMM() > 0.0);
        Iterable<Integer> temps = map(rainy,  wi-> wi.getTempC());
        int res = reduce(temps,   Integer.MIN_VALUE,  (acc, val) -> (val > acc) ? val : acc);
        */

        int res =
            reduce(
                map(
                    filter(past, wi-> wi.getPrecipMM() > 0.0),
                    wi-> wi.getTempC()
                ),
                Integer.MIN_VALUE,
                (acc, val) -> (val > acc) ? val : acc
            );

        assertEquals(expectedMax, res);

    }




    @Test
    public void pastDaysAtLisbonFrom2019Jan1Till2019Feb26Test() {

        final int expectedCount = 57;

        WeatherWebApi weather = new WeatherWebApi(new FileRequest());

        Iterable<DayInfo> past =
                weather.pastDays(lisbonLat, lisbonLong,
                        of(2019, 1, 1),
                        of(2019, 2, 26));
        int count=0;
        for(DayInfo di : past) {
            System.out.println(di);
            count++;
        }

        assertEquals(expectedCount, count);
    }

    @Test
    public void retrieveLocationsNamedLisbonTest() {

        final int expectedCount = 2;

        WeatherWebApi weather = new WeatherWebApi( new FileRequest());
        Iterable<Location> locations = weather.search("Lisbon");
        int count=0;
        for(Location loc : locations) {
            System.out.println(loc);
            count++;
        }

        assertEquals(expectedCount, count);
    }

}
