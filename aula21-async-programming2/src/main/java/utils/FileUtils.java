package utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import files.AsyncFile;

public class FileUtils {

    private final static int BUFSIZE = 4096*16;



    /**
     * Now an async file copy using NIO2 AsynchronousFileChannel
     * as an async operation notifying success/error with callbacks
     *
     * @param fileIn
     * @param fileOut
     * @return
     * @throws IOException
     */
    public static void copyFileAsync(
            String fileIn, String fileOut,
            BiConsumer<Throwable, Long> completed) throws IOException{

        long[] nBytes  = {0};

        AsyncFile fin = AsyncFile.open(fileIn);
        AsyncFile fout = AsyncFile.create(fileOut);

        byte[] buffer = new byte[BUFSIZE];

        BiConsumer<Throwable, Integer>[] writeCompletion =
                new BiConsumer[1];

        BiConsumer<Throwable, Integer> readCompletion = (t, n) -> {
            if (t != null) {
                fin.close();
                fout.close();
                completed.accept(t, null);
                return;
            }
            if (n <=0) {
                fin.close();
                fout.close();
                completed.accept(null, nBytes[0]);
                return;
            }
            nBytes[0] += n;
            fout.writeBytes(buffer, 0, n, writeCompletion[0]);
        };

        writeCompletion[0] =  (t, __) -> {
            //System.out.println("On write completed, current thread: " +
            // Thread.currentThread().getId());
            if (t != null) {
                fin.close();
                fout.close();
                completed.accept(t, null);
                return;
            }
            fin.readBytes(buffer,readCompletion);
        };

        fin.readBytes(buffer, readCompletion);
    }

    private static CompletableFuture<Long>
    continueCopy(AsyncFile fIn, AsyncFile fOut, int i, long total) {
        if ( i < BUFSIZE) {
            fIn.close();
            fOut.close();
            return CompletableFuture.completedFuture(total);
        }
        return copyToSequence(fIn, fOut, total);
    }

    private static CompletableFuture<Long>
        copyToSequence(AsyncFile fIn, AsyncFile fOut, long total) {
            byte[] buffer = new byte[BUFSIZE];

            return fIn.readBytes(buffer)
               .thenCompose(i -> {
                    return fOut.writeBytes(buffer, 0, i)
                    .thenCompose(i2 -> continueCopy(fIn, fOut, i, total + i));
               });
    }

    private static CompletableFuture<Long>
    copyToSequence2(AsyncFile fIn, AsyncFile fOut, long total) {
        byte[] buffer = new byte[BUFSIZE];

        return fIn.readBytes(buffer)
                .thenCompose(i -> {
                    if ( i < BUFSIZE) {
                        fIn.close();
                        fOut.close();
                        return CompletableFuture.completedFuture(total);
                    }
                    return fOut.writeBytes(buffer, 0, i)
                            .thenCompose(i2 -> copyToSequence2(fIn, fOut, total + i));
                });
    }

    /**
     * Now an async file copy using NIO2 AsynchronousFileChannel
     * as an async operation that returns a CompletableFuture enable composition of asynchronous copies
     *
     * @param fileIn
     * @param fileOut
     * @return
     * @throws IOException
     */
    public static CompletableFuture<Long> copyFileAsync(
            String fileIn, String fileOut) {

        AsyncFile fin = AsyncFile.open(fileIn);
        AsyncFile fout = AsyncFile.create(fileOut);

        return copyToSequence(fin,fout,0);
    }

  



}

