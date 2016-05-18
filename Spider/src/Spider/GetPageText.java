package Spider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities;
import org.jsoup.select.Elements;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;


public class GetPageText {

    public void extractText(String url, String textFile) throws Exception
    {
        // Extract text from web page and save into file
        try
        {
            Document doc = Jsoup.connect(url).get();

            Document.OutputSettings settings = doc.outputSettings();
            settings.prettyPrint(false);
            settings.escapeMode(Entities.EscapeMode.extended);
            settings.charset("UTF-8");

            Elements paragraphs = doc.select("p");

            System.out.println("EXTRACTING TEXT...");
            File file = new File(textFile);
            if(!file.exists())
            {
                for(Element p : paragraphs)
                    System.out.println(p.text()+ " ");
                try (Writer out = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(textFile), "UTF-8")))
                {
                    out.write("\nURL: " + url + "\n");
                    for (Element p : paragraphs) {
                        out.write(p.text() + " ");
                    }
                    out.close();
                }
            } else
            {
                BufferedWriter bw = null;
                try
                {
                    bw = new BufferedWriter(new FileWriter(textFile, true));
                    bw.newLine();
                    bw.write("\nURL: " + url + "\n");
                    for (Element p : paragraphs) {
                        bw.write(p.text() + " ");
                    }
                    bw.newLine();
                    bw.flush();
                    bw.close();
                } catch (IOException ioe)
                {
                    ioe.printStackTrace();
                } finally
                {
                    if (bw != null) try {
                        bw.close();
                    } catch (IOException ioe2) {
                        // ignore
                    }
                }
            }
        }
        catch (IOException ex)
        {
            ex.getMessage();
        }
        removeText(textFile);
    }

    private void removeText(String textFile) throws IOException {
        String footer = "«(.*?)@STGUtah";

        String content;
        content = new String(Files.readAllBytes(Paths.get(textFile)));
        content = content.replaceAll(footer,"");

        System.out.println("Removing footer...");
        try (Writer out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(textFile), "UTF-8")))
        {
            out.write(content);
            out.close();
        }
    }
}
