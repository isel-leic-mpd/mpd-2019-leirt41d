
import java.util.concurrent.*;
import java.util.function.Consumer;

import static utils.ThreadUtils.join;
import static utils.ThreadUtils.sleep;

public class FileOpers {
    private static ExecutorService pool =
        Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    // simulate a file copy done in 5 seconds
    public static long copyFile(String fin, String fout) {
        sleep(5000);
        return 1000;
    }

    public static long copy2Files(
            String fin1, String fin2,
            String fout1, String fout2) {
        long[] len1 = {0}, len2 = {0};

        Thread t1 = new Thread(() -> {
            System.out.println("copy in thread "+
                    Thread.currentThread().getId());
            len1[0] = copyFile(fin1, fout1);
        });
        t1.start();

        Thread t2 = new Thread(() -> {
            System.out.println("copy in thread "+
                    Thread.currentThread().getId());
            len2[0] = copyFile(fin2, fout2);
        });
        t2.start();

        // wait for threads termination
        join(t1);
        join(t2);
        return len1[0] +len2[0];
    }

    public static long copy2FilesWithFutures(
            String fin1, String fin2,
            String fout1,String fout2) {


        Future<Long> f1 =  pool.submit(() -> copyFile(fin1, fout1));
        Future<Long> f2 =  pool.submit(() -> copyFile(fin2, fout2));

        try {
            // blocking on futures completion
            return f1.get() + f2.get();
        }
        catch(ExecutionException | InterruptedException e) {
            return -1L;
        }
    }

    // First async operation idiom:
    // Return a future representing copy in course
    public static Future<Long> copyFileFut(String fin, String fout) {
        return pool.submit(() -> copyFile(fin, fout));
    }


    // Second async operation idiom:
    // Call a callback (consumer) when copy is completed
    public static void copyFileAsync(String fin, String fout, Consumer<Long> completed) {
        pool.submit(() -> {
            long res = copyFile(fin, fout);
            completed.accept(res);
        });
    }

    // Third async operation idiom:
    // Return a CompletableFuture representing copy in course
    // A CompletableFuture is best than a Future since it supports
    // composition, i.e, generation a new future somwhow from existing
    // futures (ex: theCombine),
    public static CompletableFuture<Long> copyFileAsync(String fin, String fout) {
        CompletableFuture<Long> done = new CompletableFuture<>();
        pool.execute(() -> {
            done.complete(copyFile(fin, fout));
        });
        return done;
    }
}
