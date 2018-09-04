package com.glicerial.samples.cardata.web.uitests.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class CarDetailPage {

    private WebDriver driver;

    @FindBy(id = "editlink")
    private WebElement editCarLink;

    @FindBy(id = "deletecarbutton")
    private WebElement deleteCarButton;

    @FindBy(id = "year")
    private WebElement year;

    @FindBy(id = "make")
    private WebElement make;

    @FindBy(id = "model")
    private WebElement model;

    @FindBy(id = "trimlevels")
    private WebElement trimLevels;

    public CarDetailPage(WebDriver webDriver) {
        this.driver = webDriver;
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("trimlevels")));
        PageFactory.initElements(driver, this);
    }

    public WebElement getYear() {
        return year;
    }

    public WebElement getMake() {
        return make;
    }

    public WebElement getModel() {
        return model;
    }

    public WebElement getTrimLevels() {
        return trimLevels;
    }

    public WebElement getEditCarLink() {
        return editCarLink;
    }

    public void clickEditCarLink() {
        editCarLink.click();
    }

    public void clickDeleteCarButton() {
        deleteCarButton.click();

        WebDriverWait wait = new WebDriverWait(driver, 5);

        // Confirm delete
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();

        // Dismiss dialog that says "Deleted."
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();

        // Wait for javascript redirect
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='maincontent']//table[@id='carstable']")));
    }
}
