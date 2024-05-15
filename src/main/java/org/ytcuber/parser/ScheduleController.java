package org.ytcuber.parser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.ytcuber.model.Lesson;
import org.ytcuber.repository.LessonRepository;

import java.util.ArrayList;
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
        return "welcumPage"; // Assuming this is your start page
    }

    @PostMapping("/start")
    public String startPageP(@RequestParam("subgroup") int subgroup, Model model) {
        List<Lesson> lessonsForSelectedSubgroup = lessonRepository.findBySubgroup(subgroup);
        List<Lesson> lessonsForSubgroup0 = lessonRepository.findBySubgroup(0);

        List<Lesson> combinedLessons = new ArrayList<>();
        combinedLessons.addAll(lessonsForSelectedSubgroup);
        combinedLessons.addAll(lessonsForSubgroup0);

        model.addAttribute("lessons", combinedLessons);
        return "lessons";
    }


    @GetMapping("/index")
    public String hello() {
        return "index";
    }

    @GetMapping("/test")
    public String test(Model model) {
        Iterable<Lesson> lessons = lessonRepository.findAll();
//        model.addAttribute("lessons", lessons);
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

//    @GetMapping("/lessons")
//    public String getLessons(Model model) {
//        List<Lesson> lessons = lessonRepository.findAll();
//        model.addAttribute("lessons",lessons);
//
//        return "lessons";
//    }
//
//    @PostMapping("/start")
//    public String startPageP(@RequestParam("subgroup") int subgroup, Model model) {
//        List<Lesson> lessons = lessonRepository.findBySubgroup(subgroup);
//        model.addAttribute("lessons", lessons);
//        return "lessons";
//    }
//
//// th:if="${lesson.getSubgroup() == 0 or lesson.getSubgroup() == 1}"
//
//    @GetMapping("/schedule")
//    public String getSchedule(Model model) {
//        List<Lesson> lessons = lessonRepository.findAll();
////        model.addAttribute("lessons", lessons);
//        return "schedule";
//    }
//
//    @GetMapping("/index")
//    public String hello() {
//        return "index";
//    }
//
//    @GetMapping("/test")
//    public String test(Model model) {
//        Iterable<Lesson> lessons = lessonRepository.findAll();
////        model.addAttribute("lessons", lessons);
//        return "test";
//    }
//
//    @GetMapping("/start")
//    public String startPage() {
//        return "welcumPage";
//    }
//
//    @PostMapping ("/start")
//    public String startPageP(Model model) {
//        List<Lesson> lessons = lessonRepository.findAll();
//        model.addAttribute("lessons",lessons);
//        return "lessons";
//    }
//
//    @GetMapping("/nyan")
//    public String nyan() {
//        return "nyan";
//    }
//}
