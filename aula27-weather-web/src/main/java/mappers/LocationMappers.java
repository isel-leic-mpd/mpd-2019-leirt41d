package mappers;

import html.Element;
import html.InputText;
import html.Label;
import html.Ul;
import model.Location;

import java.time.LocalDate;

import static html.Dsl.*;

public class LocationMappers {

    public static Element mapToUnorderedList(Location loc)  {
        return ul(
                li( "Name: " +loc.getName()),
                li( "Country: " + loc.getCountry()),
                li( "Latitude: " + Double.toString(loc.getLatitude())),
                li("Longitude" + Double.toString(loc.getLongitude()))
        );
    }

    public static Element mapToTableRaw(Location l) {
        LocalDate day1 = LocalDate.of(2019, 5,1);
        LocalDate day2 = LocalDate.of(2019, 5,31);
        double latitude = l.getLatitude();
        double longitude = l.getLongitude();
        String urlPastDays = String.format("/days/%f:%f/from/%s/to/%s",
                latitude, longitude, day1, day2);

        return
                tr (
                    td(l.getName()),
                    td(l.getCountry()),
                    td(Double.toString(l.getLatitude())).align("right"),
                    td(Double.toString(l.getLongitude())).align("right"),
                    td(a("weather info", urlPastDays))
                );
    }
}
