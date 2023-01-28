package com.example.app;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.Arrays;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws Exception {

        System.setProperty("webdriver.chrome.driver", System.getenv("SELENIUM_PATH"));
        System.out.println(System.getProperty("user.dir"));

        // Command Line Arguments (maybe will be reimplemented)
        CommandParser parser = new CommandParser(args);
        HashMap<String, String> userData = parser.parseArguments();

         // Selenium Logic
        Crawler crawler = new Crawler(
                new User(userData.get("email"), userData.get("password")),
                new Data(userData.get("department"), userData.get("message"))
        );
        crawler.startCrawling();
    }


}
