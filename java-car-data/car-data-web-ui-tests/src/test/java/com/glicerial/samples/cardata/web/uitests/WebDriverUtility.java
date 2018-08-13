package com.glicerial.samples.cardata.web.uitests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class WebDriverUtility {

    public WebDriver getNewWebDriver() {
        // May need following line if geckodriver not in your path
        // System.setProperty("webdriver.gecko.driver", "/usr/local/bin");

        FirefoxBinary firefoxBinary = new FirefoxBinary();
        firefoxBinary.addCommandLineOptions("--headless");
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setBinary(firefoxBinary);
        WebDriver webDriver = new FirefoxDriver(firefoxOptions);

        return webDriver;
    }

    public String getHomePage() {
        return "http://localhost:8082";
    }

    public String getAddCarPage() {
        return getHomePage() + "/addcar";
    }
}
