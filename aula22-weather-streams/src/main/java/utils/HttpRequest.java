package utils;

import org.asynchttpclient.AsyncHttpClient;

import java.io.*;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
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



    @Override
    public Stream<String> getContent(String path) {
        try {
            URL url = new URL(path);
            InputStream input = url.openStream();
            Stream<String> lines =
                    getLines(input)
                    .collect(toList())
                    .stream();
            input.close();
            return lines;

        }
        catch(IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
