package examples.rx;

import rx.Observable;
import rx.Subscriber;

import java.util.concurrent.TimeUnit;

/**
 * Application with examples of RxJava API
 *
 */
public class RxJavaGrokkingExamples
{
    public static void main( String[] args ){

        exampleSimpleFromInput();
        exampleWithTimer();
        exampleWithCreate();
        exampleWithSubscriber();
        exampleWithJust();
        exampleWithMap();

    }

    /**
     * Example with Observable.from(input)
     */
    private static void exampleSimpleFromInput(){
        String method = "exampleSimpleFromInput";
        String[] input = {"Hello", "world"};
        Observable<String> observable1 = Observable.from(input);

        observable1.subscribe(
                message -> log(method, "Observable A", message),
                error -> logError(method, "Observable A error", error.getMessage()),
                () -> log(method, "Observable A", "complete")
        );

        observable1.subscribe(
                message -> log(method, "Observable B" , message),
                error -> log(method,"Observable B" , error.getMessage()),
                () -> log(method,"Observable B","complete")
        );
    }

    /**
     * Example with Observable.timer
     */
    private static void exampleWithTimer(){
        String method = "exampleWithTimer";
        Observable<Long> observable2 = Observable.timer(1, TimeUnit.SECONDS);
        observable2.subscribe(
                message -> {
                    log(method,"Observable 2", "timer");
                }
        );
    }

    /**
     * Example with Observable.create
     *
     */
    private static void exampleWithCreate(){
        String method = "exampleWithCreate";
        Observable<String> observable3 = Observable.create(
                subscriber -> {
                    subscriber.onNext("Hello");
                    subscriber.onNext("world");
                    subscriber.onError(new Exception("bye"));
                    subscriber.onCompleted();
                }
        );

        observable3.subscribe(
                message -> log(method, "Observable 3", message),
                error -> log(method, "Observable 3", error.getMessage()),
                () -> log(method, "Observable 3", "complete!")
        );
    }

    /**
     *  Example with observable.subscribe(new Subscriber<String>(){...})
     */
    private static void exampleWithSubscriber() {
        String aMethod = "exampleWithSubscriber";
        Observable<String> observable = Observable.create(
                subscriber -> {
                    subscriber.onNext("x");
                    subscriber.onNext("x");
                    subscriber.onError(new Exception("bye"));
                    subscriber.onCompleted();
                }
        );

        observable.subscribe(new Subscriber<String>() {

            @Override
            public void onStart() {
                request(1);
                log(aMethod, "Start", "");
            }

            @Override
            public void onCompleted() {
                log(aMethod, "onCompleted", "");
            }

            @Override
            public void onError(Throwable throwable) {
                log(aMethod,"onError",throwable.getMessage());
            }

            @Override
            public void onNext(String s) {
                log(aMethod,"onNext",s);
                if ("x".equals(s)) {
                    request(2);
                } else {
                    request(1);
                }
            }
        });
    }

    /**
     * Example with Observable.just which is a shorter notation then for example exampleSimpleFromInput()
     */
    private static void exampleWithJust(){
        String method = "exampleWithJust";
        Observable.just("Using just ...")
                .subscribe(s -> log(method,s,""));
    }

    /**
     *  Example how to use map to chain the output of Observables
     */
    private static void exampleWithMap(){
        String method = "exampleWithMap";
        Observable.just("Hello, world!")
                .map(s -> s + "-map1")
                .map(s -> s + "-map2")
                .subscribe(s -> log(method,s, ""));
    }

    static void log(String method, String label, String message){
        System.out.println(method + " - " + label + ": "+ message);
    }

    static void logError(String method, String label, String message){
        System.out.println(method + " - " + label + ": "+ message);
    }
}
