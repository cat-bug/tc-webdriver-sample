package com.endava.tc;

import cucumber.api.java.After;
import org.openqa.selenium.remote.RemoteWebDriver;

public class Hooks {

    @After
    public void tearDown(){
        RemoteWebDriver driver = (RemoteWebDriver)ScenarioContext.getInstance().getData("driver");
        driver.quit();
    }
}
