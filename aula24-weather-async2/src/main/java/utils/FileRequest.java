package utils;

import java.io.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static utils.IRequest.getLines;

import static java.lang.ClassLoader.getSystemResource;

public class FileRequest  implements AsyncRequest {

    @Override
    public CompletableFuture<Stream<String>> getContent(String path) {
        path = path.substring(path.lastIndexOf('/')+1, path.lastIndexOf('&'))
                .replace('&', '-')
                .replace('=', '-')
                .replace( '?','-')
                .replace( ',','-')+ ".txt";
        try(InputStream input=getSystemResource(path).openStream()) {
            Stream<String> lines =
                    AsyncRequest.getLines(input)
                    .collect(toList())
                    .stream();
            return CompletableFuture.completedFuture(lines);
        }
        catch(IOException e) {
            throw new UncheckedIOException(e);
        }

    }
}
