package com.glicerial.samples.cardata.web.uitests;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import com.glicerial.samples.cardata.web.uitests.page.AddCarPage;
import com.glicerial.samples.cardata.web.uitests.page.CarDetailPage;
import com.glicerial.samples.cardata.web.uitests.page.LoginPage;
import com.glicerial.samples.cardata.web.uitests.page.MainPage;

import static org.junit.Assert.*;

public class DeleteCarTest {

    private WebDriver driver;
    private MainPage mainPage;
    private Map<String, String> carMap;
    private CarDataUtility carDataUtility = new CarDataUtility();

    @Before
    public void setup() {
        // Setup car to add
        carMap = new HashMap<String, String>();
        carMap.put("year", "2021");
        carMap.put("make", "Honda");
        carMap.put("model", "Civic");
        setupTrimLevels(carMap);
        carMap.put("carString", carDataUtility.getCarString(carMap));

        WebDriverUtility webDriverUtility = new WebDriverUtility();
        driver = webDriverUtility.getNewWebDriver();
        driver.get(webDriverUtility.getHomePageUrl());
    }

    @Test
    public void doDeleteCarTest() {
        login();
        mainPage = new MainPage(driver);
        addCar();

        mainPage.navigateToCarDetailPage(carMap);
        CarDetailPage carDetailPage = new CarDetailPage(driver);
        carDetailPage.clickDeleteCarButton();
        assertNull(mainPage.findCarRow(carMap));
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    private void login() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("user", "password");
    }

    private void addCar() {
        mainPage.clickAddCarLink();
        AddCarPage addCarPage = new AddCarPage(driver);
        addCarPage.addCar(carMap);
    }

    private void setupTrimLevels(Map<String, String> carMap) {
        CarDataUtility utility = new CarDataUtility();

        String randomTrim1 = utility.generateRandomTrimLevel();
        String randomTrim2 = utility.generateRandomTrimLevel();
        String randomTrim3 = utility.generateRandomTrimLevel();

        carMap.put("trimLevels", randomTrim1 + "\n" + randomTrim2 + "\n" + randomTrim3);
    }
}
