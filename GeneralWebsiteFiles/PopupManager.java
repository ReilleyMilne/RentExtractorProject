package GeneralWebsiteFiles;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.By;

import java.util.List;
import java.util.Random;

/**
 * This class manages and clears popups on web pages.
 */
public class PopupManager{
    
    /**
     * Iterates through all elements on the current webpage, checks if any elements contain 'close' or 'x'.
     * If so, clicks the button and returns true.
     * If not, does nothing and return false.
     * 
     * @return  True if popup was there, false if there was no popup.
     */
    private static boolean isPopup(WebDriver driver){
        // Gets clickable elements on the current webpage.
        List<WebElement> clickableElements = driver.findElements(By.xpath("//a | //button | //input[@type='submit'] | //input[@type='button']"));

        WebElement button = null;
        
        // Iterates through all clickable elements.
        for (WebElement element : clickableElements) {
            String elementText = element.getText().toLowerCase();
            if(elementText.contains("close") || elementText.contains("accept")){
                button = element;
                break;
            }
        }
        if(button != null){
            JavascriptExecutor js = (JavascriptExecutor)driver;
            js.executeScript("arguments[0].click();", button);
            return true;
        }
        return false;
    }

    /**
     * Runs a while loop to check that no popups are on the screen.
     * Uses the isPopup method to check.
     * Waits for 5 seconds between each check.
     * 
     * @throws InterruptedException
     */
    public static void clearPopups(WebDriver driver) throws InterruptedException {
        boolean popupCheck = true;
        Random rand = new Random();

        while(popupCheck){
            Thread.sleep(rand.nextInt(751 - 500) + 500);
            popupCheck = isPopup(driver);
        }
    }    
}