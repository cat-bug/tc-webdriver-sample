package com.endava.tc;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.lifecycle.TestDescription;

import java.util.Optional;

public class Hooks {

    @After
    public void tearDown(Scenario scenario){
        BrowserWebDriverContainer container = ((BrowserWebDriverContainer)ScenarioContext.getInstance().getData("container"));
        RemoteWebDriver driver = container.getWebDriver();
        driver.quit();
        TestDescription testDescription = new TestDescription() {
            public String getTestId() {
                return scenario.getId();
            }

            public String getFilesystemFriendlyName() {
                return scenario.getName();
            }
        };
        container.afterTest(testDescription, Optional.empty());
    }
}
