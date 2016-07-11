package Tests;

import Spider.Spider;
import org.testng.annotations.Test;

public class STGSiteTest {
	
	@Test
	public void STGSite() throws Exception {
		String startingUrl = "http://www.stgconsulting.com";
		Spider spider = new Spider(startingUrl);
		
		spider.addSiteURL("stgconsulting.com");
		spider.addWhiteListURL("www.softwaretechnologygroup.com");
		spider.addWhiteListURL("farm3.staticflickr.com");
		spider.addWhiteListURL("farm4.staticflickr.com");
		spider.addIgnoreURL("&");
		spider.addIgnoreURL("_page_id"); 
		
		String errors = spider.walkSite();
		
		System.out.println("Errors:\n" + errors);
	}
	
	@Test
	public void InvalidSite() throws Exception {
		Spider spider = new Spider("http://www.assdfsdfsdfdsdf.com/");
		
		spider.addSiteURL("http://www.assdfsdfsdfdsdf.com/");
		
		String errors = spider.walkSite();
		
		System.out.println("Errors:\n" + errors);
	}
}
