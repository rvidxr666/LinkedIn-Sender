package com.example.app;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.Arrays;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws Exception {

        System.setProperty("webdriver.chrome.driver", System.getenv("SELENIUM_PATH"));
        System.out.println(System.getProperty("user.dir"));

        CommandParserExtra parser = new CommandParserExtra(args);
        HashMap<String, String> userData = parser.parseArguments();

        // Command Line Arguments (maybe will be reimplemented)
//        CommandParser parser = new CommandParser(args);
//        HashMap<String, String> userData = parser.parseArguments();
//
//         // Selenium Logic
        Crawler crawler = new Crawler(
                new User(userData.get("-e"), userData.get("-p")),
                new Data(
                         userData.get("-d"),
                         userData.get("-t"),
                         userData.get("-f"),
                         userData.get("--country"),
                         userData.get("--city")
                    )
                );
        crawler.startCrawling();
    }


}
