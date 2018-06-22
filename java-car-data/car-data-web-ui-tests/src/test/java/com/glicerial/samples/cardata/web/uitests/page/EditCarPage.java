package com.glicerial.samples.cardata.web.uitests.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class EditCarPage {

    private WebDriver driver;

    @FindBy(id = "year")
    private WebElement yearInput;

    @FindBy(id = "make")
    private WebElement makeInput;

    @FindBy(id = "model")
    private WebElement modelInput;

    @FindBy(id = "trimlevels")
    private WebElement trimLevels;

    @FindBy(id = "addtrimlevelbutton")
    private WebElement addTrimLevelButton;

    @FindBy(id = "submitcarbutton")
    private WebElement submitCarButton;

    public EditCarPage(WebDriver driver) {
        this.driver = driver;
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("trimlevels")));
        PageFactory.initElements(driver, this);
    }

    public void clickSubmitCarButton() {
        submitCarButton.click();

        WebDriverWait wait = new WebDriverWait(driver, 5);

        // Dismiss dialog that says "Car updated."
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();

        // Wait for javascript redirect
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='maincontent']//table[@id='carstable']")));
    }

    public void editYear(String year) {
        yearInput.clear();
        yearInput.sendKeys(year);
    }

    public void editMake(String make) {
        makeInput.clear();
        makeInput.sendKeys(make);
    }

    public void editModel(String model) {
        modelInput.clear();
        modelInput.sendKeys(model);
    }

    public WebElement getTrimLevels() {
        return trimLevels;
    }

    public void clickAddTrimLevelButton() {
        addTrimLevelButton.click();
    }
}
