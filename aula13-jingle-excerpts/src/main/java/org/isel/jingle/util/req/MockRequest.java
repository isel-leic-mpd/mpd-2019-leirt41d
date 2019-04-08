package org.isel.jingle.util.req;

import java.io.IOException;
import java.io.InputStream;

public class MockRequest {

    public  static InputStream openStream(String uri) {
        String[] parts = uri.split("/");
        String path = parts[parts.length-1]
                .replace('?', '-')
                .replace('&', '-')
                .replace('=', '-')
                .replace(',', '-')
                .substring(0,68);
        try {
            return ClassLoader.getSystemResource(path).openStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
