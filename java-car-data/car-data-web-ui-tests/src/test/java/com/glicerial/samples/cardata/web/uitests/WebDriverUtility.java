package com.glicerial.samples.cardata.web.uitests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class WebDriverUtility {

    public WebDriver getNewWebDriver() {
        // May need following line if geckodriver not in your path
        // System.setProperty("webdriver.gecko.driver", "/usr/local/bin");
        WebDriver webDriver = new FirefoxDriver();        

        return webDriver;
    }

    public String getHomePage() {
        return "http://localhost:8082";
    }

    public String getAddCarPage() {
        return getHomePage() + "/addcar";
    }
}
