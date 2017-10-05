import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MainPage {

    private WebDriver driver;

    @FindBy(linkText = "Add Car")
    private WebElement addCarLink;

    public MainPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void clickAddCarLink(){
        addCarLink.click();
        System.out.println("Clicked add car link");
    }

    public List<WebElement> getCarRows() {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        List<WebElement> carRows = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//div[@class='maincontent']//table/tbody/tr"), 0));
        return carRows;
    }
}
