package org.isel.jingle.util.req;

import java.util.List;

public interface Request {

    public Iterable<String> getLines(String path);
}
