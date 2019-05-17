import dto.DayInfoDto;
import dto.LocationDto;
import dto.WeatherInfoDto;
import org.junit.Assert;
import org.junit.Test;
import utils.FileRequest;
import utils.HttpRequest;
import utils.myiterators.MyIterable;
import utils.sequences.Sequence;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static java.time.LocalDate.of;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static utils.iterators.Queries.forEach;

public class WeatherTests {
    private  final double lisbonLat = 38.71667, lisbonLong = -9.13333;

    private static class DayTemps {
        private LocalDate day;
        private List<WeatherInfoDto> temps;


        private DayTemps(LocalDate day, List<WeatherInfoDto> temps) {
            this.day = day; this.temps =temps;
        }
    }
    @Test
    public void pastWheaterAtLisbonFrom2019Feb1Till2019Feb26Test() {

        final int expectedCount = 26;

        WeatherWebApi weather = new WeatherWebApi(new HttpRequest());

        Stream<WeatherInfoDto> past =
                weather.pastWeather(lisbonLat, lisbonLong,
                        of(2019, 2, 1),
                        of(2019, 2, 26),
                        24);
        long count = past.count();

        Assert.assertEquals(expectedCount, count);
    }


    @Test
    public void pastWheaterAtLisbonFrom2019Jan1Till2019Feb26HourlyTest() {

        final int expectedCount = 766;

        WeatherWebApi weather = new WeatherWebApi(new FileRequest());

        Stream<WeatherInfoDto> past =
                weather.pastWeather(lisbonLat, lisbonLong,
                        of(2019, 1, 1),
                        of(2019, 2, 26),
                        1);

        long count = past.count();

        Assert.assertEquals(expectedCount, count);
    }

    @Test
    public void pastDaysAtLisbonFrom2019Jan1Till2019Feb26Test() {

        final long expectedCount = 57;

        WeatherWebApi weather = new WeatherWebApi(new FileRequest());

        Stream<DayInfoDto> past =
                weather.pastDays(lisbonLat, lisbonLong,
                        of(2019, 1, 1),
                        of(2019, 2, 26));

        long count = past.count();

        Assert.assertEquals(expectedCount, count);
    }

    @Test
    public void retrieveLocationsNamedLisbonTest() {

        final int expectedCount = 2;

        WeatherWebApi weather = new WeatherWebApi(new HttpRequest());
        Stream<LocationDto> locations = weather.search("Lisbon");
        long count = locations.count();
        Assert.assertEquals(expectedCount, count);
    }

    @Test
    public void FeelsLikeNearTempTest() {
        WeatherWebApi weather = new WeatherWebApi(new FileRequest());

        Stream<WeatherInfoDto> past =
                weather.pastWeather(lisbonLat, lisbonLong,
                        of(2019, 1, 1),
                        of(2019, 2, 26),
                        1);

        Map<LocalDate, List<WeatherInfoDto>> daysMap  =
                past.collect(groupingBy(wi-> wi.getLocalDate()));
        List<DayTemps> days = new ArrayList<>();
        daysMap.entrySet().forEach(e -> days.add(new DayTemps(e.getKey(), e.getValue())) );
        Stream<LocalTime> dates = days.stream()
            .flatMap(d-> d.temps.stream())
            .filter(t -> (t.getTempC() -  t.getFeelsLikeC()) > 2)
            .map(t -> t.getLocalTime());

        dates.forEach(System.out::println);

    }

    @Test
    public void getDifferenceBetweenTempAndFeels() {
        WeatherWebApi weather = new WeatherWebApi(new FileRequest());
        Stream<Integer> past = weather
                .pastWeather(lisbonLat, lisbonLong,
                        of(2019, 1, 1),
                        of(2019, 2, 26),1)
                .map(wi -> wi.getTempC()-wi.getFeelsLikeC());


        past.forEach(System.out::println);

    }

}
