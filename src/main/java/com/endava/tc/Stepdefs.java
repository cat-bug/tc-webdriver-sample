package com.endava.tc;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.testcontainers.containers.BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL;

public class Stepdefs {

    private RemoteWebDriver driver;

    public Stepdefs() throws IOException {
        InputStream input = new FileInputStream("src/main/resources/browser.properties");
        Properties prop = new Properties();
        prop.load(input);
        String browser = prop.getProperty("browser");
        Boolean incontainer = Boolean.valueOf(prop.getProperty("incontainer"));
        BrowserWebDriverContainer container = null;
        if (browser.equals("chrome")) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-notifications");
            if (!incontainer) {
                System.setProperty("webdriver.chrome.driver", "C:/drivers/chromedriver.exe");
                driver = new ChromeDriver(options);
            } else {
                container = new BrowserWebDriverContainer()
                        .withCapabilities(options)
                        .withRecordingMode(RECORD_ALL, new File("target"));
                container.start();
                driver = container.getWebDriver();
            }
        } else if (browser.equals("firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--disable-notifications");
            if (!incontainer) {
                System.setProperty("webdriver.gecko.driver", "C:/drivers/geckodriver.exe");
                driver = new FirefoxDriver();
            } else {
                container = (BrowserWebDriverContainer)new BrowserWebDriverContainer()
                        .withCapabilities(options)
                        .withRecordingMode(RECORD_ALL, new File("target"))
                        .withSharedMemorySize(2024L * FileUtils.ONE_MB);
                container.start();
                driver = container.getWebDriver();
            }
        }
        ScenarioContext.getInstance().saveData("container", container);
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
        driver.manage().timeouts().implicitlyWait(1500, TimeUnit.MILLISECONDS);
        List<WebElement> elements = driver.findElements(By.xpath("//span[contains(text(),'Communities and users')]/following-sibling::div/div/div/a/div[1]/div/div"));
        WebElement first = elements.get(0);
        Assert.assertEquals("Community name is correct", communityName, first.getText());
    }

}
