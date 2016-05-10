package Spider;

import com.swabunga.spell.SpellChecker;
import com.swabunga.spell.TeXWordFinder;
import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.event.SpellCheckEvent;
import com.swabunga.spell.event.SpellCheckListener;
import com.swabunga.spell.tokenizer.StringWordTokenizer;
import org.testng.annotations.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


public class RunSpellCheck implements SpellCheckListener {

    private static String readFile(String path) throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded);
    }

    private SpellChecker spellChecker;
    private ArrayList listOfMisspelledWords;

    @Test
    public void runSpellCheck(String url, String text, String outFile) throws IOException {

        createDictionary();
        spellChecker.addSpellCheckListener(this);

        // run the test on imported output file
        String TEXT_FILE = readFile(text);
        StringWordTokenizer texTok = new StringWordTokenizer(TEXT_FILE, new TeXWordFinder());
        populateListOfMisspelledWords(texTok);
        printWordsInMisspelledList(url, outFile);
    }

    private void createDictionary()
    {
        String DICTIONARY_FILE = "src\\resources\\dictionaries\\mainDict.txt";
        File dict = new File(DICTIONARY_FILE);
        try
        {
            spellChecker = new SpellChecker(new SpellDictionaryHashMap(dict));
        }
        catch (FileNotFoundException e)
        {
            System.err.println("Dictionary File '" + dict + "' not found! Quitting. " + e);
            System.exit(1);
        }
        catch (IOException ex)
        {
            System.err.println("IOException occurred while trying to read the dictionary file: " + ex);
            System.exit(2);
        }
    }

    private void populateListOfMisspelledWords(StringWordTokenizer texTok)
    {
        listOfMisspelledWords = new ArrayList();
        spellChecker.checkSpelling(texTok);
    }

    private void printWordsInMisspelledList(String url, String outFile)
    {
        for (Object misspelledWord : listOfMisspelledWords)
        {
            System.out.println("listOfMisspelledWords: " + misspelledWord);
        }
        saveToFile(url, outFile);
    }

    public void spellingError(SpellCheckEvent event)
    {
        event.ignoreWord(true);
        listOfMisspelledWords.add(event.getInvalidWord());
    }

    private void saveToFile(String url, String outFile) {
        try
        {
            File file = new File(outFile);
            if(!file.exists())
            {
                try (Writer out = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(outFile), "UTF-8"))) {
                    System.out.println("URL: " + url);
                    out.write("\nURL: " + url + "\n");
                    out.write("Misspelled Words: \n");
                    for (Object misspelledWord : listOfMisspelledWords) {
                        out.write(misspelledWord + ",");
                    }
                    out.close();
                }
            } else
            {
                BufferedWriter bw = null;
                try
                {
                    // APPEND MODE SET HERE
                    bw = new BufferedWriter(new FileWriter(outFile, true));
                    bw.newLine();
                    bw.write("\nURL: " + url + "\n");
                    bw.write("Misspelled Words: \n");
                    for (Object misspelledWord : listOfMisspelledWords) {
                        bw.write(misspelledWord + ",");
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
                        // just ignore it
                    }
                }
            }
        }
        catch (IOException ex)
        {
            ex.getMessage();
        }
    }
}
