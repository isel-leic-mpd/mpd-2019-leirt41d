package observables;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.schedulers.IoScheduler;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Flow.*;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class ObsUtils {

    public static <T> Observable<T> from(CompletableFuture<T> fut) {

        return Observable.create( subscriber -> {
            fut.whenComplete((T val, Throwable fault) -> {
                if (fault == null) {
                    subscriber.onNext(val);
                    subscriber.onComplete();
                } else {
                    subscriber.onError(fault);
                }
            });

        });
    }
}
