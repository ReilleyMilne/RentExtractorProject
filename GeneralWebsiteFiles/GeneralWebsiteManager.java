package GeneralWebsiteFiles;

import java.util.Random;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import RentExtractionFiles.RandomUserAgent;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;

public class GeneralWebsiteManager {
    private static Random rand = new Random();

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

        /*String proxy = "45.77.56.114:30205";  // Replace with your proxy details
        Proxy seleniumProxy = new Proxy();
        seleniumProxy.setHttpProxy(proxy)
                    .setFtpProxy(proxy)
                    .setSslProxy(proxy)
                    .setProxyType(ProxyType.MANUAL);
        options.setProxy(seleniumProxy);*/
    }

    /**
     * Clicks on the inputted button variable and switches to its window.
     * Waits 2-3 seconds after switching webpages.
     * 
     * @param button    A webelement, button, to be clicked on
     * @throws InterruptedException
     */
    public static void clickAndSwitch(WebElement button, WebDriver driver) throws InterruptedException{
        if(button != null){
            JavascriptExecutor js = (JavascriptExecutor)driver;
            js.executeScript("arguments[0].click();", button);

            Thread.sleep(rand.nextInt(3001 - 2000) + 2000);

            for(String webHandle : driver.getWindowHandles()){
                driver.switchTo().window(webHandle);
            }
            System.out.println(driver.getCurrentUrl());
        }
    }
}
