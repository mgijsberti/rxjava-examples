package examples.rx.obs;

import rx.Observable;
import rx.Subscriber;

/**
 * An observable that passes a counter to it's subscribers.
 */
public class CountObservable implements Observable.OnSubscribe<Integer>{

    private int counter = 0;

    public CountObservable(int counter) {
        this.counter = counter;
    }

    @Override
    public void call(Subscriber<? super Integer> subscriber) {
        counter++;
        System.out.println("call [" + counter + "]");
        subscriber.onNext(counter);
    }

}
