package examples.rx;

import examples.rx.search.GoogleSearch;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Application with examples of RxJava API
 * See
 *
 * http://blog.danlew.net/2014/09/15/grokking-rxjava-part-1/
 * http://blog.danlew.net/2014/09/22/grokking-rxjava-part-2/
 *
 */
public class RxJavaGrokkingExamples
{
    public static void main( String[] args ) throws Exception {
        //part 1
        //create observables
        simpleFromInput();
        withTimer();
        withCreate();
        withSubscriber();
        withJustAction();
        withJustLambda();

        //transformations
        withMapFunction();
        withMapLambda();
        withMapTransform();

        //part 2
        Observable<List<String>> search =  GoogleSearch.search("Java");
        withQueryFlatMap();
        withQueryFlatMapFilterWikiPedia();

    }



    /**
     * Example with Observable.from(input)
     */
    private static void simpleFromInput(){
        String method = new Object(){}.getClass().getEnclosingMethod().getName();
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
    private static void withTimer(){
        String method = new Object(){}.getClass().getEnclosingMethod().getName();
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
    private static void withCreate(){
        String method = new Object(){}.getClass().getEnclosingMethod().getName();
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
    private static void withSubscriber() {
        String aMethod = new Object(){}.getClass().getEnclosingMethod().getName();
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
     * Example how to use an action and subscribe an observable with the action.
     */
    private static void withJustAction(){
        String method = new Object(){}.getClass().getEnclosingMethod().getName();
        Action1<String> onNextAction = new Action1<String>() {
            @Override
            public void call(String s) {
                log(method,s,"action with 1 argument");
            }
        };
        Observable.just("Using just with action").subscribe(onNextAction);
    }

    /**
     * Example with Observable.just which is a shorter lambda notation then for example examplewithAction()
     */
    private static void withJustLambda(){
        String method = new Object(){}.getClass().getEnclosingMethod().getName();
        Observable.just("Using just with lambda ...")
                .subscribe(s -> log(method,s,""));
    }

    /**
     *  Example how to use map to chain the output of Observables with additional func1
     */
    private static void withMapFunction() {
        String method = new Object(){}.getClass().getEnclosingMethod().getName();
        Observable.just("Using just with map and function")
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return s + "--Function";
                    }
                })
                .subscribe(s -> log(method,s,"with function"));
    }

    /**
     *  Example how to use map to chain the output of Observables with lambda notation. This is a shorter version
     *  of withMapFunction()
     */
    private static void withMapLambda(){
        String method = new Object(){}.getClass().getEnclosingMethod().getName();
        Observable.just("Hello, world!")
                .map(s -> s + "-map1")
                .map(s -> s + "-map2")
                .subscribe(s -> log(method,s, ""));
    }

    /**
     * Example of map can transform streams into different types (from String to Integer to String).
     */
    private static void withMapTransform() {
        String method = new Object(){}.getClass().getEnclosingMethod().getName();
        Observable.just("Transform this!")
                .map(s -> s.hashCode())
                .map(i -> Integer.toString(i))
                .subscribe(s -> log(method,s, "transformed!"));
    }

    /**
     * Using flatMap to iterate over a List of urls returned by Google search on Java
     *
     * @throws Exception
     */
    private static void withQueryFlatMap() throws Exception {
        String method = new Object() {}.getClass().getEnclosingMethod().getName();
        search("Java")
                .flatMap(urls -> Observable.from(urls))
                .subscribe(url -> log(method,url,""));
    }

    /**
     * Using flatmap and filter to select urls from Wikipedia.
     */
    private static void withQueryFlatMapFilterWikiPedia() {
        String method = new Object() {}.getClass().getEnclosingMethod().getName();
        search("Hello world!")
                .flatMap(urls -> Observable.from(urls))
                .flatMap(url -> addWikiPrefix(url))
                .map(wurl -> startsWithWiki(wurl)) //map is used to convert Observable<String> to String
                .filter(furl -> furl != null)
                .subscribe(furl -> log(method, furl, ""));
    }

    private static Observable<List<String>> search(String text){
        try {
            return GoogleSearch.search(text);
        }catch(Exception e){
            logError("search",e.getMessage(),"");
        }
        return null;
    }

    static Observable<String> addWikiPrefix(String url){
        String wiki = "";
        if(url.contains("wikipedia.org")) {
            wiki = "Wiki:";
        }
        String[] r = {wiki + url};
        return Observable.from(r);
    }

    static String startsWithWiki(String url){
        if(url.startsWith("Wiki:")){
            return url;
        }
        return null;
    }

    static void log(String method, String label, String message){
        System.out.println(method + " - " + label + ": "+ message);
    }

    static void logError(String method, String label, String message){
        System.out.println(method + " - " + label + ": "+ message);
    }
}
