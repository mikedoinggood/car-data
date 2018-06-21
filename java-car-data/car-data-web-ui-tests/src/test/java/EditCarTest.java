import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


import static org.junit.Assert.*;

public class EditCarTest {

    private WebDriver driver;
    private MainPage mainPage;
    private CarDataUtility utility;
    private Map<String, String> carMap;

    @Before
    public void setup() {
        utility = new CarDataUtility();

        // Setup car to add
        carMap = new HashMap<String, String>();
        carMap.put("year", "2017");
        carMap.put("make", "Honda");
        carMap.put("model", "Civic");
        setTrimLevels(carMap);

        WebDriverUtility webDriverUtility = new WebDriverUtility();
        driver = webDriverUtility.getNewWebDriver();
        driver.get(webDriverUtility.getHomePage());

        login();
        mainPage = new MainPage(driver);
        addCar();
    }

    @Test
    public void doEditCarTest() {
        mainPage.navigateToCarDetailPage(carMap);

        CarDetailPage carDetailPage = new CarDetailPage(driver);
        carDetailPage.clickEditCarLink();

        // Edit car
        carMap.put("year", "1991");
        carMap.put("make", "Toyota");
        carMap.put("model", "Corolla");
        setTrimLevels(carMap);

        EditCarPage editCarPage = new EditCarPage(driver);
        editCarPage.editYear(carMap.get("year"));
        editCarPage.editMake(carMap.get("make"));
        editCarPage.editModel(carMap.get("model"));
        editTrimLevels(editCarPage);
        editCarPage.clickSubmitCarButton();
        assertNotNull(mainPage.findCarRow(carMap));
    }

    @Test
    public void doAddTrimLevelTest() {
        mainPage.navigateToCarDetailPage(carMap);

        CarDetailPage carDetailPage = new CarDetailPage(driver);
        carDetailPage.clickEditCarLink();

        EditCarPage editCarPage = new EditCarPage(driver);
        addNewTrimLevel(editCarPage);
        editCarPage.clickSubmitCarButton();
        assertNotNull(mainPage.findCarRow(carMap));
    }

    @Test
    public void doDeleteTrimLevelTest() {
        mainPage.navigateToCarDetailPage(carMap);

        CarDetailPage carDetailPage = new CarDetailPage(driver);
        carDetailPage.clickEditCarLink();

        EditCarPage editCarPage = new EditCarPage(driver);
        deleteTrimLevel(editCarPage);
        editCarPage.clickSubmitCarButton();
        assertNotNull(mainPage.findCarRow(carMap));
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

    private void setTrimLevels(Map<String, String> carMap) {
        utility = new CarDataUtility();

        String randomTrim1 = utility.generateRandomTrimLevel();
        String randomTrim2 = utility.generateRandomTrimLevel();
        String randomTrim3 = utility.generateRandomTrimLevel();

        carMap.put("trimLevels", randomTrim1 + "\n" + randomTrim2 + "\n" + randomTrim3);
    }

    private void editTrimLevels(EditCarPage editCarPage) {
        String trimLevelString = carMap.get("trimLevels");
        String[] trimLevelsArray = trimLevelString.split("\n");
        WebElement trimLevels = editCarPage.getTrimLevels();
        List<WebElement> trimLevelInputGroups = trimLevels.findElements(By.className("input-group")); 

        for (int i=0; i<trimLevelsArray.length; i++) {
            WebElement trimLevel = trimLevelInputGroups.get(i);
            WebElement input = trimLevel.findElement(By.xpath(".//input"));
            input.clear();
            input.sendKeys(trimLevelsArray[i]);
        }
    }

    private void addNewTrimLevel(EditCarPage editCarPage) {
        // Add new trim level to carMap
        String trimLevelString = carMap.get("trimLevels");
        String newTrimLevel = utility.generateRandomTrimLevel();
        carMap.put("trimLevels", trimLevelString + "\n" + newTrimLevel);
        trimLevelString = carMap.get("trimLevels");
        String[] trimLevelsArray = trimLevelString.split("\n");

        // Enter the new trim level (last trim level item of trimLevelsArray)
        // Last trimLevelInputGroup is the input box for a new trim level
        int newTrimLevelIndex = trimLevelsArray.length - 1;
        editCarPage.clickAddTrimLevelButton();
        WebElement trimLevels = editCarPage.getTrimLevels();
        List<WebElement> trimLevelInputGroups = trimLevels.findElements(By.className("input-group"));
        WebElement newTrimLevelInputGroup = trimLevelInputGroups.get(newTrimLevelIndex);
        WebElement newTrimLevelInput = newTrimLevelInputGroup.findElement(By.xpath(".//input"));
        newTrimLevelInput.sendKeys(trimLevelsArray[newTrimLevelIndex]);
    }

    private void deleteTrimLevel(EditCarPage editCarPage) {
        // Arbitrarily deleting first trim level
        String trimLevelString = carMap.get("trimLevels");
        String[] trimLevelsArray = trimLevelString.split("\n");
        String trimLevelToDelete = trimLevelsArray[0];
        System.out.println("Trim level to delete: " + trimLevelToDelete);
        carMap.put("trimLevels", trimLevelsArray[1] + "\n" + trimLevelsArray[2]);

        WebElement trimLevels = editCarPage.getTrimLevels();
        List<WebElement> trimLevelInputGroups = trimLevels.findElements(By.className("input-group"));

        for (WebElement trimLevelInputGroup : trimLevelInputGroups) {
            WebElement trimLevelInput = trimLevelInputGroup.findElement(By.xpath(".//input"));
            String trimLevelText = trimLevelInput.getAttribute("value");

            if (trimLevelToDelete.equals(trimLevelText)) {
                trimLevelInputGroup.findElement(By.xpath(".//button")).click();
            }
        }
    }
}
