package com.example.app;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.List;

public class Crawler {
    public String url = "https://www.linkedin.com/";
    public Data data;
    public User user;
    public ChromeOptions options;
    public WebDriver driver;


    public Crawler(User user, Data data) {
        this.options = this.setOptions();
        this.user = user;
        this.data = data;
        this.driver = this.initializeSelenium();
    }

    public void startCrawling() throws MissingRegionException {
        this.driver.get(this.url);
        this.loginToWebsite();
        this.getPeople();


//        String loginFormXpath = "//div[@class='sign-in-form__form-input-container']//input";
//        List<WebElement> elems =  driver.findElements(new By.ByXPath(loginFormXpath));
//        System.out.println(elems);
    }

    private WebElement findElementByXPath(String Xpath) {
        WebElement targetElement =  this.driver.findElement(new By.ByXPath(Xpath));
        return targetElement;
    }

    private List<WebElement> findElementsByXPath(String Xpath) {
        List<WebElement> targetElements =  this.driver.findElements(new By.ByXPath(Xpath));
        return targetElements;
    }

    private WebElement findElementByXPathWait(String Xpath, Integer seconds) {
        WebDriverWait wait = new WebDriverWait(this.driver, Duration.ofSeconds(seconds));
        wait.until(ExpectedConditions.visibilityOfElementLocated(new By.ByXPath(Xpath)));
        WebElement targetElement =  this.driver.findElement(new By.ByXPath(Xpath));
        return targetElement;
    }

    private List<WebElement> findElementsByXPathWait(String Xpath, Integer seconds) {
        WebDriverWait wait = new WebDriverWait(this.driver, Duration.ofSeconds(seconds));
        wait.until(ExpectedConditions.visibilityOfElementLocated(new By.ByXPath(Xpath)));
        List<WebElement> targetElements =  this.driver.findElements(new By.ByXPath(Xpath));
        return targetElements;
    }

    // filter by Country and Department
    private void getPeople() throws MissingRegionException {
        this.searchBar();
        this.filterPeople();
    }


    private void filterPeople() throws MissingRegionException {
        this.findElementByXPathWait("//button[text()='People']", 20).click();
        this.findElementByXPathWait("//button[text()='Locations']", 20).click();

        // input Add a location
        WebElement locationInput =  this.findElementByXPathWait(
                "//input[@aria-label='Add a location']", 20);

        locationInput.sendKeys("Toronto, Canada"); // Need to add a country and city as an input parameter

        // Find the region options provided by LinkedIn
        this.findElementsByXPathWait(
                "//div[contains(@class, 'triggered-content')]//div[contains(@id, 'basic-result')]", 20)
                .get(0).click();

//        if (targetRegion == null) {
//            throw new MissingRegionException("Provided region is not supported!");
//        }
//
//        targetRegion.click();

//        lst.get(0).click();

        // Find the needed option
//        try {
//            WebElement region = this.findNeededElem(lst);
//            System.out.println(region.getText());
//        } catch (MissingRegionException e) {
//            throw new MissingRegionException(e.getMessage());
//        }


        // Button
//        this.findElementByXPathWait("//span[@text()='Show results']/parent::button", 20).click();
    }

    private WebElement findNeededElem(List<WebElement> regionsList) throws MissingRegionException {
        for (WebElement region : regionsList) {
            String regionName = region.getText();
            if (regionName.contains("Toronto") && regionName.contains("Canada")) {
                return region;
            }
        }
        throw new MissingRegionException("Can't find the region specified!");
    }

    private void searchBar() {
        WebElement searchBar = findElementByXPathWait("//input[@class='search-global-typeahead__input']", 20);
        searchBar.sendKeys(this.data.getDepartment());
        searchBar.sendKeys(Keys.ENTER);
    }

    private void loginToWebsite() {
        List<WebElement> inputFields = this.findElementsByXPath("//div[@class='sign-in-form__form-input-container']//input");
        inputFields.get(0).sendKeys(this.user.getEmail());
        inputFields.get(1).sendKeys(this.user.getPassword());
        this.findElementByXPath("//button[@class='sign-in-form__submit-button']").click();
    }

    private WebDriver initializeSelenium() {
        WebDriver driver = new ChromeDriver();
        driver.get(this.url);
        return driver;
    }

    private ChromeOptions setOptions() {
        ChromeOptions options = new ChromeOptions();
        System.out.println(System.getenv("SELENIUM_PATH"));
        options.setBinary(System.getenv("SELENIUM_PATH"));
        return options;
    }
}


