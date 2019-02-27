package com.glicerial.samples.cardata.web.uitests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.glicerial.samples.cardata.web.uitests.page.AddCarPage;
import com.glicerial.samples.cardata.web.uitests.page.LoginPage;
import com.glicerial.samples.cardata.web.uitests.page.MainPage;

import static org.junit.Assert.*;

public class AddCarsTest {

    private WebDriver driver;
    private MainPage mainPage;
    private List<Map<String, String>> carMapList = new ArrayList<Map<String, String>>();
    private CarDataUtility carDataUtility = new CarDataUtility();

    @Before
    public void setup() {
        // Setup cars to add
        Map<String, String> carMap1 = new HashMap<String, String>();
        carMap1.put("year", "2017");
        carMap1.put("make", "Honda");
        carMap1.put("model", "Civic");
        setupTrimLevels(carMap1);
        carMap1.put("found", "false");
        carMapList.add(carMap1); 

        Map<String, String> carMap2 = new HashMap<String, String>();
        carMap2.clear();
        carMap2.put("year", "2016");
        carMap2.put("make", "Toyota");
        carMap2.put("model", "Corolla");
        setupTrimLevels(carMap2);
        carMap2.put("found", "false");
        carMapList.add(carMap2); 

        Map<String, String> carMap3 = new HashMap<String, String>();
        carMap3.clear();
        carMap3.put("year", "2013");
        carMap3.put("make", "Ford");
        carMap3.put("model", "Explorer");
        setupTrimLevels(carMap3);
        carMap3.put("found", "false");
        carMapList.add(carMap3); 

        for (Map<String, String> carMap : carMapList) {
            carMap.put("carString", carDataUtility.getCarString(carMap));
        }

        WebDriverUtility webDriverUtility = new WebDriverUtility();
        driver = webDriverUtility.getNewWebDriver();
        driver.get(webDriverUtility.getHomePageUrl());
    }

    @Test
    public void doAddCarsTest() {
        login();
        mainPage = new MainPage(driver);
        addCars();
        checkCars();
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    private void login() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("user", "password");
    }

    private void addCars() {
        for (Map<String, String> carMap : carMapList) {
            mainPage.clickAddCarLink();
            AddCarPage addCarPage = new AddCarPage(driver);
            addCarPage.addCar(carMap);
        }
    }

    private void checkCars() {
        Map<String, WebElement> carRows = mainPage.findMultipleCarRows(carMapList);

        for (Map<String, String> carMap : carMapList) {
            String carString = carMap.get("carString");
            assertNotNull("Could not find car: " + carString, carRows.get(carString));
        }
    }

    private void setupTrimLevels(Map<String, String> carMap) {
        CarDataUtility utility = new CarDataUtility();

        String randomTrim1 = utility.generateRandomTrimLevel();
        String randomTrim2 = utility.generateRandomTrimLevel();
        String randomTrim3 = utility.generateRandomTrimLevel();

        carMap.put("trimLevels", randomTrim1 + "\n" + randomTrim2 + "\n" + randomTrim3);
    }
}
