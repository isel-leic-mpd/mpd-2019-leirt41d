import com.google.gson.Gson;
import dto.DayInfoDto;
import dto.LocationDto;
import dto.SearchApiDto;
import dto.WeatherInfoDto;
import utils.IRequest;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static queries.lazy.Queries.*;

public class WeatherWebApi {
    private static final String API_KEY;
    private static final String WEATHER_SERVICE =  "http://api.worldweatheronline.com/premium/v1/";
    private static final String WEATHER_PAST_TEMPLATE =
            "past-weather.ashx?q=%s&date=%s&enddate=%s&tp=%d&format=csv&key=%s";
    private static final String WEATHER_SEARCH_TEMPLATE = "search.ashx?query=%s&format=json&key=%s";
    private final IRequest req;

    protected final Gson gson;

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

    public WeatherWebApi(IRequest req) {

        this.req = req;
        gson = new Gson();
    }

    public Iterable<WeatherInfoDto> pastWeather(double latitude, double longitude, LocalDate from, LocalDate to, int period) {
        String query = latitude + "," + longitude;
        String path =  WEATHER_SERVICE + String.format(WEATHER_PAST_TEMPLATE, query, from, to, period,API_KEY);


        List<WeatherInfoDto> result = new ArrayList<>(); // where the dto.WeatherInfo instances are collected


        /*
        // used to do the HTTP request to worldweatheronline service
        URL url = new URL(path);

        // the stream used to get the response to the service request
        Iterator<String> it = getLines(url.openStream()).iterator();
        */
        /*
        Iterator<String> it = req.getContent(path).iterator();
        while(it.hasNext() &&  it.next().startsWith("#")) {}

        boolean toRead= false;
        while(it.hasNext()) {
            String line = it.next();
            if (toRead) {
                result.add(WeatherInfo.valueOf(line));
                toRead = false;
            }
            else {
                toRead = true;
            }
        }

        */



        /*
         O código a seguir ao comentário é equivalente a este dentro do comentário:

        Iterable<String> iter0 = dropWhile(req.getContent(path), s -> s.startsWith("#"));
        Iterable<String> iter1 = skip(iter0, 1);
        Iterable<String> iter2 = filter(iter1, s-> s.contains("worldweatheronline"));
        Iterable<WeatherInfoDto> iter3 = map(iter2, s -> WeatherInfoDto.valueOf(s));
       */

        return map(
                filter(
                    skip(
                        dropWhile(req.getContent(path), s -> s.startsWith("#")),
                    1
                    ),
                    s -> s.contains("worldweatheronline")
                ),
                s -> WeatherInfoDto.valueOf(s)
            );

    }




    public Iterable<DayInfoDto> pastDays(double latitude, double longitude, LocalDate from, LocalDate to) {
        String query = latitude + "," + longitude;
        String path =  WEATHER_SERVICE + String.format(WEATHER_PAST_TEMPLATE, query, from, to, 24, API_KEY);


        List<DayInfoDto> result = new ArrayList<>(); // where the dto.WeatherInfo instances are collected

        Iterator<String> it = req.getContent(path).iterator();

        while(it.hasNext() &&  it.next().startsWith("#")) {}

        boolean toRead= true;
        while(it.hasNext()) {
            String line = it.next();
            if (toRead) {
                result.add(DayInfoDto.valueOf(line));
                toRead = false;
            }
            else {
                toRead = true;
            }
        }

        return result;

        // colocar aqui o código alternativo!
    }

    public Iterable<LocationDto> search(String location) {

        String path =  WEATHER_SERVICE + String.format(WEATHER_SEARCH_TEMPLATE, location, API_KEY);

        /*
        List<LocationDto> result = new ArrayList<>(); // where the dto.WeatherInfo instances are collected

        try {
            // used to do the HTTP request to worldweatheronline service
            //URL url = new URL(path);

            // the stream used to get the response to the service request
            //InputStream respStream = url.openStream();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(respStream))) {
                String line;
                while ((line = reader.readLine()) != null && line.startsWith("#")) {
                }

                while (line != null) {

                    result.add(LocationDto.valueOf(line));
                    line = reader.readLine();

                }
            }
        }
        catch(IOException e) {
            throw new UncheckedIOException(e);
        }
        return result;
        */


        try(BufferedReader reader =
                    new BufferedReader(new InputStreamReader(req.openStream(path)))) {
            //String json = reader.lines().reduce((s1,s2) -> s1 + s2).get();
            SearchApiDto search = gson.fromJson(reader, SearchApiDto.class);
            return Arrays.asList(search.getResult().getLocations());
        }
        catch(IOException e) {
            throw new UncheckedIOException(e);
        }

    }

}
