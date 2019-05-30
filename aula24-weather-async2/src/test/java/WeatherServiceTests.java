
import model.DayInfo;
import model.Location;
import org.junit.Test;
import utils.HttpRequest;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static java.time.LocalDate.of;
import static org.junit.Assert.assertEquals;


public class WeatherServiceTests {

    @Test
    public void retrieveLocationsNamedLisbonTest() {

        final int expectedCount = 2;

        WeatherService weather =
                new WeatherService(new WeatherWebApi(new HttpRequest()));
        CompletableFuture<Stream<Location>> locations =
                weather.search("Lisbon");

        long count= locations.join().count();

        assertEquals(expectedCount, count);
    }

    @Test
    public void getMoonPhaseOfDate() {
        WeatherService weather =
                new WeatherService(new WeatherWebApi(new HttpRequest()));

        Optional<Location> lisbon =
            weather.search("Lisbon")
            .thenApply( s -> s.filter(  l -> l.getCountry()
                        .equalsIgnoreCase("portugal"))
                        .findFirst()).join();

        assertEquals(true, lisbon.isPresent());

        LocalDate day = LocalDate.of(2019, 2, 12);

        Stream<DayInfo> days =
                lisbon.get()
                .getDays(day,day)
                .join();

        Optional<DayInfo> firstDay = days.findFirst();

        assertEquals(true, firstDay.isPresent());
        System.out.println(firstDay.get().getMoonPhase());
    }


    @Test
    public void getDays2and3Feb() {
        WeatherService weather = new WeatherService(new WeatherWebApi(new HttpRequest()));

        LocalDate day1 = of(2019, 2, 2);
        LocalDate day2 = of(2019, 2, 3);

        CompletableFuture<Stream<DayInfo>> days =
                weather.search("Lisbon")
                .thenApply(s ->
                    s.filter( l ->
                                l.getCountry()
                                .equalsIgnoreCase("portugal"))
                            .findFirst()
                            .get()
                )
                .thenCompose(loc -> loc.getDays(day1, day2));


        long observationsCount =
            days.thenCompose(s ->
                s.map(d -> d.getTemperatures()
                    .thenApply(s1 -> s1.count())
                )
                .reduce((f1,f2) -> f1.thenCombine(f1, (l1,l2) -> l1+l2))
                .get()
            )
            .join();
            assertEquals(48, observationsCount);

    }


    /**
     * Retorna um CompletableFuture cujo valor é a Location
     * de nome locationName no país de nome country
     * Assume-se que a location existe
     * @param service
     * @param locationName
     * @param country
     * @return
     */
    private CompletableFuture<Location> getLocationFromName(
            WeatherService service, String locationName, String country) {
        return null;
    }

    /**
     * Retorna o CompletableFuture cujo valor é a
     * observação meteorológica diária obtida
     * da Location "location" na data "date"
     * @param location
     * @param date
     * @return
     */
    private CompletableFuture<DayInfo>
            getDayInfoFromLocation(Location location, LocalDate date) {

        return null;
    }

    /**
     *  Retorna o CompletableFuture cujo valor é a
     *  máxima temperatura na observação meteorológica diária obtida
     *  da Location  de nome "locationName"  do país "country" na data "date"
     * @param service
     * @param locationName
     * @param country
     * @param date
     * @return
     */
    private CompletableFuture<Integer> getMaxTempFromLocationNameAtDay(
            WeatherService service, String locationName,
            String country, LocalDate date) {
        return null;
    }

    /**
     * Retorna o CompletableFuture cujo valor é a máxima temperatura
     * de um conjunto de localidades dentro de um país numa dada data
     * @param service
     * @param names
     * @param country
     * @param date
     * @return
     */
    private CompletableFuture<Integer>
    getMaxTempOfSelectedCountryLocationsAtaDay(
            WeatherService service, List<String> names, String country,
            LocalDate date) {
        return null;
    }


    @Test
    public void maxTempFromSelectedLocationAtaDayTest() {
        WeatherService service = new WeatherService(
                new WeatherWebApi(new HttpRequest()));

        List<String> names = List.of("Lisbon", "Oporto",
                "Coimbra", "Faro");

        long start = System.currentTimeMillis();
        int[] maxTemp = {0};
        getMaxTempOfSelectedCountryLocationsAtaDay(
                service,names,"portugal",
                LocalDate.of(2019,5,12))
                .thenAccept(temp -> maxTemp[0] = temp)
                .join();
        long end = System.currentTimeMillis();
        System.out.println(maxTemp[0] + ":done at " + (end-start) + "ms!");

    }

}
