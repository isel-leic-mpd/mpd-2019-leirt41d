import dto.DayInfoDto;
import dto.LocationDto;
import dto.WeatherInfoDto;
import utils.AsyncRequest;
import utils.myiterators.MyIterable;
import utils.sequences.Sequence;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static utils.iterators.Queries.*;

public class WeatherWebApi {
    private static final String API_KEY;
    private static final String WEATHER_SERVICE =  "http://api.worldweatheronline.com/premium/v1/";
    private static final String WEATHER_PAST_TEMPLATE =
            "past-weather.ashx?q=%s&date=%s&enddate=%s&tp=%d&format=csv&key=%s";
    private static final String WEATHER_SEARCH_TEMPLATE = "search.ashx?query=%s&format=tab&key=%s";
    private final AsyncRequest req;

    private static String getApiKeyFromResources() {
        try {
            URL keyFile = ClassLoader.getSystemResource("worldweatheronline-app-key.txt");
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(keyFile.openStream()))) {
                return reader.readLine();
            }

        }
        catch(IOException e) {
            throw new IllegalStateException(
                    "YOU MUST GET a KEY from developer.worldweatheronline.com and place it in src/main/resources/worldweatheronline-app-key.txt");
        }
    }

    static {
        API_KEY = getApiKeyFromResources();
    }

    public WeatherWebApi(AsyncRequest req) {
        this.req = req;
    }



    public CompletableFuture<Stream<WeatherInfoDto>>
    pastWeather(double latitude, double longitude, LocalDate from, LocalDate to, int period) {
        String query = latitude + "," + longitude;
        String path =  WEATHER_SERVICE + String.format(WEATHER_PAST_TEMPLATE, query, from, to, period,API_KEY);
        CompletableFuture<Stream<String>> src =  req.getContent(path);

        return src
                .thenApply( s ->
                    s.dropWhile(str -> str.startsWith("#"))
                    .skip(1)
                    .filter(str->
                            str.contains("worldweatheronline"))
                    .map(str->WeatherInfoDto.valueOf(str) )
                );
    }

    /*
    public Stream<DayInfoDto> pastDays(double latitude, double longitude, LocalDate from, LocalDate to) {
        String query = latitude + "," + longitude;
        String path =  WEATHER_SERVICE + String.format(WEATHER_PAST_TEMPLATE, query, from, to, 24, API_KEY);

        Stream<String> src =  req.getContent(path);

        return src
                .dropWhile(str -> str.startsWith("#"))
                .skip(1)
                .filter(str-> !str.contains("worldweatheronline"))
                .map(str->DayInfoDto.valueOf(str) );
    }

    public Stream<LocationDto> search(String location) {

        String path =  WEATHER_SERVICE + String.format(WEATHER_SEARCH_TEMPLATE, location, API_KEY);
        Stream<String> src =  req.getContent(path);

        return  src
                .dropWhile(str -> str.startsWith("#"))
                .map(str->LocationDto.valueOf(str) );
    }
    */
}
