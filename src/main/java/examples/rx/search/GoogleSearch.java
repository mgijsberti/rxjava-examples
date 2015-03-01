package examples.rx.search;

import com.google.gson.Gson;
import rx.Observable;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class GoogleSearch {

    private String search;

    public GoogleSearch(String search) {
        this.search = search;
    }

    public List<String> getUrls() throws IOException {

        String google = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=";
        String charset = "UTF-8";

        URL url = new URL(google + URLEncoder.encode(search, charset) + "&rsz=8");
        Reader reader = new InputStreamReader(url.openStream(), charset);
        GoogleResults results = new Gson().fromJson(reader, GoogleResults.class);

        List<String> urls  = new ArrayList<>();
        for(GoogleResults.Result googleResults : results.getResponseData().getResults()){
            urls.add(googleResults.getUrl());
        }
        return urls;
    }

    public static Observable<List<String>> search(String text) throws Exception {
        return Observable.just(new GoogleSearch(text).getUrls());
    }
}
