import dto.WeatherInfo;

import java.util.ArrayList;
import java.util.List;

public class WeatherQueries {
    // first attempt, naive queries

    public static Iterable<WeatherInfo> getSunnyDays(Iterable<WeatherInfo> src) {
        List<WeatherInfo> result = new ArrayList<>();
        for(WeatherInfo wi : src) {
            if (wi.getDescription().contains("Sunny"))
                result.add(wi);
        }
        return result;
    }

    public static Iterable<WeatherInfo> getCloudyDays(Iterable<WeatherInfo> src) {
        List<WeatherInfo> result = new ArrayList<>();
        for(WeatherInfo wi : src) {
            if (wi.getDescription().contains("cloudy"))
                result.add(wi);
        }
        return result;
    }

    // second attempt, value parametrization

    public static Iterable<WeatherInfo> getDaysOf(Iterable<WeatherInfo> src,
                                           String keyDescription) {
        List<WeatherInfo> result = new ArrayList<>();
        for(WeatherInfo wi : src) {
            if (wi.getDescription().contains(keyDescription))
                result.add(wi);
        }
        return result;
    }

    // third attempt, behaviour paremetrization

    //the filter is passed as a WeatherInfoFilter
    public static Iterable<WeatherInfo> filter(
            Iterable<WeatherInfo> src, WeatherInfoFilter selector) {
        List<WeatherInfo> result = new ArrayList<>();

        for(WeatherInfo wi : src) {
            if (selector.check(wi))
                result.add(wi);
        }
        return result;
    }

    public static Iterable<WeatherInfo> getDaysOf2(Iterable<WeatherInfo> src,
                                           String keyDescription) {
        // with an anonymous class, just to remind the old dark days...
        /*
        return filter(src, new WeatherInfoFilter() {
            @Override
            public boolean check(WeatherInfo wi) {
                return wi.getDescription().contains(keyDescription);
            }
        });
        */

        return filter(src,
                wi -> wi.getDescription().contains(keyDescription));
    }



}
