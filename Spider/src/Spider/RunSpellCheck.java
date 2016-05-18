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

    private SpellChecker spellChecker;
    private ArrayList listOfMisspelledWords;

    @Test
    public void runSpellCheck(String url, String text, String spellFile, String jsonFile) throws Exception
    {
        createDictionary();
        spellChecker.addSpellCheckListener(this);

        // Run the test on imported output file
        String TEXT_FILE = readFile(text);
        StringWordTokenizer texTok = new StringWordTokenizer(TEXT_FILE, new TeXWordFinder());
        populateListOfMisspelledWords(texTok);
        printWordsInMisspelledList(url, spellFile, jsonFile);
    }

    private static String readFile(String path) throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded);
    }

    private void createDictionary() throws Exception
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

    private void writeToJson(String url, String jsonOutput) throws Exception {
        CreateJsonFile.writeJsonFile(url, listOfMisspelledWords, jsonOutput);
    }

    private void populateListOfMisspelledWords(StringWordTokenizer texTok)
    {
        listOfMisspelledWords = new ArrayList();
        spellChecker.checkSpelling(texTok);
    }

    private void printWordsInMisspelledList(String url, String spellFile, String jsonFile) throws Exception {
        for (Object misspelledWord : listOfMisspelledWords)
        {
            System.out.println("listOfMisspelledWords: " + misspelledWord);
        }
        saveToFile(url, spellFile);
        writeToJson(url, jsonFile);
    }

    public void spellingError(SpellCheckEvent event)
    {
        event.ignoreWord(true);
        listOfMisspelledWords.add(event.getInvalidWord());
    }

    private void saveToFile(String url, String outFile) throws Exception {
        try
        {
            OutputFileWriter.writeSpellingToNewFile(url, outFile, listOfMisspelledWords);
        }
        catch (IOException ex)
        {
            ex.getMessage();
        }
    }
}
