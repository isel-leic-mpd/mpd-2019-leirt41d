import dto.DayInfoDto;
import dto.LocationDto;
import dto.WeatherInfoDto;
import model.DayInfo;
import model.Location;
import model.WeatherInfo;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static utils.EagerQueries.map;

public class WeatherService {
    private WeatherWebApi api;

    public WeatherService(WeatherWebApi api) {
        this.api = api;
    }



    public Stream<WeatherInfo> pastWeather(Location loc, LocalDate from, LocalDate to, int period) {
        return  api.pastWeather(loc.getLatitude(),
                                loc.getLongitude(),
                                from, to, period)
                .map(this::dtoToWeatherInfo);
    }

    public  WeatherInfo dtoToWeatherInfo(WeatherInfoDto dto) {
        return new WeatherInfo(
                dto.getLocalTime(),
                dto.getTempC(),
                dto.getDescription(),
                dto.getPrecipMM(),
                dto.getFeelsLikeC());
    }



    public Stream<Location> search(String query) {
        return api.search(query)
                .map(this::dtoToLocation);
    }

    public Stream<DayInfo>
            pastDays(Location loc, LocalDate from, LocalDate to) {

        return api.pastDays(loc.getLatitude(),
                            loc.getLongitude(),
                            from,to)
                .map(di -> dtoToDayInfo(di,loc));

    }

    public  Location dtoToLocation(LocationDto dto) {
        return new Location(dto.getName(),
                dto.getCountry(),
                dto.getLatitude(),
                dto.getLongitude(),
                this::pastDays
                );
    }



    public DayInfo dtoToDayInfo(DayInfoDto dto, Location loc) {
        return new DayInfo(
                dto.getDate(),
                dto.getMaxTemp(),
                dto.getMinTemp(),
                dto.getSunrise(),
                dto.getSunset(),
                dto.getMoonrise(),
                dto.getMoonset(),
                dto.getMoonPhase(),
                dto.getMoonIllumination(),
                () -> pastWeather(loc, dto.getDate(), dto.getDate(), 1)
        );
    }
}
