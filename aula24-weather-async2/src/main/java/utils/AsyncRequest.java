package utils;

import java.io.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public interface AsyncRequest {
    CompletableFuture<Stream<String>> getContent(String path);

    static  Stream<String> getLines(InputStream input) {
        BufferedReader reader =
                new BufferedReader(
                        new InputStreamReader(input));
        return reader.lines();

    }
}
