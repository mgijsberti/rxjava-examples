package examples.rx;

import examples.rx.obs.CountObservable;
import org.junit.Before;
import org.junit.Test;
import rx.Observable;
import rx.Observer;

import static org.mockito.Mockito.*;


public class CounterTest {

    Observable<Integer> observable;

    @Before
    public void setUp() {

        // Create a new observable that emits one integer on each subscribe.
        // The number indicates how many times the subscribe has been called.
        observable = Observable.create(new CountObservable(3));
    }

    @Test
    public void testCounter(){
        Observer observer1 =  mock(Observer.class);
        Observer observer2 =  mock(Observer.class);
        observable.subscribe(observer1);
        observable.subscribe(observer2);
        observable.subscribe(observer2);

        verify(observer1).onNext(4);
        verify(observer2).onNext(5);
        verify(observer2).onNext(6);
        verify(observer1).onNext(4);

    }

}
