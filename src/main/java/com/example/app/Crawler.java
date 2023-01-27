package com.example.app;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

public class Crawler {
    public String url = "https://www.linkedin.com/";
    public String email;
    public String password;
    public String department;
    public String message;
    public WebDriver driver;
    public ChromeOptions options;


    public Crawler(String email, String password, String department, String message) {
        this.options = this.setOptions();
        this.email = email;
        this.password = password;
        this.department = department;
        this.message = message;
        this.driver = this.initializeSelenium();
    }

    public void startCrawling() {
        this.driver.get(this.url);
        this.loginToWebsite();
//        String loginFormXpath = "//div[@class='sign-in-form__form-input-container']//input";
//        List<WebElement> elems =  driver.findElements(new By.ByXPath(loginFormXpath));
//        System.out.println(elems);
    }

    // filter by Country and Department
    private void filterPeopleByDep() {

    }
    private void loginToWebsite() {
        String loginFormXpath = "//div[@class='sign-in-form__form-input-container']//input";
        List<WebElement> inputFields =  driver.findElements(new By.ByXPath(loginFormXpath));
        inputFields.get(0).sendKeys(this.email);
        inputFields.get(1).sendKeys(this.password);

        String buttonXpath = "//button[@class='sign-in-form__submit-button']";
        WebElement submitButton =  driver.findElement(new By.ByXPath(buttonXpath));
        submitButton.click();
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
