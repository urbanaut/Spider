package Spider;

import com.google.gson.Gson;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bill.witt on 6/13/2016.
 */
public class CreateGsonFile {

    @Test
    public void writeJsonFile() throws Exception {
        Gson gson = new Gson();
        GsonBase gsonBase = new GsonBase();

        String url = "http://www.stgconsulting.com";
        List<String> wordList = new ArrayList<String>();
        wordList.add("thas");
        wordList.add("krime");
        wordList.add("theer");

        gsonBase.setUrl(url);
        gsonBase.setMisspelledWords(wordList);

        String jsonString = gson.toJson(gsonBase,GsonBase.class);
        System.out.println("Translated JSON: \n" + jsonString);

    }
}
