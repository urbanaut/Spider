package enums;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import utils.TestBase;

/**
 * Created by bill.witt on 7/21/2016.
 */
public class MonsterEnums extends TestBase  {

    // Job Status Elements
    private static String jobStatusParent = "//select[@title='Jobs Statuses']/child::option";
    private static By fullTime = By.xpath(jobStatusParent + "[text()='Full Time']");
    private static By partTime = By.xpath(jobStatusParent + "[text()='Part Time']");
    private static By perDiem = By.xpath(jobStatusParent + "[text()='Per Diem']");

    public enum JobStatus {
        FULL_TIME,
        PART_TIME,
        PER_DIEM
    }

    public static void selectJobStatus(JobStatus status) {
        try {
            switch (status) {
                case FULL_TIME:
                    WebElement fullTimeStatusElem = driver.findElement(fullTime);
                    fullTimeStatusElem.click();
                    break;
                case PART_TIME:
                    WebElement partTimeStatusElem = driver.findElement(partTime);
                    partTimeStatusElem.click();
                    break;
                case PER_DIEM:
                    WebElement perDiemStatusElem = driver.findElement(perDiem);
                    perDiemStatusElem.click();
                    break;
                default:
                    System.out.println("Invalid status type chosen.");
                    break;
            }
        } catch (Exception e) {
            System.out.println("Error: Job status selection failed.");
            e.printStackTrace();
        }
    }

    // Years of Experience Elements
    private static String yearsOfExperienceParent = "//select[@title='Years of Experince']/child::option";
    private static By lessThanOneYear = By.xpath(yearsOfExperienceParent + "[text()='Less than 1 Year']");
    private static By oneToTwoYears = By.xpath(yearsOfExperienceParent + "[text()='1+ to 2 Years']");
    private static By twoToFiveYears = By.xpath(yearsOfExperienceParent + "[text()='2+ to 5 Years']");
    private static By fiveToSevenYears = By.xpath(yearsOfExperienceParent + "[text()='5+ to 7 Years']");
    private static By sevenToTenYears = By.xpath(yearsOfExperienceParent + "[text()='7+ to 10 Years']");
    private static By tenToFifteenYears = By.xpath(yearsOfExperienceParent + "[text()='10+ to 15 Years']");
    private static By moreThanFifteenYears = By.xpath(yearsOfExperienceParent + "[text()='More than 15 Years']");

    public enum YearsOfExperience {
        LESS_THAN_ONE_YEAR,
        ONE_T0_TWO_YEARS,
        TWO_TO_FIVE_YEARS,
        FIVE_TO_SEVEN_YEARS,
        SEVEN_TO_TEN_YEARS,
        TEN_TO_FIFTEEN_YEARS,
        MORE_THAN_FIFTEEN_YEARS
    }

    public static void selectYearsOfExperience(YearsOfExperience years) {
        try{
            switch(years) {
                case LESS_THAN_ONE_YEAR:
                    WebElement lessThanOneYearElem = driver.findElement(lessThanOneYear);
                    lessThanOneYearElem.click();
                    break;
                case ONE_T0_TWO_YEARS:
                    WebElement oneToTwoYearsElem = driver.findElement(oneToTwoYears);
                    oneToTwoYearsElem.click();
                    break;
                case TWO_TO_FIVE_YEARS:
                    WebElement twoToFiveYearsElem = driver.findElement(twoToFiveYears);
                    twoToFiveYearsElem.click();
                    break;
                case FIVE_TO_SEVEN_YEARS:
                    WebElement fiveToSevenYearsElem = driver.findElement(fiveToSevenYears);
                    fiveToSevenYearsElem.click();
                    break;
                case SEVEN_TO_TEN_YEARS:
                    WebElement sevenToTenYearsElem = driver.findElement(sevenToTenYears);
                    sevenToTenYearsElem.click();
                    break;
                case TEN_TO_FIFTEEN_YEARS:
                    WebElement tenToFifteenYearsElem = driver.findElement(tenToFifteenYears);
                    tenToFifteenYearsElem.click();
                    break;
                case MORE_THAN_FIFTEEN_YEARS:
                    WebElement moreThanFifteenYearsElem = driver.findElement(moreThanFifteenYears);
                    moreThanFifteenYearsElem.click();
                    break;
                default:
                    System.out.println("Invalid years of experience option.");
                    break;
            }
        } catch(Exception e) {
            System.out.println("Error: Years of experience selection failed.");
            e.printStackTrace();
        }
    }
}
