package Spider;

import java.util.List;

/**
 * Created by bill.witt on 7/11/2016.
 */
public class GsonBase {
    private List<String> misspelledWords;
    private String url;

    GsonBase(){}

    public GsonBase(List<String> misspelledWords, String url) {
        this.misspelledWords = misspelledWords;
        this.url = url;
    }

    public List<String> getMisspelledWords() {
        return misspelledWords;
    }
    public void setMisspelledWords(List<String> misspelledWords) {
        this.misspelledWords = misspelledWords;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public String toString() {
        return "[" + url + ", " + misspelledWords + "]";
    }

}
