package utils;

import org.asynchttpclient.AsyncHttpClient;

import java.io.*;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static utils.IRequest.getLines;
import static org.asynchttpclient.Dsl.asyncHttpClient;

public class HttpRequest implements IRequest {

    private static void ahcClose(AsyncHttpClient client) {
        try {
            client.close();
        }
        catch(IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    public CompletableFuture<Stream<String>> getContentAsync(String path) {

        AsyncHttpClient client = asyncHttpClient();

        return client.prepareGet(path)
                .execute()
                .toCompletableFuture()
                .thenApply(r -> getLines(r.getResponseBodyAsStream()))
                .whenComplete((s,t) -> ahcClose(client)
               );

    }

    @Override
    public Stream<String> getContent(String path) {
        try {
            URL url = new URL(path);
            return
                getLines(url.openStream())
                .collect(Collectors.toList())
                .stream();
        }
        catch(IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
