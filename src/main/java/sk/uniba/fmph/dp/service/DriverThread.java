package sk.uniba.fmph.dp.service;


import org.apache.commons.validator.routines.UrlValidator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import sk.uniba.fmph.dp.model.DriverFunctions;
import sk.uniba.fmph.dp.model.LinkModel;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DriverThread extends Thread {

    WebDriver driver = null;
    ArrayList<WebElement> allLinks = null;
    String url = null;

    public DriverThread(String name, String driverName, String url) throws Exception {
        this.url = url;
        if (driverName.equalsIgnoreCase("firefox")) {
            this.driver = new FirefoxDriver();
        } else if (driverName.equalsIgnoreCase("chrome")) {
            this.driver = new ChromeDriver();
        } else if (driverName.equalsIgnoreCase("ie")) {
            this.driver = new InternetExplorerDriver();
        } else if (driverName.equalsIgnoreCase("phantomjs")) {
            this.driver = new PhantomJSDriver();
        } else {
            throw new Exception("Driver not FF/Chrome/IE or PhantomJS!");
        }
    }

    @Override
    public void run() {
        this.allLinks = (ArrayList<WebElement>) DriverFunctions.getAllLinks(this.driver, url);
    }

    public ArrayList<LinkModel> getAllLinks() throws IOException {
        String[] schemes = {"http", "https"};
        UrlValidator urlValidator = new UrlValidator(schemes);

        ArrayList<LinkModel> result = new ArrayList<>();
        for (WebElement webLink : this.allLinks) {
            String validationResult = "";
            if (urlValidator.isValid(webLink.getAttribute("href"))) {
                validationResult = "Valid";
            } else {
                validationResult = "Invalid";
            }

            String response = "";
            try {
                URL url = new URL(webLink.getAttribute("href"));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                response = connection.getResponseMessage();
                connection.disconnect();
            } catch (Exception e) {
                System.out.println(e);
            }

            result.add(new LinkModel(webLink.getText(), webLink.getAttribute("href"), validationResult, response));
        }
        return result;
    }

    public void cleanUp() {
        this.driver.quit();
    }
}
