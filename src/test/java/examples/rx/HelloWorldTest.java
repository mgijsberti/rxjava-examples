package examples.rx;

import examples.rx.obs.HelloWorldObservable;
import org.junit.Before;
import org.junit.Test;
import rx.Observable;
import rx.Observer;

import static org.mockito.Mockito.*;

/**
 * Example with how to mock an Observable with Mockito
 */
public class HelloWorldTest {

    private Observable observable;

    @Before
    public void setUp(){
        observable = Observable.create(new HelloWorldObservable());
    }

    @Test
    public void testHelloWorld(){
        Observer observer = mock(Observer.class);
        observable.subscribe(observer);

        verify(observer).onNext("Hello");
        verify(observer).onNext("world!");
    }
}
