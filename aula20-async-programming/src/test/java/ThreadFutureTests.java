import org.junit.Test;


import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.assertEquals;

import static utils.ThreadUtils.sleep;

public class ThreadFutureTests {

    @Test
    public void copy2FilesSequential() {
        long start = System.currentTimeMillis();

        long res1 = FileOpers.copyFile("f1", "f2");
        long res2 = FileOpers.copyFile("out1", "out2");
        assertEquals(2000, res1+res2);
        System.out.println("Done in " +
                (System.currentTimeMillis() - start) + " ms.");
    }

    @Test
    public void copy2FilesIn2Threads() {
        System.out.println("test in thread "+
                Thread.currentThread().getId());
        long start = System.currentTimeMillis();
        long res = FileOpers.copy2Files(
                "f1", "f2", "out1", "out2"
        );
        assertEquals(2000, res);
        System.out.println("Done in " +
                (System.currentTimeMillis() - start) + " ms.");
    }

    @Test
    public void copy2FilesWithFuturesTest() {
        long start = System.currentTimeMillis();
        long res = FileOpers.copy2FilesWithFutures(
                "f1", "f2", "out1", "out2"
        );
        assertEquals(2000, res);
        System.out.println("Done in " +
                (System.currentTimeMillis() - start) + " ms.");
    }


    @Test
    public void copy2FilesWithFutureInEachCopy() {
        long start = System.currentTimeMillis();

        Future<Long> f1 = FileOpers.copyFileFut("f1", "out1");
        Future<Long> f2 = FileOpers.copyFileFut("f2", "out2");

        long res=0;
        try {
            res = f1.get() + f2.get();
        }
        catch(InterruptedException | ExecutionException e) {

        }
        assertEquals(2000, res);
        System.out.println("Done in " +
                (System.currentTimeMillis() - start) + " ms.");
    }

    @Test
    public void copy2FilesWithAsyncCallbacksTest() {
        AtomicLong res = new AtomicLong(0);
        AtomicInteger cbNumber = new AtomicInteger(0);
        long start = System.currentTimeMillis();
        CompletableFuture<Boolean> resultFuture =
                new CompletableFuture<>();


        FileOpers.copyFileAsync("f1",
                    "out1",
                    l -> {
                        System.out.println("callback copy 1 in thread "+
                                Thread.currentThread().getId());
                        res.addAndGet(l);
                        if (cbNumber.incrementAndGet() == 2) {
                            // The last callback! Complete future!
                            resultFuture.complete(true);
                        }
                    }
                );

        FileOpers.copyFileAsync("f2",
                "out2",
                l -> {
                    System.out.println("callback copy 2 in thread "+
                            Thread.currentThread().getId());
                    res.addAndGet(l);
                    if (cbNumber.incrementAndGet() == 2) {
                        // The last callback! Complete future!
                        resultFuture.complete(true);
                    }

                }
        );
        //sleep(7000);
        resultFuture.join();
        assertEquals(2000, res.get());
        System.out.println("Done in " +
                (System.currentTimeMillis() - start) + " ms.");
    }

    @Test
    public void copy2FilesWithCompletableFuturesTest() {
        long start = System.currentTimeMillis();

        CompletableFuture<Long> f1 =
                FileOpers.copyFileAsync("f1", "out1");
        CompletableFuture<Long> f2 =
                FileOpers.copyFileAsync("f2", "out2");
        CompletableFuture<Long> futResult =
                f1.thenCombine(f2, (l1,l2) -> l1 + l2);

        assertEquals(2000, futResult.join().longValue());
        System.out.println("Done in " +
                (System.currentTimeMillis() - start) + " ms.");

    }
}
