package model;

import utils.TriFunction;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Location {


    private String name;
    private String country;
    private double latitude;
    private double longitude;

    private TriFunction<Location, LocalDate, LocalDate,
            CompletableFuture<Stream<DayInfo>>> pastDays;

    public Location(String name, String country,   double latitude, double longitude,
                    TriFunction<Location, LocalDate, LocalDate,
                           CompletableFuture<Stream<DayInfo>>> pastDays) {
        this.name = name;
        this.country = country;

        this.latitude = latitude;
        this.longitude = longitude;
        this.pastDays = pastDays;
    }

    public Location(double latitude, double longitude) {

        this.latitude = latitude;
        this.longitude = longitude;

    }

    // acessors
    public String getName()         { return name; }
    public String getCountry()      { return country; }
    public double getLatitude()     { return latitude; }
    public double getLongitude()    { return longitude; }


    public CompletableFuture<Stream<DayInfo>> getDays(LocalDate from, LocalDate to)  {
        return pastDays.apply(this, from,to);
    }

    @Override
    public String toString() {
        return "{"
                + name
                + ", country=" + country
                + ", latitude=" + latitude
                + ", longitude=" + longitude
                + "}";
    }


}
