import dto.WeatherInfo;

import java.util.ArrayList;
import java.util.List;

public class WeatherQueries {

    public Iterable<WeatherInfo> getSunnyDays(Iterable<WeatherInfo> src) {
         return filter(src,
             wi -> wi.getDescription().contains("Sunny "));
    }

    public Iterable<WeatherInfo> getDaysOf(Iterable<WeatherInfo> src,
                                           String keyDescription) {
        return filter(src,
                wi -> wi.getDescription().contains(keyDescription)
        );
    }

    public Iterable<Integer> map(Iterable<WeatherInfo> src,
                               WeatherInfoToInt mapper  ) {
        List<Integer> result = new ArrayList<>();

        for(WeatherInfo wi : src) {
            result.add(mapper.toInt(wi));
        }
        return result;
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

    public Iterable<WeatherInfo>
            getDaysWithRealFeelDifferenceGreaterThanThreshold(
            Iterable<WeatherInfo> src,  int threshold) {

        return filter(src,
                wi -> {

                    int diff = Math.abs(wi.getTempC() - wi.getFeelsLikeC());
                    return diff > threshold;
                });

    }

    public int count(Iterable<WeatherInfo> src) {
        int count = 0;
        for(WeatherInfo wi : src) {
            count++;
        }
        return count;
    }

    public int countSunnyDays(Iterable<WeatherInfo> src) {
       return count(getSunnyDays(src));
    }

    public int sum(Iterable<Integer> src) {
        int sum =0;
        for(int val : src)
            sum += val;
        return sum;
    }

    public int sumSunnyDaysTemp(Iterable<WeatherInfo> src) {
        /*
        Iterable<WeatherInfo> sunny =
                filter(src, wi -> wi.getDescription().contains("Sunny"));
        Iterable<Integer> temps = map(sunny, wi -> wi.getTempC());
        return sum(temps);
        */

        return sum(
                  map(
                    filter(src,wi -> wi.getDescription().contains("Sunny") ),
                    wi -> wi.getTempC()
                  )
                );
    }


}
