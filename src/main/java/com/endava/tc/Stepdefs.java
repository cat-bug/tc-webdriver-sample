package com.endava.tc;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Stepdefs {

    @Given("home page is opened")
    public void openHomePage() {
        RemoteWebDriver driver = ((RemoteWebDriver) ScenarioContext.getInstance().getData("driver"));
        driver.get("https://www.reddit.com");
    }

    @When("user searches (.+)")
    public void userSearchesKeyword(String keyword) {
        WebElement search = ((RemoteWebDriver) ScenarioContext.getInstance().getData("driver")).findElement(By.xpath("//input[@type='search']"));
        search.sendKeys(keyword);
        search.sendKeys(Keys.ENTER);
        ScenarioContext.getInstance().saveData("keyword", keyword);
    }

    @Then("top community in search result is (.+)")
    public void topCommunityInSearchResultIsCommunity(String communityName) throws IOException {
        RemoteWebDriver driver = ((RemoteWebDriver) ScenarioContext.getInstance().getData("driver"));
        driver.manage().timeouts().implicitlyWait(1800, TimeUnit.MILLISECONDS);
        List<WebElement> elements = ((RemoteWebDriver) ScenarioContext.getInstance().getData("driver")).findElements(By.xpath("//span[contains(text(),'Communities and users')]/following-sibling::div/div/div/a/div[1]/div/div"));
        WebElement first = elements.get(0);
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrFile, new File("target/screenshot_" + ScenarioContext.getInstance().getData("keyword") + ".png"));
        Assert.assertEquals("Community name is correct", communityName, first.getText());
    }

}
