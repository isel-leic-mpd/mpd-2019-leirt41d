package org.isel.jingle.util.req;

import org.isel.jingle.util.iterators.IteratorInputStream;

import java.io.InputStream;
import java.util.function.Function;
import java.util.function.Supplier;

public class BaseRequest implements Request {

    private final Function<String, InputStream> openStream;

    public BaseRequest(Function<String, InputStream> openStream) {
        this.openStream = openStream;
    }

    public final Iterable<String> getLines(String path) {

        return () -> {
            Supplier<InputStream> in = () -> openStream.apply(path);
            return new IteratorInputStream(in);
        };
    }
}