package dto;

import com.google.gson.annotations.SerializedName;

public class LocationDto {
    /*
    #The Search API
    #Data returned is laid out in the following order:-
    #AreaName    Country     Region(If available)    Latitude    Longitude   Population(if available)    Weather Forecast URL
    #
    Lisbon	Portugal	Lisboa	38.717	-9.133	517798	http://api.worldweatheronline.com/v2/weather.aspx?q=38.7167,-9.1333
    Lisbon	United States of America	Maine	44.031	-70.105	9392	http://api.worldweatheronline.com/v2/weather.aspx?q=44.0314,-70.105
    */

    // indexes
    private static final int NAME       =   0;
    private static final int COUNTRY    =   1;
    private static final int LATITUDE   =   3;
    private static final int LONGITUDE  =   4;
    private static final int WEATHER_URL=   5;

    @SerializedName("areaName")
    private NameDto[] name;        // index 0
    private NameDto[] country;     // index 1
    private double latitude;    // index 3
    private double longitude;   // index 4
    private NameDto[] weatherUrl;  // index 5

    public LocationDto(NameDto[] name, NameDto[] country,
                       double latitude, double longitude, NameDto[] weatherUrl) {
        this.name = name;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        this.weatherUrl = weatherUrl;
    }

    // acessors
    public String getName()         { return name[0].value; }
    public String getCountry()      { return country[0].value; }
    public double getLatitude()     { return latitude; }
    public double getLongitude()    { return longitude; }
    public String getWeatherUrl()   { return weatherUrl[0].value; }

    @Override
    public String toString() {
        return "{"
                + getName()
                + ", country=" + getCountry()
                + ", latitude=" + latitude
                + ", longitude=" + longitude
                + ", weatherUrl=" + getWeatherUrl();
    }

    public static LocationDto valueOf(String line) {
        String[] parts = line.split("\t");
        NameDto[] name = { new NameDto(parts[NAME])};
        NameDto[] country = { new NameDto(parts[COUNTRY])};
        NameDto[] weatherUrl = { new NameDto(parts[WEATHER_URL])};
        return new LocationDto(
                name,
                country,
                Double.valueOf(parts[LATITUDE]),
                Double.valueOf(parts[LONGITUDE]),
                weatherUrl
        );
    }
}
