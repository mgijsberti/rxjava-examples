package examples.rx.search;

import org.junit.Test;

import java.util.List;
import static org.junit.Assert.assertEquals;

public class GoogleSearchTest {

    @Test
    public void testSearch() throws Exception{
        GoogleSearch googleSearch = new GoogleSearch("news");
        List<String> foundUrls = googleSearch.getUrls();
        assertEquals(8, foundUrls.size());
        for(String url : foundUrls){
            System.out.println(url);
        }
    }
}
