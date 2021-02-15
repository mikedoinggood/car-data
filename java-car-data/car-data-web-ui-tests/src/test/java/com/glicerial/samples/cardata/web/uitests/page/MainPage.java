package com.glicerial.samples.cardata.web.uitests.page;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Select;

import com.glicerial.samples.cardata.web.uitests.CarDataUtility;


public class MainPage {

    private WebDriver driver;
    private CarDataUtility carDataUtility = new CarDataUtility();

    public enum CarSortBy { MAKE, OLDEST, NEWEST }

    @FindBy(linkText = "Add Car")
    private WebElement addCarLink;

    public MainPage(WebDriver webDriver) {
        this.driver = webDriver;
        PageFactory.initElements(driver, this);
    }

    public void clickAddCarLink(){
        addCarLink.click();
        System.out.println("Clicked add car link");
    }

    public List<WebElement> getCarRows() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        List<WebElement> carRows = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//table[@id='carstable']/tbody/tr")));

        return carRows;
    }

    public void navigateToCarDetailPage(Map<String, String> carMap) {
        boolean navigated;

        try {
            WebElement carRow = findCarRow(carMap);
            List<WebElement> rowColumns = carRow.findElements(By.xpath(".//td"));
            rowColumns.get(0).findElement(By.tagName("a")).click();
            navigated = true;
            System.out.println("Going to detail page of car: " + carMap.get("carString"));
        } catch (NoSuchElementException e) {
            navigated = false;
        }

        assertTrue(navigated);
    }

    public WebElement findCarRow(Map<String, String> carMap) {
        System.out.println("Finding car row...");
        Boolean clickedNextPageLink = false;

        do {
            List<WebElement> carRows = getCarRows();

            for (WebElement row : carRows) {
                Map<String, String> rowCarMap = carRowToCarMap(row);

                if (carDataUtility.getCarString(rowCarMap).equals(carMap.get("carString"))) {
                    return row;
                }
          }

            clickedNextPageLink = clickNextPageLink();
        } while (clickedNextPageLink);

        return null;
    }

    public Map<String, WebElement> findMultipleCarRows(List<Map<String, String>> carMapList) {
        System.out.println("Finding multiple car rows...");
        Boolean clickedNextPageLink = false;
        int carMapListSize = carMapList.size();
        int foundCount = 0;

        Map<String, WebElement> foundCarRows = new HashMap<String, WebElement>();

        for (Map<String, String> carMap: carMapList) {
            foundCarRows.put(carMap.get("carString"), null);
        }

        do {
            List<WebElement> carRows = getCarRows();

            for (WebElement row : carRows) {
                Map<String, String> rowCarMap = carRowToCarMap(row);
                String rowCarString = carDataUtility.getCarString(rowCarMap) ;

                if (foundCarRows.containsKey(rowCarString)) {
                    foundCarRows.put(rowCarString, row);
                    System.out.println("Found car: " + rowCarString);
                    foundCount++;

                    if (foundCount == carMapListSize) {
                        return foundCarRows;
                    }
                }
            }

            clickedNextPageLink = clickNextPageLink();
        } while (clickedNextPageLink);

        return foundCarRows;
    }

    private boolean clickNextPageLink() {
        try {
            WebElement nextPageLink = driver.findElement(By.linkText("[Next]"));
            nextPageLink.click();
            System.out.println("Clicked next page link");
        } catch (NoSuchElementException ex) {
            return false;
        }

        return true;
    }

    public void selectCarSortBy(CarSortBy carSortBy) {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        Select carSortBySelect = new Select(wait.until(ExpectedConditions.presenceOfElementLocated(By.id("carsortby"))));

        switch(carSortBy) {
            case MAKE:
                carSortBySelect.selectByValue("make");
                break;
            case OLDEST:
                carSortBySelect.selectByValue("oldest");
                break;
            case NEWEST:
                carSortBySelect.selectByValue("newest");
                break;
        }
    }

    public Map<String, String> carRowToCarMap(WebElement row) {
        List<WebElement> rowColumns = row.findElements(By.xpath(".//td"));
        Map<String, String> carMap = new HashMap<String, String>();

        carMap.put("year", rowColumns.get(0).getText());
        carMap.put("make", rowColumns.get(1).getText());
        carMap.put("model", rowColumns.get(2).getText());
        carMap.put("trimLevels", rowColumns.get(3).getText().replaceAll(", ", "\n"));

        return carMap;
    }
}
