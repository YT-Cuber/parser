package org.ytcuber.parser;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ScheduleController {

    @GetMapping("/index")
    public String hello() {
        return "index";
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @GetMapping("/start")
    public String startPage() {
        return "welcumPage";
    }

    @PostMapping ("/start")
    public String startPageP() {
        return "nyan";
    }

    @GetMapping("/nyan")
    public String nyan() {
        return "nyan";
    }
}
