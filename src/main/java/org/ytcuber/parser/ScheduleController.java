package org.ytcuber.parser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.ytcuber.model.Lesson;
import org.ytcuber.repository.GroupRepository;
import org.ytcuber.repository.LessonRepository;

import java.util.List;

@Controller
public class ScheduleController {
    @Autowired
    private LessonRepository lessonRepository;

    public ScheduleController(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    @GetMapping("/start")
    public String startPage() {
        return "welcumPage";
    }

    @PostMapping("/start")
    public String startPageP(@RequestParam("subgroup") int subgroup, @RequestParam("group_text") String groupText, Model model) {
        System.err.println(groupText.toLowerCase());
        Long groupId = 1L;
        List<Lesson> lessonsForSelectedSubgroup = lessonRepository.getLessons(subgroup);
        model.addAttribute("lessons", lessonsForSelectedSubgroup);
        return "lessons";
    }

    @GetMapping("/index")
    public String hello() {
        return "index";
    }

    @GetMapping("/test")
    public String test() {
//        Iterable<Lesson> lessons = lessonRepository.findAll();
        return "test";
    }

    @GetMapping("/schedule")
    public String getSchedule(Model model) {
        List<Lesson> lessons = lessonRepository.findAll();
        model.addAttribute("lessons", lessons);
        return "schedule";
    }
    @GetMapping("/nyan")
    public String nyan() {
        return "nyan";
    }
}