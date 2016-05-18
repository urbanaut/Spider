package Spider;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by bill.witt on 5/17/2016.
 */
class CreateJsonFile {

    @SuppressWarnings("unchecked")
    static void writeJsonFile(String url, ArrayList wordlist, String outFile) throws Exception {

        JSONObject obj = new JSONObject();
        JSONArray list = new JSONArray();

        for (Object misspelledWord : wordlist) {
            list.add(misspelledWord);
        }
        url = url.replace("//","");
        obj.put("Name", url);
        obj.put("Misspelled Words:", list);

        File file = new File(outFile);
        if (!file.exists())
        {
            try (Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8"))) {
                out.write(obj.toJSONString());
                System.out.println("Successfully Copied JSON Object to File...");
                System.out.println("\nJSON Object: " + obj);
            } catch (Exception e) {
                System.out.println("Failed to create new output file");
            }
        } else {
            BufferedWriter bw = null;
            try {
                bw = new BufferedWriter(new FileWriter(outFile, true));
                bw.write(obj.toJSONString());
                bw.flush();
                bw.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } finally {
                if (bw != null) try {
                    bw.close();
                } catch (IOException ioe2) {
                    // just ignore it
                }
            }
        }
    }
}
