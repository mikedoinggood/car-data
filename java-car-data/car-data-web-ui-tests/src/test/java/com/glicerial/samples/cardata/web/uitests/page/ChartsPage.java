package com.glicerial.samples.cardata.web.uitests.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class ChartsPage {

    private WebDriver driver;

    @FindBy(id = "charts")
    private WebElement chartsDiv;

    public ChartsPage(WebDriver webDriver) {
        this.driver = webDriver;
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("charts")));
        PageFactory.initElements(driver, this);
    }
}