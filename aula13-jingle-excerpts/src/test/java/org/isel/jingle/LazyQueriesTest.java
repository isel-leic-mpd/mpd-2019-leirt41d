package org.isel.jingle;

import org.isel.jingle.util.queries.LazyQueries;
import org.junit.Test;

import java.util.List;
import java.util.Random;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.isel.jingle.util.queries.LazyQueries.count;
import static org.isel.jingle.util.queries.LazyQueries.filter;
import static org.isel.jingle.util.queries.LazyQueries.flatMap;
import static org.isel.jingle.util.queries.LazyQueries.from;
import static org.isel.jingle.util.queries.LazyQueries.generate;
import static org.isel.jingle.util.queries.LazyQueries.iterate;
import static org.isel.jingle.util.queries.LazyQueries.limit;
import static org.isel.jingle.util.queries.LazyQueries.map;
import static org.isel.jingle.util.queries.LazyQueries.max;
import static org.isel.jingle.util.queries.LazyQueries.skip;
import static org.isel.jingle.util.queries.LazyQueries.takeWhile;
import static org.isel.jingle.util.queries.LazyQueries.toArray;
import static org.junit.Assert.assertArrayEquals;


public class LazyQueriesTest {
    @Test
    public void testMax(){
        Random r = new Random();
        Iterable<Integer> nrs = limit(generate(() -> r.nextInt(100)), 10);
        Integer max = max(nrs);
        System.out.println(max);
    }

    @Test
    public void testSkip(){
        List<Integer> nrs = asList(1, 2, 3, 4, 5, 6, 7, 8);
        Object[] actual = toArray(skip(nrs, 3));
        Object[] expected = { 4, 5, 6, 7, 8 };
        assertArrayEquals(expected, actual);
    }
    @Test
    public void testLimit(){
        Iterable<Integer> nrs = LazyQueries.limit(iterate(1, n -> n + 1), 11);
        assertEquals(11,count(nrs));
    }

    @Test
    public void testGenerate(){
        Random r = new Random();
        Iterable<Integer> nrs = limit(generate(() -> r.nextInt(100)), 10);
        for(int n : nrs) System.out.println(n);
    }

    @Test
    public void testMap(){
        List<String> words = asList("super", "isel", "ola", "fcp");
        Object[] actual = toArray(map(words, w -> w.length()));
        Object[] expected = { 5, 4, 3, 3 };
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFirstFilterMapNrs(){
        // An infinite Sequence CANNOT be converted to a Collection
        // Object[] nrs = toArray(iterate(1, n -> n + 1));

        Iterable<Integer> nrs = iterate(1, n -> n + 1);
        int first = LazyQueries.first(filter(map(nrs,
                n -> n*n),
            n -> n > 3));
        assertEquals(4, first);
    }
}