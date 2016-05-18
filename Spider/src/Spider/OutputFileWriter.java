package Spider;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by bill.witt on 5/18/2016.
 */
class OutputFileWriter {

    static void writeExtractionToNewFile(String url, String textFile, Elements elements) throws Exception
    {
        System.out.println("Extracting text...");
        File file = new File(textFile);
        if(!file.exists())
        {
            try (Writer out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(textFile), "UTF-8")))
            {
                out.write("\nURL: " + url + "\n");
                for (Element e : elements) {
                    out.write(e.text() + " ");
                }
                out.close();
            } catch (Exception e) {
                System.out.println("Failed to write to new file");
            }
        } else
        {
            writeExtractionToExistingFile(url, textFile, elements);
        }
    }

    private static void writeExtractionToExistingFile(String url, String textFile, Elements elements) throws Exception
    {
        BufferedWriter bw = null;
        try
        {
            bw = new BufferedWriter(new FileWriter(textFile, true));
            bw.newLine();
            bw.write("\nURL: " + url + "\n");
            for (Element e : elements) {
                bw.write(e.text() + " ");
            }
            bw.newLine();
            bw.flush();
            bw.close();
        } catch (IOException ioe)
        {
            System.out.println("Failed to write to existing file");
        } finally
        {
            if (bw != null) try {
                bw.close();
            } catch (IOException ioe2) {
                // ignore
            }
        }
    }

    static void writeSpellingToNewFile(String url, String textFile, ArrayList list) throws Exception
    {
        File file = new File(textFile);
        if(!file.exists())
        {
            try (Writer out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(textFile), "UTF-8"))) {
                out.write("\nURL: " + url + "\n");
                out.write("Misspelled Words: \n");
                for (Object misspelledWord : list) {
                    out.write(misspelledWord + ",");
                }
                out.close();
            }
        } else
        {
            writeSpellingToExistingFile(url, textFile, list);
        }
    }

    private static void writeSpellingToExistingFile(String url, String textFile, ArrayList list) throws Exception
    {
        BufferedWriter bw = null;
        try
        {
            bw = new BufferedWriter(new FileWriter(textFile, true));
            bw.newLine();
            bw.write("\nURL: " + url + "\n");
            bw.write("Misspelled Words: \n");
            for (Object misspelledWord : list) {
                bw.write(misspelledWord + ",");
            }
            bw.newLine();
            bw.flush();
            bw.close();
        } catch (IOException ioe)
        {
            System.out.println("Failed to write to existing file");
        } finally
        {
            if (bw != null) try {
                bw.close();
            } catch (IOException ioe2) {
                // just ignore it
            }
        }
    }
}
