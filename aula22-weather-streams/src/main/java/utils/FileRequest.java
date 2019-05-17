package utils;

import java.io.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import static utils.IRequest.getLines;

import static java.lang.ClassLoader.getSystemResource;

public class FileRequest  implements IRequest {

    @Override
    public Stream<String> getContent(String path) {
        path = path.substring(path.lastIndexOf('/')+1, path.lastIndexOf('&'))
                .replace('&', '-')
                .replace('=', '-')
                .replace( '?','-')
                .replace( ',','-')+ ".txt";
        try {
            return getLines(getSystemResource(path).openStream());
        }
        catch(IOException e) {
            throw new UncheckedIOException(e);
        }

    }
}
