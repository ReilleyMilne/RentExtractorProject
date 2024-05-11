package GeneralWebsiteFiles;

import java.util.Random;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import RentExtractionFiles.RandomUserAgent;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;

/**
 * Manages general tasks related to website interactions.
 */
public class GeneralWebsiteManager {
    private static Random rand = new Random();

    /**
    * Sets up and initializes the ChromeDriver with specific configurations.
    * 
    * Uses the RandomUserAgent class to get a random user agent.
    *
    * This method configures the ChromeOptions for the ChromeDriver with various arguments,
    * including setting it to run in headless mode, defining window size, specifying user-agent,
    * language, vendor, platform, WebGL vendor, renderer, and fixing hairline issues.
    * 
    * @return An instance of ChromeDriver initialized with the configured ChromeOptions.
    */
    public static ChromeDriver setupDriver(){
        // Setup for the driver
        ChromeOptions options = new ChromeOptions();

        options.addArguments("--headless=new");
        options.addArguments("--window-size=1920,1086");
        options.addArguments("user-agent=" + RandomUserAgent.getRandomUserAgent());
        options.addArguments("--lang=en-US", "--lang=en", "--vendor=Google Inc.",
                             "--platform=Win32", "--webgl_vendor=Intel Inc.",
                             "--renderer=Intel Iris OpenGL Engine", "--fix-hairline");

        return new ChromeDriver(options);
    }

    /**
    * Clicks on the specified WebElement and switches to the new window if opened.
    * 
    * This method executes a click action on the provided button WebElement using JavaScript.
    * After clicking, it waits for a random duration between 2000 to 3000 milliseconds to ensure the page loads.
    * It then switches to the new window handle if a new window is opened.
    * 
    * @param button The WebElement representing the button to click.
    * @param driver The WebDriver instance used to interact with the web page.
    * @throws InterruptedException If the thread is interrupted while waiting.
    */
    public static void clickAndSwitch(WebElement button, WebDriver driver) throws InterruptedException{
        if(button != null){
            JavascriptExecutor js = (JavascriptExecutor)driver;
            js.executeScript("arguments[0].click();", button);

            Thread.sleep(rand.nextInt(3001 - 2000) + 2000);

            for(String webHandle : driver.getWindowHandles()){
                driver.switchTo().window(webHandle);
            }
        }
    }
}
