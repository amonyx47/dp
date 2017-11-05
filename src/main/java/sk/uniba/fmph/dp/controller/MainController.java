package sk.uniba.fmph.dp.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;

@Controller
public class MainController {

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

}
