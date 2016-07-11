package Spider;

import java.util.List;

/**
 * Created by bill.witt on 7/11/2016.
 */
public class GsonBase {
    private List<String> MisspelledWords;
    private String URL;

    GsonBase(){}

    public void setUrl(String url) {
        this.URL = url;
    }

    public void setMisspelledWords(List<String> misspelledWords) {
        this.MisspelledWords = misspelledWords;
    }

    public String toString() {
        return "[" + MisspelledWords + ", " + URL + "]";
    }

}
