package sk.uniba.fmph.dp.controller;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import sk.uniba.fmph.dp.service.DriverThread;

import java.io.IOException;

@Controller
public class MainController {

    String pageTested = "http://www.facebook.com/";
    int maxConcurrentThreads = 100;

    @Value("${spring.application.name}")
    String appName;


    String status = HttpStatus.NOT_IMPLEMENTED.toString();

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        return "home";
    }

    @GetMapping("/error")
    public String errorPage(Model model){
        model.addAttribute("status", status);
        return "error";
    }

    @GetMapping("/links")
    public String linkTesting(Model model) throws Exception {


        DriverThread t1 = new DriverThread("firefoxDriver", "firefox", pageTested);
        DriverThread t2 = new DriverThread("chromeDriver", "chrome", pageTested);

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        model.addAttribute("pageTested", pageTested);
        long start = System.currentTimeMillis();
        model.addAttribute("firefoxLinks", t1.getAllLinks());
        long end = System.currentTimeMillis();
        model.addAttribute("firefoxLoadTime", end - start);
        start = end;
        model.addAttribute("chromeLinks", t2.getAllLinks());
        end = System.currentTimeMillis();
        model.addAttribute("chromeLoadTime", end - start);

        t1.cleanUp();
        t2.cleanUp();

        return "links";
    }

    @GetMapping("/validateHTML")
    public String validateHTML(Model model) {

        String response = null;
        String source = null;
        HttpResponse<String> uniResponse = null;
        try {
            source = Jsoup.connect(pageTested).get().html();
            uniResponse = Unirest.post("https://validator.w3.org/nu/")
                    .header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.101 Safari/537.36")
                    .header("Content-Type", "text/html; charset=UTF-8")
                    .queryString("out", "xml")
                    .body(source)
                    .asString();
        } catch (UnirestException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }
        response = uniResponse.getBody();

        model.addAttribute("pageTested", pageTested);
        model.addAttribute("response", response);

        return "validateHTML";
    }

    @GetMapping("/validateCSS")
    public String validateCSS(Model model) {

        model.addAttribute("pageTested", pageTested);

        return "validateCSS.html";
    }

    @GetMapping("validateJS")
    public String validateJS(Model model) {

        model.addAttribute("pageTested", pageTested);

        return "validateJS.html";
    }

}
