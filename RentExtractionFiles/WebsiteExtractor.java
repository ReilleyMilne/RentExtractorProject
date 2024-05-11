package RentExtractionFiles;

import java.util.NoSuchElementException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.List;
import java.util.Iterator;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import GeneralWebsiteFiles.CSVEditor;
import GeneralWebsiteFiles.GeneralWebsiteManager;
import GeneralWebsiteFiles.PopupManager;

public class WebsiteExtractor {
    private static WebDriver driver;
    private static Random rand = new Random();

    private static final String location = "bothell";
    private static final int minPrice = 1300;
    
    private static boolean clickedFloorPlanButton = false;
    private static int totalMinPrice = Integer.MAX_VALUE;
    
    public static void main(String[] args) throws InterruptedException, IOException {
        driver = GeneralWebsiteManager.setupDriver();

        //Create and open output file and reader.
        File file = new File("C:\\Users\\Reilley\\OneDrive\\Desktop\\VS Code Projects\\RentExtractor\\RentExtraction\\apartmentsdata.csv");
        CSVEditor writer = new CSVEditor(file);
        File inputFile = new File("C:\\Users\\Reilley\\OneDrive\\Desktop\\VS Code Projects\\RentExtractor\\RentExtraction\\urls.csv");
        FileReader fileReader = new FileReader(inputFile);
        CSVReader reader = new CSVReader(fileReader);

        List<String[]> urls = null;
        try{
            urls = reader.readAll();
        } catch(CsvException e){
            e.getStackTrace();
        }

        for(int i = 0; i < urls.size(); i++){
            String url = urls.get(i)[0];
            String tempPrice = setup(url);
            writer.addWebColumn(new String[] {url, tempPrice});
        }
        
        driver.quit();

        writer.sortByLength();
        reader.close();
    }

    public static String setup(String url) throws InterruptedException{
        driver.get(url);

        // Wait to load
        Thread.sleep(rand.nextInt(3001 - 2000) + 2000);

        PopupManager.clearPopups(driver);
        checkForButtons("floor");

        if(totalMinPrice != Integer.MAX_VALUE){
            String returnedMin = String.valueOf(totalMinPrice);
            totalMinPrice = Integer.MAX_VALUE;
            return returnedMin;
        }
        else{
            return "0";
        }
    }

    /**
     * Gets a list of clickable elements within the current webpage.
     * Clicks a button with text of partialButtonText and switches to the webpage of that button.
     * Clears popups on new page.
     * Updates totalMinPrice.
     * 
     * @param partialButtonText Can either be 'floor' or 'availability'
     * @throws InterruptedException
     */
    public static void checkForButtons(String partialButtonText) throws InterruptedException {
        List<WebElement> clickableElements = driver.findElements(By.xpath("//a | //button | //input[@type='submit'] | //input[@type='button']"));

        WebElement button = null;

        for (WebElement element : clickableElements) {
            String elementText = element.getText().toLowerCase();
            if(elementText.contains(partialButtonText) || elementText.contains("pricing")){
                button = element;
                break;
            }
        }
        if(!clickedFloorPlanButton){
            GeneralWebsiteManager.clickAndSwitch(button, driver);
            PopupManager.clearPopups(driver);
            clickedFloorPlanButton = true;
            checkPrices();            
            checkForButtons("availability");
        }
        else if(clickedFloorPlanButton){
            checkPrices();
            if(totalMinPrice == Integer.MAX_VALUE){
                GeneralWebsiteManager.clickAndSwitch(button, driver);
                PopupManager.clearPopups(driver);
                checkPrices();
            }
        }
    }

    public static void checkPrices() throws InterruptedException {
        int tempMin = 0;
        updatePrices(location);
        tempMin = totalMinPrice;
        updatePrices();
        if(tempMin > totalMinPrice && tempMin != Integer.MAX_VALUE){
            totalMinPrice = tempMin;
        }
    }

    /**
     * Finds all elements in the current webpage.
     * Checks elements text for numbers following '$'.
     * Compares those to every other number, returning the lowest one.
     * 
     * @throws InterruptedException
     */
    public static void updatePrices(){
        // Gets all text elements on a webpage.
        List<WebElement> textOptions = null;
        try
        {
            textOptions = driver.findElements(By.tagName("div"));
        }
        catch(NoSuchElementException e)
        {
            System.out.println("Error: " + e);
        }

        Iterator<WebElement> i = textOptions.iterator();

        while(i.hasNext()){
            String price = i.next().getText();
            int tempPrice = getMinInText(price);
            if((tempPrice < totalMinPrice || totalMinPrice == Integer.MAX_VALUE) && tempPrice > minPrice){
                totalMinPrice = tempPrice;
            }
        }
    }

    public static int getMinInText(String price){
        int min = 0;

        Pattern pattern = Pattern.compile("\\$([\\d,.]+)");
        Matcher matcher = pattern.matcher(price);

        // Iterate through matches and print the numbers
        while (matcher.find()) {
            // Extract the matched number
            String match = matcher.group(1);
            // Remove commas from the matched number and print it
            String amount = match.replaceAll(",", "");
            // Check if amount is less than the previous minimum.
            int intAmt = Integer.parseInt(amount);
            if((intAmt < min || min == 0) && intAmt > minPrice){
                min = intAmt;
            }
        }
        return min;
    }

    /**
     * Finds all elements in the current webpage.
     * Checks elements text for numbers following '$' and has bothell.
     * Compares those to every other number, returning the lowest one.
     * 
     * @throws InterruptedException
     */
    public static void updatePrices(String locLocation){
        // Gets all text elements on a webpage.
        List<WebElement> textOptions = null;
        try
        {
            textOptions = driver.findElements(By.tagName("div"));
        }
        catch(NoSuchElementException e)
        {
            System.out.println("Error: " + e);
        }

        Iterator<WebElement> i = textOptions.iterator();

        while(i.hasNext()){
            String price = i.next().getText();
            int tempPrice = getMinInText(price, locLocation);
            if((tempPrice < totalMinPrice || totalMinPrice == Integer.MAX_VALUE) && tempPrice > minPrice){
                totalMinPrice = tempPrice;
            }
        }
    }

    public static int getMinInText(String price, String locLocation){
        int min = 0;

        Pattern pattern = Pattern.compile("\\$([\\d,.]+)");
        Matcher matcher = pattern.matcher(price);

        int lastLocationIndex = -1;

        // Iterate through matches and print the numbers
        while (matcher.find()) {
            // Extract the matched number
            String match = matcher.group(1);
            // Remove commas from the matched number and print it
            String amount = match.replaceAll(",", "");
            // Check if amount is less than the previous minimum.
            int intAmt = Integer.parseInt(amount);

            int indexOfPrice = matcher.start();
            int indexOfBothell = price.toLowerCase().lastIndexOf(locLocation, indexOfPrice);
            int displacementofIndex = Math.abs(indexOfBothell-indexOfPrice);
    
            if(indexOfBothell != -1 && indexOfPrice > indexOfBothell && displacementofIndex <= 100) {
                // Check if the nearest Bothell is closer to the price
                if(lastLocationIndex == -1 || indexOfBothell > lastLocationIndex) {
                    lastLocationIndex = indexOfBothell;

                    if((intAmt < min || min == 0) && intAmt > minPrice){
                        min = intAmt;
                    }
                }
            }
        }
        return min;
    }
}