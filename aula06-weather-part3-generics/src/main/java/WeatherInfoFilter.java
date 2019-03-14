import dto.WeatherInfo;

//An interface used to filter selected WeatherInfo instances
public interface WeatherInfoFilter {
    boolean check(WeatherInfo wi);
}
