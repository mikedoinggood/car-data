package com.glicerial.samples.cardata.web.uitests;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.glicerial.samples.cardata.web.uitests.page.AddCarPage;
import com.glicerial.samples.cardata.web.uitests.page.CarDetailPage;
import com.glicerial.samples.cardata.web.uitests.page.ChartsPage;
import com.glicerial.samples.cardata.web.uitests.page.EditCarPage;
import com.glicerial.samples.cardata.web.uitests.page.LoginPage;
import com.glicerial.samples.cardata.web.uitests.page.MainPage;


public class LoginTest {
    private WebDriver driver;
    private WebDriverUtility webDriverUtility;
    private MainPage mainPage;

    @Before
    public void setup() {
        webDriverUtility = new WebDriverUtility();
        driver = webDriverUtility.getNewWebDriver();
    }

    @Test
    public void loginTest() {
        login("user", "password");
        driver.findElement(By.linkText("Logged in as user"));
    }

    @Test
    public void invalidUsernameLoginTest() {
        login("abc", "password");
        assertLoginErrorMessage();
    }

    @Test
    public void invalidPasswordLoginTest() {
        login("user", "abc");
        assertLoginErrorMessage();
    }

    @Test
    public void logoutTest() {
        login("user", "password");
        logout();
    }

    @Test
    public void addCarPageLoginTest() {
        driver.get(webDriverUtility.getAddCarPageUrl());

        try {
            AddCarPage addCarPage = new AddCarPage(driver);
        } catch (TimeoutException ex) {
            System.out.println("Could not load add car page.");
        }

        login("user", "password");
        driver.get(webDriverUtility.getAddCarPageUrl());
        AddCarPage addCarPage = new AddCarPage(driver);
        System.out.println("Add car page loaded.");
    }

    @Test
    public void ChartsPageLoginTest() {
        driver.get(webDriverUtility.getChartsPageUrl());

        try {
            ChartsPage chartsPage = new ChartsPage(driver);
        } catch (TimeoutException ex) {
            System.out.println("Could not load charts page.");
        }

        login("user", "password");
        driver.get(webDriverUtility.getChartsPageUrl());
        ChartsPage chartsPage = new ChartsPage(driver);
        System.out.println("Charts page loaded.");
    }

    @Test
    public void carDetailPageLoginTest() {
        loginAndAddCar();
        List<WebElement> carRows = mainPage.getCarRows();
        String firstCarDetailLink = carRows.get(0).findElement(By.tagName("a")).getAttribute("href");

        driver.get(firstCarDetailLink);
        CarDetailPage carDetailPage = new CarDetailPage(driver);
        System.out.println("Car detail page loaded.");

        logout();
        driver.get(firstCarDetailLink);

        try {
            carDetailPage = new CarDetailPage(driver);
        } catch (TimeoutException ex) {
            System.out.println("Could not load car detail page.");
        }
    }

    @Test
    public void editCarPageLoginTest() {
        loginAndAddCar();
        List<WebElement> carRows = mainPage.getCarRows();
        String firstCarDetailLink = carRows.get(0).findElement(By.tagName("a")).getAttribute("href");

        driver.get(firstCarDetailLink);
        CarDetailPage carDetailPage = new CarDetailPage(driver);
        System.out.println("Car detail page loaded.");

        String firstCarEditLink = carDetailPage.getEditCarLink().getAttribute("href");
        driver.get(firstCarEditLink);
        EditCarPage editCarPage = new EditCarPage(driver);
        System.out.println("Edit car page loaded.");

        logout();
        driver.get(firstCarEditLink);

        try {
            editCarPage = new EditCarPage(driver);
        } catch (TimeoutException ex) {
            System.out.println("Could not load edit car page.");
        }
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    private void login(String username, String password) {
        driver.get(webDriverUtility.getHomePageUrl());
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username, password);
        System.out.println("Logged in.");
    }

    private void loginAndAddCar() {
        driver.get(webDriverUtility.getHomePageUrl());
        login("user", "password");
        mainPage = new MainPage(driver);
        mainPage.clickAddCarLink();
        AddCarPage addCarPage = new AddCarPage(driver);

        // Adding car just to make sure at least one car is present
        Map<String, String> carMap = new HashMap<String, String>();
        carMap.put("year", "2017");
        carMap.put("make", "Honda");
        carMap.put("model", "Civic");
        carMap.put("trimLevels", "");

        addCarPage.addCar(carMap);
        System.out.println("Added car.");
    }

    private void logout() {
        driver.findElement(By.linkText("Logged in as user")).click();
        driver.findElement(By.linkText("Logout")).click();
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("loginform")));
        System.out.println("Logged out.");
    }

    private void assertLoginErrorMessage() {
        WebElement errorMessage = driver.findElement(By.className("text-danger"));
        assertEquals("Invalid username or password.", errorMessage.getText());
    }
}
