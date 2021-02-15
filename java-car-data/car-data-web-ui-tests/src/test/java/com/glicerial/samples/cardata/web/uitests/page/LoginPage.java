package com.glicerial.samples.cardata.web.uitests.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    @FindBy(id = "username")
    private WebElement userNameInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(xpath = "//form[@id='loginform']//input[@type='submit']")
    private WebElement signInButton;

    public LoginPage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public void typeUserName(String userName) {
        userNameInput.sendKeys(userName);
    }

    public void typePassword(String password) {
        passwordInput.sendKeys(password);
    }

    public void clickSignInButton() {
        signInButton.click();
    }

    public void login(String username, String password) {
        System.out.println("Logging in...");
        typeUserName(username);
        typePassword(password);
        clickSignInButton();
    }
}
