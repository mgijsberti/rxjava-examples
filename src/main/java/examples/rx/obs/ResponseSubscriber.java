package examples.rx.obs;

import rx.Subscriber;

import java.util.ArrayList;
import java.util.List;


public class ResponseSubscriber extends Subscriber<Integer> {

    private List<Integer> responses = new ArrayList<>();

    public List<Integer> getResponses() {
        return responses;
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onNext(Integer integer) {
        request(integer.longValue());
        System.out.println("onNext [" + integer + "]");
        this.responses.add(integer);
    }

    @Override
    public String toString() {
        return "ResponseSubscriber{" +
                "responses=" + toStringResponses() +
                '}';
    }

    private String toStringResponses(){
        StringBuilder builder = new StringBuilder();
        for(Integer r: getResponses()){
            builder.append(r);
            builder.append("|");
        }
        return builder.toString();
    }

}
