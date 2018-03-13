import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class AddCarPage {

    private WebDriver driver;

    @FindBy(id = "year")
    private WebElement yearInput;

    @FindBy(id = "make")
    private WebElement makeInput;

    @FindBy(id = "model")
    private WebElement modelInput;

    @FindBy(className = "trimlevel")
    private List<WebElement> trimLevelInputs;

    @FindBy(id = "addtrimlevelbutton")
    private WebElement addTrimLevelButton;

    @FindBy(id = "submitcarbutton")
    private WebElement submitCarButton;

    public AddCarPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void typeYear(String year) {
        yearInput.sendKeys(year);
    }

    public void typeMake(String make) {
        makeInput.sendKeys(make);
    }

    public void typeModel(String model) {
        modelInput.sendKeys(model);
    }

    public void typeTrimLevels(String trimLevels) {
        String[] trimLevelArray = trimLevels.split("\n");

        for (int i=1; i < trimLevelArray.length; i++) {
            addTrimLevelButton.click();
        }

        for (int i=0; i < trimLevelArray.length; i++) {
            trimLevelInputs.get(i).sendKeys(trimLevelArray[i]);
        }
    }

    public void clickSubmitCarButton() {
        submitCarButton.click();
    }

    public void addCar(Map<String, String> carMap) {
        typeYear(carMap.get("year"));
        typeMake(carMap.get("make"));
        typeModel(carMap.get("model"));
        typeTrimLevels(carMap.get("trimLevels"));

        clickSubmitCarButton();

        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();

        // Wait for javascript redirect
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='maincontent']//table[@id='carstable']")));
    }
}
