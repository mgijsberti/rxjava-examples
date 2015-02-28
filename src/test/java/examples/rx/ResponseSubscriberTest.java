package examples.rx;

import examples.rx.obs.ResponseSubscriber;
import org.junit.Before;
import org.junit.Test;
import rx.Observable;
import rx.Subscriber;

import java.util.List;


import static org.junit.Assert.assertEquals;

public class ResponseSubscriberTest {

    private Subscriber<Integer> subscriber;

    @Before
    public void setUp(){
        subscriber = new ResponseSubscriber();
    }

    @Test
    public void testResponseSubscriber(){
        Observable<Integer> observable;
        observable = Observable.create(
                subscriber -> {
                    subscriber.onNext(1);
                    subscriber.onNext(2);
                    subscriber.onNext(3);
                    subscriber.onCompleted();
                }
        );

        observable.subscribe(subscriber);

        ResponseSubscriber responseSubscriber = (ResponseSubscriber)subscriber;
        List<Integer> responses = responseSubscriber.getResponses();
        assertEquals(responses.size(), 3);
        System.out.println(responses);
    }

    @Test
    public void testConcatResponseSubscriber(){

        Observable<Integer> observable;
        observable = Observable.create(
                subscriber -> {
                    subscriber.onNext(1);
                    subscriber.onNext(2);
                    subscriber.onNext(3);
                    subscriber.onCompleted();
                }
        );

        Observable<Integer> observable2;

        observable2 = Observable.create(
                subscriber -> {
                    subscriber.onNext(4);
                    subscriber.onNext(5);
                    subscriber.onNext(6);
                    subscriber.onCompleted();

                }
        );

//        Observable.concat(observable,observable2);
//        observable2.concatWith(observable);

        observable.subscribe(subscriber);
        observable2.subscribe(subscriber);
        ResponseSubscriber responseSubscriber = (ResponseSubscriber)subscriber;
        List<Integer> responses = responseSubscriber.getResponses();
        assertEquals(responses.size(), 6);
        System.out.println(responses);

    }
}
