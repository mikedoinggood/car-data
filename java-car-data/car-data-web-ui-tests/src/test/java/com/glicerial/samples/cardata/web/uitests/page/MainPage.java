package com.glicerial.samples.cardata.web.uitests.page;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.glicerial.samples.cardata.web.uitests.CarDataUtility;


public class MainPage {

    private WebDriver driver;
    private CarDataUtility carDataUtility = new CarDataUtility();

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
        List<WebElement> carRows = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//div[@class='maincontent']//table[@id='carstable']/tbody/tr"), 0));
        return carRows;
    }

    public void navigateToCarDetailPage(Map<String, String> carMap) {
        boolean navigated;

        try {
            WebElement carRow = findCarRow(carMap);
            List<WebElement> rowColumns = carRow.findElements(By.xpath(".//td"));
            rowColumns.get(0).findElement(By.tagName("a")).click();
            navigated = true;
            System.out.println("Going to detail page of car: " + carDataUtility.getCarString(carMap));
        } catch (NoSuchElementException e) {
            navigated = false;
        }

        assertTrue(navigated);
    }

    public WebElement findCarRow(Map<String, String> carMap) {
        List<WebElement> carRows = getCarRows();

        for (WebElement row : carRows) {
            List<WebElement> rowColumns = row.findElements(By.xpath(".//td"));
            if (checkMakeModelYear(rowColumns, carMap) && checkTrimLevels(rowColumns, carMap)) {
                return row;
            }
        }

        throw new NoSuchElementException();
    }

    public boolean checkMakeModelYear(List<WebElement> rowColumns, Map<String, String> carMap) {
        if (rowColumns.get(0).getText().equals(carMap.get("year")) &&
            rowColumns.get(1).getText().equals(carMap.get("make")) &&
            rowColumns.get(2).getText().equals(carMap.get("model"))) {

            return true;
        }

        return false;
    }

    public boolean checkTrimLevels(List<WebElement> rowColumns, Map<String, String> carMap) {
        String[] trimLevelsArray = carMap.get("trimLevels").split("\n");
        String[] mainPageTrimLevelsArray = rowColumns.get(3).getText().split(", ");
        Arrays.sort(trimLevelsArray);
        Arrays.sort(mainPageTrimLevelsArray);

        if (Arrays.equals(trimLevelsArray,mainPageTrimLevelsArray)) {
            return true;
        } else {
            return false;
        }
    }
}
