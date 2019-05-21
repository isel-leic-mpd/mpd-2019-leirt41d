package utils;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Response;

import java.io.*;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static utils.AsyncRequest.getLines;
import static org.asynchttpclient.Dsl.asyncHttpClient;

public class HttpRequest implements AsyncRequest {

    private static void ahcClose(AsyncHttpClient client) {
        try {
            client.close();
        }
        catch(IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public CompletableFuture<Stream<String>>
        getContent(String path) {

        AsyncHttpClient client = asyncHttpClient();

        return  client.prepareGet(path)
                .execute()
                .toCompletableFuture()
                .thenApply(r ->
                    getLines(r.getResponseBodyAsStream()))
                .whenComplete((s, t) -> ahcClose(client));
    }

}
