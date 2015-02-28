package examples.rx.obs;

import rx.Observable;
import rx.Subscriber;

public class HelloWorldObservable implements Observable.OnSubscribe<String> {

    @Override
    public void call(Subscriber<? super String> subscriber) {
        subscriber.onNext("Hello");
        subscriber.onNext("world!");
        subscriber.onCompleted();
    }
}
