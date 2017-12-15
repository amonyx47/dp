package sk.uniba.fmph.dp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

@Data
@AllArgsConstructor
public class DriverFunctions {

    public static List<WebElement> getAllLinks(WebDriver driver, String url) {
        driver.get(url);

        return driver.findElements(By.tagName("a"));
    }

}
