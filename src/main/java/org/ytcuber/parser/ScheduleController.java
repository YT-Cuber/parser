package org.ytcuber.parser;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
