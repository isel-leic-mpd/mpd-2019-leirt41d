package org.isel.jingle.util.iterators;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

public class IteratorInputStream implements Iterator<String> {
    private BufferedReader reader;
    private String line;
    private boolean isRead = false;

    public IteratorInputStream(Supplier<InputStream> in) {
        this.reader = new BufferedReader(new InputStreamReader(in.get()));
    }

    @Override
    public boolean hasNext() {
        if(isRead) return false;
        if(line != null){
            return true;
        }
        try {
            if ((line = reader.readLine()) != null){
                return true;
            }
            isRead = true;
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public String next() {
        if(!hasNext()){ throw new NoSuchElementException();}
        String aux = line;
        line = null;
        return aux;
    }
}