import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.text.RandomStringGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import static org.apache.commons.text.CharacterPredicates.DIGITS;
import static org.apache.commons.text.CharacterPredicates.LETTERS;
import static org.junit.Assert.*;

public class AddCarsTest {

    private WebDriver driver;
    private List<Map<String, String>> carMapList = new ArrayList<Map<String, String>>();

    @Before
    public void setup() {
        // Setup cars to add
        Map<String, String> carMap1 = new HashMap<String, String>();
        carMap1.put("year", "2017");
        carMap1.put("make", "Honda");
        carMap1.put("model", "Civic");
        carMap1.put("trimLevels", generateRandomTrimLevels());
        carMap1.put("found", "false");
        carMapList.add(carMap1); 

        Map<String, String> carMap2 = new HashMap<String, String>();
        carMap2.clear();
        carMap2.put("year", "2016");
        carMap2.put("make", "Toyota");
        carMap2.put("model", "Corolla");
        carMap2.put("trimLevels", generateRandomTrimLevels());
        carMap2.put("found", "false");
        carMapList.add(carMap2); 

        Map<String, String> carMap3 = new HashMap<String, String>();
        carMap3.clear();
        carMap3.put("year", "2013");
        carMap3.put("make", "Ford");
        carMap3.put("model", "Explorer");
        carMap3.put("trimLevels", generateRandomTrimLevels());
        carMap3.put("found", "false");
        carMapList.add(carMap3); 

        // May need following line if geckodriver not in your path
        // System.setProperty("webdriver.gecko.driver", "/usr/local/bin");
        driver = new FirefoxDriver();
        driver.get("http://localhost:8082");
    }

    @Test
    public void doAddCarsTest() {
        login();
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
            driver.findElement(By.linkText("Add Car")).click();
            AddCarPage addCarPage = new AddCarPage(driver);
            addCarPage.addCar(carMap);
            System.out.println("Added car: " + getCarString(carMap));
        }
    }

    private void checkCars() {
        MainPage mainPage = new MainPage(driver);
        List<WebElement> carRows = mainPage.getCarRows();

        for (WebElement row : carRows) {
            List<WebElement> rowColumns = row.findElements(By.xpath(".//td"));

            for (Map<String, String> carMap : carMapList) {
                if (checkMakeModelYear(rowColumns, carMap) && checkTrimLevels(rowColumns, carMap)) {
                    carMap.put("found", "true");
                    System.out.println("Found car: " + getCarString(carMap));
                    break;
                }
            }
        }

        for (Map<String, String> carMap : carMapList) {
            assertTrue("Could not find car: " + getCarString(carMap), Boolean.valueOf(carMap.get("found")));
        }
    }

    private boolean checkMakeModelYear(List<WebElement> rowColumns, Map<String, String> carMap) {
        if (rowColumns.get(0).getText().equals(carMap.get("year")) &&
            rowColumns.get(1).getText().equals(carMap.get("make")) &&
            rowColumns.get(2).getText().equals(carMap.get("model"))) {

            return true;
        }

        return false;
    }

    private boolean checkTrimLevels(List<WebElement> rowColumns, Map<String, String> carMap) {
        String trimLevels = carMap.get("trimLevels");
        String[] trimLevelsArray = trimLevels.split("\n");

        for (String trimLevel : trimLevelsArray) {
            String tableTrimLevelsString = rowColumns.get(3).getText();            
            String regexString = ".*\\b" + Pattern.quote(trimLevel) + "\\b.*";

            if (!tableTrimLevelsString.matches(regexString)) {
                return false;
            }
        }

        return true;
    }

    private String getCarString(Map<String, String> carMap) {
        return carMap.get("year") + " " +
            carMap.get("make") + " " +
            carMap.get("model") + 
            " With trim levels: " + carMap.get("trimLevels").replaceAll("\n", ",");
    }

    private String generateRandomTrimLevels() {
        int length = 5;
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
            .withinRange('0', 'z')
            .filteredBy(LETTERS, DIGITS)
            .build();

        String randomTrim1 = generator.generate(length);
        String randomTrim2 = generator.generate(length);
        String randomTrim3 = generator.generate(length);

        return randomTrim1 + "\n" + randomTrim2 + "\n" + randomTrim3;
    }
}
