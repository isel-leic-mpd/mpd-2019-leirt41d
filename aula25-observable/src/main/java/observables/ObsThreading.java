package observables;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ObsThreading {

    private static class ObserverLong implements Observer<Long> {

        @Override
        public void onSubscribe(Disposable d) {
            System.out.println("Subscribed on thread= "
                    + Thread.currentThread().getId());

        }
        @Override
        public void onNext(Long aLong) {
            System.out.println("Next value " + aLong + " on thread "
                    + Thread.currentThread().getId());

        }

        @Override
        public void onError(Throwable e) {
            System.out.println("Error" + e.getMessage() + "on thread "
                    + Thread.currentThread().getId());
        }

        @Override
        public void onComplete() {
            System.out.println("Completed on thread "
                    + Thread.currentThread().getId());
        }
    }

    public static Observable<Long> timedCountDown() {
        return Observable.range(1,20)
                .map(i -> 20 - i +1)
                .doOnNext(i ->
                        System.out.println("On map" + i + ", " +
                        "thread= " + Thread.currentThread().getId()))
                .flatMap( i ->
                        Observable.timer(i, TimeUnit.SECONDS)
                        .map(l -> (long)i));
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Main thread= " + Thread.currentThread().getId());
        Observable<Long> obs = timedCountDown();

        obs.subscribe(new ObserverLong());

        //obs.subscribe(new ObserverLong());

        System.in.read();
    }
}
