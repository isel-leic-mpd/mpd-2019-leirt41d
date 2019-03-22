package dto;

public class Location {
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
    private static final int DISTRICT   =   2;
    private static final int LATITUDE   =   3;
    private static final int LONGITUDE  =   4;
    private static final int WEATHER_URL=   5;

    private String name;        // index 0
    private String country;     // index 1
    private String district;    // index 2
    private double latitude;    // index 3
    private double longitude;   // index 4
    private String weatherUrl;  // index 5

    public Location(String name, String country, String district, double latitude, double longitude, String weatherUrl) {
        this.name = name;
        this.country = country;
        this.district = district;
        this.latitude = latitude;
        this.longitude = longitude;
        this.weatherUrl = weatherUrl;
    }

    // acessors
    public String getName()         { return name; }
    public String getCountry()      { return country; }
    public String getDistrict()     { return district; }
    public double getLatitude()     { return latitude; }
    public double getLongitude()    { return longitude; }
    public String getWeatherUrl()   { return weatherUrl; }

    @Override
    public String toString() {
        return "{"
                + name
                + ", country=" + country
                + ", district=" + district
                + ", latitude=" + latitude
                + ", longitude=" + longitude
                + ", weatherUrl=" + weatherUrl;
    }

    public static Location valueOf(String line) {
        String[] parts = line.split("\t");
        return new Location(
                parts[NAME],
                parts[COUNTRY],
                parts[DISTRICT],
                Double.valueOf(parts[LATITUDE]),
                Double.valueOf(parts[LONGITUDE]),
                parts[WEATHER_URL]

        );
    }
}
