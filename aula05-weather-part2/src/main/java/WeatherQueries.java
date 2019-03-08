import dto.WeatherInfo;

import java.util.ArrayList;
import java.util.List;

public class WeatherQueries {

    public Iterable<WeatherInfo> getSunnyDays(Iterable<WeatherInfo> src) {
         return filter(src,
             wi -> {
                    return wi.getDescription().contains("Sunny ");
             });
    }

    public Iterable<WeatherInfo> getDaysOf(Iterable<WeatherInfo> src,
                                           String keyDescription) {
        return filter(src, new WeatherInfoFilter() {
            @Override
            public boolean check(WeatherInfo wi) {
                return wi.getDescription().contains(keyDescription);
            }
        });
    }

    public Iterable<WeatherInfo> filter(
            Iterable<WeatherInfo> src, WeatherInfoFilter selector) {
        List<WeatherInfo> result = new ArrayList<>();

        for(WeatherInfo wi : src) {
            if ( selector.check(wi))
                result.add(wi);
        }
        return result;
    }


}
