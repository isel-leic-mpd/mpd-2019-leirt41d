import org.junit.Assert;
import org.junit.Test;

import files.AsyncFile;
import utils.FileUtils;
import static utils.FileUtils.copyFileAsync;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;



public class AsyncTests {
    // start Of Sequential Tests
    private static void copyFile(String fin, String fout) throws IOException{
        try(InputStream is = new FileInputStream(fin);
            OutputStream os = new FileOutputStream(fout))  {
            byte[] buffer = new byte[4096];
            int size;
            while((size = is.read(buffer)) > 0)
                os.write(buffer, 0, size);
        }

    }
    @Test
    public void copy2FilesSequentialTest() throws IOException {
        String[] fin = { "fin1.dat", "fin2.dat"};
        String[] fout = {"fout1.dat", "fout2.dat"};

        copyFile("fin1.dat","fout1.dat" );
        copyFile("fin2.dat","fout2.dat" );
    }
    private static Stream<String> getLines(Path p) {
        try {
            return Files.lines(p);
        }
        catch(IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Test
    public void countFilesLinesSequentialTest() throws IOException {
        List<Path> files = getFilesFromFolder("rdf-files.tar");
        long start = System.currentTimeMillis();
        long result = files.stream()
                .flatMap(AsyncTests::getLines)
                .count();
        System.out.println(result
                + ", done in "
                + (System.currentTimeMillis()-start) + " ms!");
    }

    @Test
    public void countLinesSequentialTest() {
        String fin = "uthread.c";

        long count = getLines(Path.of(fin))
                .peek(l -> {
                    System.out.println(l); } )
                .count();
        assertEquals(526, count);
        System.out.println("lines: " + count);
    }
    // End of sequential tests


    // start of old tests

    @Test
    public void CountLinesOf2TextFileTest() {
        String fin = "uthread.c";

        AsyncFile file1 = AsyncFile.open(fin);
        AsyncFile file2 = AsyncFile.open(fin);
        CompletableFuture<Long> lines1 =
                file1.readLines()
                        .thenApply(s -> s.count());
        CompletableFuture<Long> lines2 =
                file2.readLines()
                        .thenApply((s-> s.count()));

        CompletableFuture<Long> all =
                lines1
                        .thenCombine(lines2, (nl1,nl2) -> nl1+nl2);

        assertEquals(526*2, all.join().longValue());
    }

    @Test
    public void countLinesWithAsyncFile2Test() {
        String fin = "uthread.c";

        AsyncFile file = AsyncFile.open(fin);

        long count = file.readLines()
                .join().count();

        assertEquals(526, count);
        System.out.println("lines: " + count);

    }
    //end of old tests

    @Test
    public void ShowLinesOfTextFileTest() {
        String fin = "uthread.c";

        AsyncFile file = AsyncFile.open(fin);
        CompletableFuture<Stream<String>> lines =
                file.readLines();

        CompletableFuture<Void> result = lines.thenAccept(sl -> {
            sl.forEach(System.out::println);
            file.close();
        });

        result.join();
    }

    @Test
    public void ShowCountOfLinesOfTextFileTest() {
        String fin = "uthread.c";

        AsyncFile file = AsyncFile.open(fin);
        CompletableFuture<Stream<String>> lines =
                file.readLines();

        CompletableFuture<Long> result =
            lines.thenApply(sl -> {
                return  sl.count();
        })
        .whenComplete((__, error) -> file.close());

        System.out.println(result.join());
    }


    @Test
    public void copy2FilesWithCompletableFutureTest() {
        String[] fin = { "fin1.dat", "fin2.dat"};
        String[] fout = {"fout1.dat", "fout2.dat"};

        CompletableFuture<Long> fl1 = copyFileAsync(fin[0], fout[0]);

        CompletableFuture<Long> fl2 = copyFileAsync(fin[1], fout[1]);
        CompletableFuture<Long> fres =
                fl1.thenCombine( fl2, (l1,l2) -> l1 +l2);

        long l = fres.join();
        System.out.println(l);
    }

    @Test
    public void copy2FilesInSequenceWithCompletableFutureTest() {
        String[] fin = { "fin1.dat", "fin2.dat"};
        String[] fout = {"fout1.dat", "fout2.dat"};

        CompletableFuture<Long> fl1 = copyFileAsync(fin[0], fout[0]);

        CompletableFuture<Long> fres=
                fl1
                        .thenCompose( l ->
                                copyFileAsync(fin[1], fout[1]));

        long l = fres.join();
        System.out.println(l);
    }

    @Test
    public void copyNFilesWithCompletableFutureCombineTest() {
        String[] fin = { "fin1.dat", "fin2.dat", "fin3.dat", "fin4.dat" };
        String[] fout = { "fout.dat", "fout2.dat", "fout3.dat", "fout4.dat" };

        List<CompletableFuture<Long>> futureCopies =
                IntStream.range(0, fin.length)
                .boxed()
                .map(i -> copyFileAsync(fin[i], fout[i]))
                .collect(toList());

        CompletableFuture<Long> total = futureCopies.stream()
                .reduce((f1,f2) -> f1.thenCombine(f2, (l1, l2) -> l1+l2))
                .get();

        System.out.println(total);

    }

    @Test
    public void copyNFilesWithCompletableFutureAllOfTest() {
        String[] fin = { "fin1.dat", "fin2.dat", "fin3.dat", "fin4.dat" };
        String[] fout = { "fout.dat", "fout2.dat", "fout3.dat", "fout4.dat" };

        List<CompletableFuture<Long>> futureCopies =
                IntStream.range(0, fin.length)
                        .boxed()
                        .map(i -> copyFileAsync(fin[i], fout[i]))
                        .collect(toList());

        CompletableFuture<Long>[] futCounts =
                futureCopies.toArray(s-> new CompletableFuture[s]);


        CompletableFuture<Long> total =
                CompletableFuture.allOf(futCounts)
                .thenApply(__ -> {
                    return Stream.of(futCounts)
                    .mapToLong(f -> f.join())
                    .sum();
                });

        System.out.println(total.join());

    }

    private static List<Path> getFilesFromFolder(String rootName)
        throws IOException  {
        Path root = Path.of(rootName);

        return Files.walk(root)
                .filter(p -> Files.isRegularFile(p))
                .collect(Collectors.toList());
    }


    @Test
    public void countMultipleFilesLinesAsyncWithAllOfTest()
                throws IOException {
        List<Path> files = getFilesFromFolder("rdf-files.tar");

        long start = System.currentTimeMillis();

        Stream<CompletableFuture<Long>> futCounts =
                files.stream()
                .map(p -> {
                    AsyncFile f = AsyncFile.open(p.toString());
                    return f.readLines()
                            .thenApply(s -> s.count())
                            .whenComplete((l, __) -> f.close());
                });

        CompletableFuture<Long>[] futArray =
                futCounts.toArray(s-> new CompletableFuture[s]);

        System.out.println("Start count!");

        CompletableFuture<Long> result =
            CompletableFuture
            .allOf(futArray)
            .thenApply(__ -> {
                return Stream.of(futArray)
                    .mapToLong(f -> f.join())
                    .sum();
            });

        System.out.println(result.join()
                + ", done in "
                + (System.currentTimeMillis()-start) + " ms!");
    }

    @Test
    public void countMultipleFilesLinesAsyncWithCombineTest()
            throws IOException {
        List<Path> files = getFilesFromFolder("rdf-files.tar");

        long start = System.currentTimeMillis();

        Stream<CompletableFuture<Long>> futCounts =
                files.stream()
                        .map(p -> {
                            AsyncFile f = AsyncFile.open(p.toString());
                            return f.readLines()
                                    .whenComplete((t, s) -> f.close())
                                    .thenApply(s -> s.count());
                        });

        System.out.println("Start count!");

        CompletableFuture<Long> result = futCounts
                    .reduce((f1,f2) -> {
                        return f1.thenCombine(f2, (l1,l2)-> l1+l2 );
                    })
                    .get();

        System.out.println(result.join()
                + ", done in "
                + (System.currentTimeMillis()-start) + " ms!");
    }


}
