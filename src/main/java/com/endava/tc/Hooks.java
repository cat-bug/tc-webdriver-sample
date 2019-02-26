package com.endava.tc;

import cucumber.api.Result;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.lifecycle.TestDescription;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

import static org.testcontainers.containers.BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL;

public class Hooks {


    @Before
    public void setup() throws IOException {
        InputStream input = new FileInputStream("src/main/resources/browser.properties");
        Properties prop = new Properties();
        prop.load(input);
        String browser = prop.getProperty("browser");
        Boolean incontainer = Boolean.valueOf(prop.getProperty("incontainer"));

        if (incontainer) {
            BrowserWebDriverContainer container = startContainer(browser);
            ScenarioContext.getInstance().saveData("container", container);
            ScenarioContext.getInstance().saveData("driver", container.getWebDriver());
        } else {
            ScenarioContext.getInstance().saveData("driver", startLocalDriver(browser));
        }

    }

    @After
    public void tearDown(Scenario scenario) {
        RemoteWebDriver driver = ((RemoteWebDriver) ScenarioContext.getInstance().getData("driver"));
        driver.quit();
        BrowserWebDriverContainer container = ((BrowserWebDriverContainer) ScenarioContext.getInstance().getData("container"));
        if (container != null) {
            TestDescription testDescription = new TestDescription() {
                public String getTestId() {
                    return scenario.getId();
                }

                public String getFilesystemFriendlyName() {
                    return scenario.getName();
                }
            };
            Result.Type status = scenario.getStatus();
            container.afterTest(testDescription, status == Result.Type.PASSED ? Optional.empty() : Optional.of(new AssertionError()));
        }
    }

    private RemoteWebDriver startLocalDriver(String browser) {
        RemoteWebDriver driver;

        if (browser.equalsIgnoreCase("chrome")) {
            System.setProperty("webdriver.chrome.driver", "C:/drivers/chromedriver.exe");
            driver = new ChromeDriver(new ChromeOptions().addArguments("--disable-notifications"));
        } else {
            System.setProperty("webdriver.gecko.driver", "C:/drivers/geckodriver.exe");
            driver = new FirefoxDriver(new FirefoxOptions().addArguments("--disable-notifications"));
        }
        return driver;
    }

    private BrowserWebDriverContainer startContainer(String browser) {
        BrowserWebDriverContainer container = null;
        if (browser.equalsIgnoreCase("chrome")) {
            container = new BrowserWebDriverContainer()
                    .withCapabilities(new ChromeOptions().addArguments("--disable-notifications"))
                    .withRecordingMode(RECORD_ALL, new File("target"));
        } else {
            container = (BrowserWebDriverContainer) new BrowserWebDriverContainer()
                    .withCapabilities(new FirefoxOptions().addArguments("--disable-notifications"))
                    .withRecordingMode(RECORD_ALL, new File("target"))
                    .withSharedMemorySize(4024L * FileUtils.ONE_MB);
        }
        container.start();
        return container;
    }
}
