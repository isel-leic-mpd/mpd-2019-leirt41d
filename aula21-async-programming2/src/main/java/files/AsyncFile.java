package files;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.Channel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.InvalidParameterException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;


public class AsyncFile {
    private enum Mode { Read, Write}

    private static int CHUNKSIZE = 4096*16;

    private AsynchronousFileChannel channel;
    private Mode mode;
    private long position;

    public static void closeChannel(Channel c) {
        try { c.close(); } catch(Exception e) {}
    }

    public static AsyncFile open(String path) {
        Path pathIn = Paths.get(path);
        try {
            return new AsyncFile(pathIn, Mode.Read);
        }
        catch(IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static AsyncFile create(String path) {
        Path pathIn = Paths.get(path);
        try {
            return new AsyncFile(pathIn, Mode.Write);
        }
        catch(IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private  AsyncFile(Path path, Mode mode) throws IOException {
        if (mode == Mode.Read )
            channel = AsynchronousFileChannel.open(path,StandardOpenOption.READ);
        else
            channel = AsynchronousFileChannel.open(path,
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING
               );

    }

    public void readBytes(byte[] data, int ofs, int size,
                   BiConsumer<Throwable, Integer> completed) {
        if (completed == null)
            throw new InvalidParameterException("callback can't be null!");
        if (mode == Mode.Write)
            throw new IllegalStateException("File is not readable");
        if (size + ofs > data.length)
            size = data.length - ofs;
        if (size ==0) {
            completed.accept(null, 0);
            return;
        }
        int s = size;
        ByteBuffer buf = ByteBuffer.wrap(data, ofs, size);
        CompletionHandler<Integer,Object> readCompleted =
            new CompletionHandler<Integer,Object>() {

                @Override
                public void completed(Integer result, Object attachment) {
                    position += result;
                    completed.accept(null,result);
                }

                @Override
                public void failed(Throwable exc, Object attachment) {
                    completed.accept(exc, null);
                }
            };
        channel.read(buf,position, null, readCompleted);
    }

    public  void readBytes(byte[] data, BiConsumer<Throwable, Integer> completed) {
        readBytes(data, 0, data.length, completed);
    }

    public void writeBytes( byte[] data, int ofs, int size,
                     BiConsumer<Throwable, Integer> completed) {
        if (completed == null)
            throw new InvalidParameterException("callback can't be null!");
        if (mode == Mode.Read)
            throw new IllegalStateException("File is not writable");
        if (ofs + size > data.length) size = data.length - ofs;

        ByteBuffer buf = ByteBuffer.wrap(data, ofs, size);
        CompletionHandler<Integer,Object> writeCompleted =
            new CompletionHandler<Integer,Object>() {

            @Override
            public void completed(Integer result, Object attachment) {
                position += result;
                completed.accept(null, result);
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                completed.accept(exc, null);
            }
        };

        channel.write(buf,position,null,writeCompleted);
    }

    public CompletableFuture<Integer> writeBytes(byte[] data, int ofs, int size) {
        CompletableFuture<Integer> completed = new CompletableFuture<>();
        writeBytes(data,ofs, size,
                (t,i) -> {
                    if (t== null) completed.complete(i);
                    else completed.completeExceptionally(t);
                });
        return completed;
    }

    public CompletableFuture<Integer> writeBytes(byte[] data) {
        return writeBytes(data, 0, data.length);
    }


    public CompletableFuture<Integer> readBytes(byte[] data, int ofs, int size) {
        CompletableFuture<Integer> completed = new CompletableFuture<>();
        readBytes(data, ofs, size,
                (t,i) -> {
                    if (t== null) completed.complete(i);
                    else completed.completeExceptionally(t);
                });
        return completed;
    }

    public CompletableFuture<Integer> readBytes(byte[] data) {
        return readBytes(data,0, data.length);
    }

    private  CompletableFuture<Integer> copyToSequence(byte[] out, int ofs) {
        return readBytes(out, ofs, CHUNKSIZE)
                .thenCompose(i -> {
                    if (i <= 0)
                        return CompletableFuture.completedFuture(ofs);
                    return copyToSequence(out, ofs+i);
                });
    }

    private int getSize() {
        try {
            long s = channel.size();
            if (s > Integer.MAX_VALUE) throw new RuntimeException("File too big to read!");
            return (int) s;
        }
        catch(IOException e) { throw new UncheckedIOException(e); }
    }

    public CompletableFuture<ByteArrayInputStream> readAll() {
        byte[] data = new byte[getSize()];
        return copyToSequence(data, 0)
        .thenApply( i ->
                new ByteArrayInputStream(data, 0, i));
    }

    public CompletableFuture<Stream<String>>
    readLines() {
        return readAll().
                thenApply(sin -> {
                      BufferedReader reader =
                            new BufferedReader(new InputStreamReader(sin));
                      return reader.lines();
                });
    }

    private static ExecutorService pool = Executors.newFixedThreadPool(8);
    public CompletableFuture<Stream<String>> readLines2() {
        if (getSize() < 30000)
            return toMemoryStream().
                    thenApply(sin -> {
                        //System.out.println("Thread " + Thread.currentThread().getId());
                        BufferedReader reader =
                                new BufferedReader(new InputStreamReader(sin));
                        return reader.lines();
                    });
        return toMemoryStream().
                thenApplyAsync(sin -> {
                    System.out.println("Thread " + Thread.currentThread().getId());
                    BufferedReader reader =
                            new BufferedReader(new InputStreamReader(sin));
                    return reader.lines();
                });
    }


    private  void toMemoryStream(
            BiConsumer<Throwable, ByteArrayInputStream> completed) {
        byte[] buffer = new byte[getSize()];
        int[] nBytes = {0};

        BiConsumer<Throwable, Integer>[] readCompletion = new BiConsumer[1];
        readCompletion[0] = (t, n) -> {
            if (t != null) {
                completed.accept(t, null);
                return;
            }
            //System.out.println("On read completed, current thread: " + Thread.currentThread().getId());
            if (n <=0) {
                completed.accept(null, new ByteArrayInputStream(buffer,0,nBytes[0]));
                return;
            }
            nBytes[0] += n;
            readBytes(buffer,nBytes[0], CHUNKSIZE, readCompletion[0]);
        };
        readBytes(buffer,nBytes[0], CHUNKSIZE, readCompletion[0]);
    }

    public  CompletableFuture<ByteArrayInputStream> toMemoryStream() {
        CompletableFuture<ByteArrayInputStream> completed =
                new CompletableFuture<>();

        toMemoryStream((t,sa) -> {
            if (t!= null) completed.completeExceptionally(t);
            else completed.complete(sa);
        });
        return completed;
    }

    public void close() {
        closeChannel(channel);
    }
}
