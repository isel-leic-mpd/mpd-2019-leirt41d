import html.Body;
import html.Element;
import html.Html;
import html.Table;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import mappers.DaysMappers;
import mappers.HtmlMapper;
import mappers.LocationMappers;
import model.Location;
import utils.HttpRequest;

import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.stream.Stream;

import static html.Dsl.table;

public class App {

    private static WeatherWebApi api = new WeatherWebApi(
            new HttpRequest().compose(System.out::println));

    private static WeatherService service = new WeatherService(api);

    private static Locale locale = new Locale ("pt", "PT");

    public static void main(String[] args) throws Exception {

        Router router = Router.router(Vertx.vertx());

        router.route("/location/:name").handler(App::getLocation);
        router.route("/days/:coords/from/:day1/to/:day2")
                .handler(App::getDaysInfo);

        Vertx.vertx()
                .createHttpServer()
                .requestHandler(router::accept)
                .listen(8000);
    }
    private static void sendErrorResponse(HttpServerResponse resp, int errorCode) {
        resp.putHeader("content-type", "text/html");
        String msg = "<h1> Error" + errorCode + "! </h1>";
        resp.putHeader("content-length", "" + msg.length());
        resp.write(msg);

        resp.setStatusCode(200);
        resp.end();
        resp.close();
    }

    private static void sendOkResponse(HttpServerResponse resp) {

        resp.end();
        resp.close();
    }


    private static Html html = new Html();
    private static Body body = new Body();

    private static void startDoc(HttpServerResponse resp) {
        resp.write(html.startText(0));
        resp.write(body.startText(1));
    }

    private static void endDoc(HttpServerResponse resp) {
        resp.write(body.endText(1));
        resp.write(html.endText(0));

    }

    private static <T> void buildView(
            HttpServerResponse resp, Stream<T> rows,
            HtmlMapper<T> mapper, String... columns) {


        resp.putHeader("content-type", "text/html");
        resp.setStatusCode(200);
        resp.setChunked(true);
        resp.setWriteQueueMaxSize(128);

        startDoc(resp);

        Element table = table(columns);
        resp.write(table.startText(2));

        rows.forEach(r ->
                resp.write(mapper.map(r).toString(3)));

        resp.write(table.endText(2));
        endDoc(resp);
        sendOkResponse(resp);
    }

    private static void getLocation(RoutingContext ctx) {
        HttpServerResponse resp = ctx.response();
        String locationName = ctx.request().getParam("name");

        service.search(locationName)
        .thenAccept(sloc ->
            buildView(resp, sloc, l1 -> LocationMappers.mapToTableRaw(l1),
                    "Name", "Country", "Latitude", "Longitude", "PastMonth"));
    }

    private static void getDaysInfo(RoutingContext ctx)  {
        HttpServerResponse resp = ctx.response();
        String[] latLong = ctx.request().getParam("coords").split(":");
        NumberFormat format = NumberFormat.getInstance(locale);
        double latitude, longitude;
        try {
            latitude =  format.parse(latLong[0]).doubleValue();
            longitude = format.parse(latLong[1]).doubleValue();
        }
        catch(ParseException e) {
            throw new RuntimeException("Parser error", e);
        }

        String day1 = ctx.request().getParam("day1");
        String day2 = ctx.request().getParam("day2");
        Location loc = new Location(latitude, longitude);
        service.pastDays(loc, LocalDate.parse(day1),
                LocalDate.parse(day2))
                .thenAccept(sdays ->
                buildView(resp, sdays, l1 -> DaysMappers.mapToTableRaw(l1),
                "Date", "Min Temp", "Max Temp", "Moon Phase Description", "Sunrise", "Sunset"));

    }
}
