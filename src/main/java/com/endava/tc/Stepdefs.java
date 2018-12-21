package com.endava.tc;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import org.junit.Rule;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;

import java.io.*;
import java.util.List;
import java.util.Properties;

import static org.testcontainers.containers.BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL;

public class Stepdefs {
    private RemoteWebDriver driver;

    @Rule
    public BrowserWebDriverContainer chrome = new BrowserWebDriverContainer()
            .withCapabilities(new ChromeOptions())
            .withRecordingMode(RECORD_ALL, new File("target"));



    public Stepdefs() throws IOException {

        InputStream input = new FileInputStream("src/main/resources/browser.properties");
        Properties prop = new Properties();
        prop.load(input);
        String browser = prop.getProperty("browser");
        if (browser.equals("chrome")) {
            System.setProperty("webdriver.chrome.driver", "C:/drivers/chromedriver.exe");
            driver = new ChromeDriver();
        } else if (browser.equals("firefox")) {
            System.setProperty("webdriver.gecko.driver", "C:/drivers/geckodriver.exe");
            driver = new FirefoxDriver();
//            chrome.start();
//            driver = chrome.getWebDriver();

        }
        ScenarioContext.getInstance().saveData("driver", driver);
    }


    @Given("home page is opened")
    public void openHomePage() {
        driver.get("https://www.reddit.com");
    }

    @When("user searches (.+)")
    public void userSearchesKeyword(String keyword) {
        WebElement search = driver.findElement(By.xpath("//input[@type='search']"));
        search.sendKeys(keyword);
        search.sendKeys(Keys.ENTER);
    }

    @Then("top community in search result is (.+)")
    public void topCommunityInSearchResultIsCommunity(String communityName) {
        List<WebElement> elements = driver.findElements(By.xpath("//span[contains(text(),'Communities and users')]/following-sibling::div/div/div/a"));
        WebElement first = elements.get(0);
        Assert.assertEquals("Community name is correct", communityName, first.getText());
    }
}
