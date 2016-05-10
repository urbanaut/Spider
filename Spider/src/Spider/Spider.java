package Spider;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Spider {
	
	//private static final String ValidURLPattern = "^https?://www\\.[A-Za-z0-9]+\\..+";
	// TODO: Refactor the walk section so there isn't repeated code with links/images
	// TODO: Maybe make it so users can configure what tags we look at on the page?
	// TODO: Make the return value of the 'walk' function something more than a straight String
	// TODO: Make spider a bit more resilient if the browser gets closed
	
	private String startURL = "";

	private final HashSet<String> whitelistedURLs; // List of allowed URLs
	private final HashSet<String> siteURLs;        // List of URLs in the target 'site'
	private final HashSet<String> ignoreURLs;      // List of URLs that should be ignored
	
	private FileWriter writer;
	private final Date date = new Date() ;
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss") ;
	private final String textFile = "src\\output\\extract_" + dateFormat.format(date) + ".txt";
	private final String spellFile = "src\\output\\spelling_" + dateFormat.format(date) + ".txt";

	
	private Spider()
	{
		whitelistedURLs = new HashSet<>();
		siteURLs = new HashSet<>();
		ignoreURLs = new HashSet<>();
		SetupFile();
	}
	
	/**
	 * Sets up a spider object with the given starting URL
	 * 
	 * @param startingURL A URL where the spider should start
	 */
	public Spider(String startingURL)
	{
		this();
		
		if (startingURL == null || startingURL.isEmpty())
			throw new InvalidParameterException("Starting URL must not be null or empty");
		
		startURL = startingURL;
	}

	// Ensures that writer is closed
	@Override
	protected void finalize() throws Throwable {
		try
		{
			CloseFile();
		} catch (Throwable t)
		{
			throw t;
		} finally {
			super.finalize();
		}
	}
	
	/**
	 * Add a URL that is 'safe' for the spider to visit
	 * Any site visited that isn't in the whitelist will cause an error to be generated
	 * 
	 * @param whiteListURL URL to add
	 */
	public void addWhiteListURL(String whiteListURL)
	{
		if (whiteListURL == null || whiteListURL.isEmpty())
			throw new InvalidParameterException("Whitelist URL must not be null or empty");

		whitelistedURLs.add(whiteListURL);
	}
	
	/**
	 * Add a URL to the list that the spider is meant to visit
	 * 
	 * @param siteURL URL to be added
	 */
	public void addSiteURL(String siteURL)
	{
		if (siteURL == null || siteURL.isEmpty())
			throw new InvalidParameterException("Site URL must not be null or empty");
		
		siteURLs.add(siteURL);
		whitelistedURLs.add(siteURL);
	}

	/**
	 * Adds a URL to the ignore list. If a target URL matches this list, it won't be considered
	 * 
	 * @param ignoreURL URL to be skipped
	 */
	public void addIgnoreURL(String ignoreURL)
	{
		if (ignoreURL == null || ignoreURL.isEmpty())
			throw new InvalidParameterException("Ignore URL must not be null or empty");
		
		ignoreURLs.add(ignoreURL);
	}
	
	/**
	 * Orders the spider to 'walk' through the given site.
	 * @return A String containing all of the errors found.
	 */
	public String walkSite() throws Exception
	{
		// Verify there isn't a problem with the starting URL
		if (startURL == null || startURL.isEmpty())
			return "Starting URL is null or empty.";
		
		String errors = "";
		
		Queue<Strand> toVisitURLs = new LinkedBlockingQueue<Strand>();
		HashSet<String> visitedURLs = new HashSet<>();

		System.setProperty("webdriver.chrome.driver", "src\\resources\\binaries\\32bit\\chrome\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		//WebDriver driver = new FirefoxDriver();
		
		toVisitURLs.add(new Strand ("", startURL));
		
		while (!toVisitURLs.isEmpty())
		{
			Strand visit = toVisitURLs.poll();
			System.out.println("Spider is now considering visiting:" + visit);
			
			// Check if we've visited this URL already
			if (visitedURLs.contains(visit.getDestination()))
			{
				System.out.println("Spider already visited that.");
			}
			else
			{
				// Haven't visited it, is it a site URL that we have to actually visit?
				if (hashSetPartialMatch(siteURLs, visit.getDestination()))
				{
					// Verify the link works 
					if (checkLinkBroken(visit.getDestination()))
					{
						errors = reportErrors("Tried to visit broken link: " + visit + "\n", errors);
					}
					
					System.out.println("Spider is now visiting: " + visit);
					driver.get(visit.getDestination());
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					System.out.println("Recognized site URL, spidering through page");
					
					// Scan page for images and links to check
					scanPage(driver, toVisitURLs, "a", "href");
					scanPage(driver, toVisitURLs, "img", "src");
					
					// Verify we got redirected to the right place
					if (!driver.getCurrentUrl().equals(visit.getDestination()))
					{
						errors = reportErrors("Link did not lead to where expected.\n" +
					              "Current URL: " + driver.getCurrentUrl() + " link was " + visit.getDestination() + " source was " + visit.getSource() + "\n", errors);
					}
					visitedURLs.add(visit.getDestination());

					// Extract all web page text and save to file
					GetPageText extract = new GetPageText();
					RunSpellCheck checker = new RunSpellCheck();
					extract.extractText(visit.getDestination(),textFile);

					// Run spell check on output text
					try {
						checker.runSpellCheck(visit.getDestination(),textFile,spellFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				else
				{
					// Make sure the link is still valid.
					if (checkLinkBroken(visit.getDestination()))
					{
						System.out.println("DEBUG: Broken link found.");
						errors = reportErrors("Broken link found: " + visit + "\n", errors);
					}

					// Is it in the whitelist?
					if (hashSetPartialMatch(whitelistedURLs, visit.getDestination()))
					{
						System.out.println("Targeted url is whitelisted: " + visit);
					}
					else
					{
						errors = reportErrors("Non-whitelisted URL was linked: " + visit + "\n", errors);
					}
					
					visitedURLs.add(visit.getDestination());
				}
			}
		}
		driver.close();
		driver.quit();

		return errors;
	}
	
	// Iterate through a set, and find if something partially matches the
	// passed in string.
	private boolean hashSetPartialMatch(HashSet<String> set, String toMatch)
	{
		for(String str : set)
		{
			System.out.println(toMatch + ".contains(" + str + ") == " + toMatch.contains(str));
			System.out.println(str + ".contains(" + toMatch + ") == " + str.contains(toMatch));
			System.out.println("Comparing |" + str + "|" + toMatch + "|");
			if (toMatch.contains(str) || str.contains(toMatch))
				return true;
		}
		
		return false;
	}
	
	// Perform a REST call to see if a link works
	private boolean checkLinkBroken(String url)
	{
		HttpURLConnection connection;
		
		System.out.println("DEBUG: checkLinkBroken visiting " + url);
		
		try {
			connection = (HttpURLConnection) new URL(url).openConnection();
			int response = connection.getResponseCode();
			
			System.out.println("DEBUG: Response code: " + response);
			
			// Check if response is in one of the 'acceptable' ranges
			return response < 200 || response >= 400;
		} catch (IOException e) {
			e.printStackTrace();
			return true;
		}		
	}

	// Scans the current driver page, extracting out URLs
	private void scanPage(WebDriver driver, Queue<Strand> toVisitURLs, String tagName, String targetAttribute)
	{
		List<WebElement> links = driver.findElements(By.tagName(tagName));
		
		for (WebElement link : links)
		{
			try
			{
				String target = link.getAttribute(targetAttribute);
				if (target!=null)
				System.out.println("Spider found possible link to visit: " + target);
				
				if (target != null && 
					(target.startsWith("http://") || target.startsWith("https://")) &&
					!hashSetPartialMatch(ignoreURLs, target)
				   )
				{
					System.out.println("Valid link found, adding to visit list: " + target);
					toVisitURLs.add(new Strand(driver.getCurrentUrl(),  target));
				}
			}
			catch (StaleElementReferenceException e)
			{
				// Don't really care about Stale Elements
			}
		}
	}
	
	private void SetupFile()
	{
		try
		{
			writer = new FileWriter(getFileName(), true);
		}
		catch (IOException e)
		{
			System.out.println("Failed to open file");
			e.printStackTrace();
		}		
	}
	
	private String getFileName()
	{
		LocalDateTime date = LocalDateTime.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("'Date'ddMMMyyyy.'Time'H.m.s.S");	
		
		return "SpiderRun" + date.format(format) + ".txt";
	}
	
	private String reportErrors(String errorToReport, String existingErrors)
	{
		if (writer != null)
		{
			try {
				writer.write(errorToReport);
				//writer.write(System.getProperty( "line.separator" ));
				writer.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return existingErrors + errorToReport;
	}
	
	private void CloseFile()
	{
		System.out.println("Commence closing");
		if (writer != null)
		{
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
