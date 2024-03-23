package org.ytcuber.parser;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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

    @GetMapping("/nyan")
    public String nyan() {
        return "nyan";
    }
}
