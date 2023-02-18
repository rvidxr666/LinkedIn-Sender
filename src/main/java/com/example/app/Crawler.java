package com.example.app;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
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

    public void startCrawling() throws InterruptedException {
        this.driver.get(this.url);
        this.loginToWebsite();
        this.foldMessenger();
        this.getPeople();
    }

    private void foldMessenger() {
        List<WebElement> lstOfButtons = this.findElementsByXPathWait(
                "//button[contains(@class, 'msg-overlay-bubble-header__control')]", 20);
        lstOfButtons.get(lstOfButtons.size()-1).click();
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
    private void getPeople() throws InterruptedException {
        this.searchBar();
        this.filterPeople();
        this.profileIteration();
    }

    private void profileIteration() throws InterruptedException {
        while (true) {
            List<WebElement> lstOfElems = this.findElementsByXPathWait("//a[contains(@class,'app-aware-link  scale-down ')]", 20);
            List<String> lstOfLinks= lstOfElems.stream().map((e) -> e.getAttribute("href")).toList();
            this.driver.navigate().refresh();
            String currUrl = this.driver.getCurrentUrl();

            for (int i = 0; i<lstOfLinks.size(); i++) {
                this.driver.get(lstOfLinks.get(i));

                try {
                    this.findElementByXPathWait("//div[@class='pvs-profile-actions ']//a", 20).click();
                    this.findElementByXPathWait("//input[@placeholder='Subject (optional)']", 20).sendKeys(data.getTopic());
                    this.findElementByXPathWait("//div[@aria-label='Write a message…']", 20)
                            .sendKeys(this.data.getMessage());
                } catch (TimeoutException e) {
                    System.out.println("Timed out!");
                }
            }

            // Main page
            this.driver.get(currUrl);
            Thread.sleep(4000);


            // Next
            this.scrollFunction();

            WebElement button;
            try {
                button  = this.findElementByXPathWait("//button[@aria-label='Next']", 20);
                button.click();
            } catch (NotFoundException e) {
                button = null;
            }

            if (button == null) {
                break;
            }
        }

    }

    private void scrollFunction() throws InterruptedException {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        driver.findElement(By.tagName("body")).sendKeys(Keys.END);
    }

    private void filterPeople() throws InterruptedException {
        this.findElementByXPathWait("//button[text()='People']", 20).click();
        this.findElementByXPathWait("//button[text()='Locations']", 20).click();
        this.inputRegion();
    }


    private void inputRegion() throws InterruptedException {
        while (true) {
            WebElement locationInput =  this.findElementByXPathWait(
                    "//input[@aria-label='Add a location']", 20);

            List<WebElement> formButtons =  this.findElementsByXPathWait(
                    "//input[@aria-label='Add a location']/ancestor::form//button", 20);

            locationInput.clear();
            locationInput.sendKeys(data.getCity() + ", " + data.getCountry()); // Add dynamic parameters
            Thread.sleep(2000);

            try {
                List<WebElement> lstOfRegions = this.findElementsByXPathWait(
                                "//div[contains(@class, 'triggered-content')]//div[contains(@id, 'basic-result')]", 20);
                WebElement targetRegion = this.findNeededRegion(lstOfRegions);
                targetRegion.click();

                WebElement enterButton = formButtons.stream().filter((elem) -> elem.getText().equals("Show results")).toList().get(0);
                enterButton.click();
                break;
            } catch (StaleElementReferenceException ignored) {
            } catch (MissingRegionException e) {
                throw new RuntimeException(e);
            }

        }

    }

    private WebElement findNeededRegion(List<WebElement> regionsList) throws MissingRegionException {
        for (WebElement region : regionsList) {
            String regionName = region.getText();
            if (regionName.contains(data.getCity()) && regionName.contains(data.getCountry())) {
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


