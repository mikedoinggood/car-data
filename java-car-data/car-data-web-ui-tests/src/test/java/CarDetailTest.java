import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import static org.junit.Assert.*;

public class CarDetailTest {

    private WebDriver driver;
    private MainPage mainPage;
    private Map<String, String> carMap;

    @Before
    public void setup() {
        // Setup car to add
        carMap = new HashMap<String, String>();
        carMap.put("year", "2017");
        carMap.put("make", "Honda");
        carMap.put("model", "Civic");
        setupTrimLevels(carMap);

        // May need following line if geckodriver not in your path
        // System.setProperty("webdriver.gecko.driver", "/usr/local/bin");
        driver = new FirefoxDriver();
        driver.get("http://localhost:8082");
    }

    @Test
    public void doCarDetailTest() {
        login();
        mainPage = new MainPage(driver);
        addCar();

        mainPage.navigateToCarDetailPage(carMap);
        checkCarDetailPage();
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

    private void checkCarDetailPage() {
        CarDetailPage carDetailPage = new CarDetailPage(driver);
        assertEquals(carMap.get("year"), carDetailPage.getYear().getText());
        assertEquals(carMap.get("make"), carDetailPage.getMake().getText());
        assertEquals(carMap.get("model"), carDetailPage.getModel().getText());

        String[] trimLevelsArray = carMap.get("trimLevels").split("\n");
        String[] carDetailPageTrimLevelsArray = carDetailPage.getTrimLevels().getText().split("\n");
        Arrays.sort(trimLevelsArray);
        Arrays.sort(carDetailPageTrimLevelsArray);
        assertArrayEquals(trimLevelsArray, carDetailPageTrimLevelsArray);
    }

    private void setupTrimLevels(Map<String, String> carMap) {
        CarDataUtility utility = new CarDataUtility();

        String randomTrim1 = utility.generateRandomTrimLevel();
        String randomTrim2 = utility.generateRandomTrimLevel();
        String randomTrim3 = utility.generateRandomTrimLevel();

        carMap.put("trimLevels", randomTrim1 + "\n" + randomTrim2 + "\n" + randomTrim3);
    }
}
