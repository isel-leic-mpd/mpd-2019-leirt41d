import dto.DayInfoDto;
import dto.LocationDto;
import dto.WeatherInfoDto;
import model.DayInfo;
import model.Location;
import org.junit.Assert;
import org.junit.Test;
import utils.FileRequest;
import utils.HttpRequest;
import utils.myiterators.MyIterable;
import utils.sequences.Sequence;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
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

        long[] count = {0};

        CompletableFuture<Void> past =
                weather
                .pastWeather(lisbonLat, lisbonLong,
                        of(2019, 2, 1),
                        of(2019, 2, 26),
                        24)
                .thenAccept(s -> count[0] = s.count());

        past.join(); // wait for CompletableFuture completion
        Assert.assertEquals(expectedCount, count[0]);
    }

    /*

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
    */
    @Test
    public void pastDaysAtLisbonFrom2019Jan1Till2019Feb26Test() {

        final long expectedCount = 57;

        WeatherWebApi weather = new WeatherWebApi(new FileRequest());

        CompletableFuture<Stream<DayInfoDto>> past =
                weather.pastDays(lisbonLat, lisbonLong,
                        of(2019, 1, 1),
                        of(2019, 2, 26));

        long[] count = {0};
        past
        .thenAccept( s -> count[0] = s.count())
        .join();


        Assert.assertEquals(expectedCount, count[0]);
    }

    @Test
    public void maxTempFromSelectedLocationAtaDay() {
        WeatherWebApi weather = new WeatherWebApi(
                new HttpRequest());

        String[] names = {"Lisbon", "Oporto",
                "Coimbra", "Faro"
        };

        Stream<CompletableFuture<LocationDto>>
                futLocs = Stream.of(names)
                .map(n -> weather.search(n))
                .map( f -> f.thenApply(s->
                        s.filter(
                                l-> l.getCountry().equalsIgnoreCase("portugal")
                        )

                ))
                .map( f -> f.thenApply( s -> s.findFirst().get()));


        Stream<CompletableFuture<DayInfoDto>> futDays = futLocs
                .map(f -> f.thenCompose( l ->
                        weather.pastDays(l.getLatitude(), l.getLongitude(),
                                of(2019, 5, 12),
                                of(2019, 5, 12)))
                        .thenApply( s -> s.findFirst().get()));

        CompletableFuture<DayInfoDto>[] futDaysArray =
                futDays.toArray(sz -> new CompletableFuture[sz]);

        int[] maxDayTemp = {0};

        CompletableFuture
                .allOf(futDaysArray)
                .thenAccept(__ -> {
                    int m =  Stream.of(futDaysArray)
                            .mapToInt(f -> f.join().getMaxTemp())
                            .max()
                            .getAsInt();
                    maxDayTemp[0] = m;
                }).join();

        System.out.println(maxDayTemp[0]);
    }

    private CompletableFuture<LocationDto> getLocationFromName(
            WeatherWebApi weather, String locationName, String country) {
        return weather.search(locationName)
                .thenApply( s-> s.filter(l -> l.getCountry().equalsIgnoreCase(country))
                                .findFirst().get());
    }

    private CompletableFuture<DayInfoDto> getDayInfoFromLocation(
            WeatherWebApi weather, LocationDto location, LocalDate date) {
        return weather
                .pastDays(location.getLatitude(), location.getLongitude(),
                date, date)
                .thenApply(s -> s.findFirst().get());
    }

    private CompletableFuture<Integer> getMaxTempFromLocationNameAtDay(
            WeatherWebApi weather, String locationName, String country, LocalDate date) {
        return getLocationFromName(weather, locationName, country)
                .thenCompose( l -> getDayInfoFromLocation(weather, l, date))
                .thenApply( d -> d.getMaxTemp());
    }

    private CompletableFuture<Integer> getMaxTempOfSelectedCountryLocationsAtaDay(
            WeatherWebApi weather, List<String> names, String country, LocalDate date) {
        return names.stream()
                .map(n -> getMaxTempFromLocationNameAtDay(weather, n, country, date))
                .reduce( (f1,f2) -> f1.thenCombine(f2, (m1, m2 ) -> (m1 > m2) ? m1 : m2))
                .get();
    }


    private CompletableFuture<Integer> getMaxTempOfSelectedCountryLocationsAtaDay2(
            WeatherWebApi weather, List<String> names, String country, LocalDate date) {
        CompletableFuture<Integer> maxTemps[] =  names.stream()
                .map(n -> getMaxTempFromLocationNameAtDay(weather, n, country, date))
                .toArray(sz -> new CompletableFuture[sz]);
        return CompletableFuture.allOf(maxTemps)
                .thenApply(__ -> {
                    return Stream.of(maxTemps)
                    .mapToInt(f -> f.join())
                    .max()
                    .getAsInt();
                });

    }



    @Test
    public void maxTempFromSelectedLocationAtaDayTest2() {
        WeatherWebApi weather = new WeatherWebApi(
                new HttpRequest());

        String[] names = {"Lisbon", "Oporto",
                "Coimbra", "Faro"
        };

        Stream<CompletableFuture<LocationDto>>
                futLocs = Stream.of(names)
                .map(n -> weather.search(n))
                .map( f -> f.thenApply(s->
                        s.filter(
                                l-> l.getCountry().equalsIgnoreCase("portugal")
                        )

                ))
                .map( f -> f.thenApply( s -> s.findFirst().get()));


        Stream<CompletableFuture<DayInfoDto>> futDays = futLocs
                .map(f -> f.thenCompose( l ->
                        weather.pastDays(l.getLatitude(), l.getLongitude(),
                                of(2019, 5, 12),
                                of(2019, 5, 12)))
                        .thenApply( s -> s.findFirst().get()));

        CompletableFuture<DayInfoDto>[] futDaysArray =
                futDays.toArray(sz -> new CompletableFuture[sz]);

        int[] maxDayTemp = {0};

        CompletableFuture
                .allOf(futDaysArray)
                .thenAccept(__ -> {
                    int m =  Stream.of(futDaysArray)
                            .mapToInt(f -> f.join().getMaxTemp())
                            .max()
                            .getAsInt();
                    maxDayTemp[0] = m;
                }).join();

        System.out.println(maxDayTemp[0]);
    }

    /*

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
    */
}
