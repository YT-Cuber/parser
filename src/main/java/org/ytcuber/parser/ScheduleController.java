package org.ytcuber.parser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.ytcuber.model.Lesson;
import org.ytcuber.repository.LessonRepository;

import java.util.Arrays;
import java.util.List;

@Controller
public class ScheduleController {

    @Autowired
    private LessonRepository lessonRepository;

   /* @GetMapping("/lessons")
    public String getLessons() {
        List<Lesson> lessonsForSubgroup0 = lessonRepository.findBySubgroup(0); // Fetch lessons for subgroup 0
        List<Lesson> lessonsForSubgroup1 = lessonRepository.findBySubgroup(1); // Fetch lessons for subgroup 1

        StringBuilder html = new StringBuilder(); // Build HTML code

        // Start the table
        html.append("<table>");

        // Iterate over lessons for subgroup 0
        for (Lesson lesson : lessonsForSubgroup0) {
            html.append("<tr>");

            // Create table cells for each lesson field
            html.append("<td>").append(lesson.getOrdinal()).append(" пара </td>");
            html.append("<td>").append(lesson.getSubject()).append("</td>");
            html.append("<td>").append(lesson.getTeacher()).append("</td>");
            html.append("<td>").append(lesson.getLocation()).append("</td>");
            // html.append("<td>").append(lesson.getTime()).append("</td>");

            html.append("</tr>");
        }

        // Iterate over lessons for subgroup 1
        for (Lesson lesson : lessonsForSubgroup1) {
            html.append("<tr>");

            // Create table cells for each lesson field
            html.append("<td>").append(lesson.getOrdinal()).append("пара </td>");
            html.append("<td>").append(lesson.getSubject()).append("</td>");
            html.append("<td>").append(lesson.getTeacher()).append("</td>");
            html.append("<td>").append(lesson.getLocation()).append("</td>");
            // html.append("<td>").append(lesson.getTime()).append("</td>");

            html.append("</tr>");
        }

        // Close the table
        html.append("</table>");

        return html.toString();
    }*/

    @GetMapping("/lessons")
    public String getLessons(Model model) {
        List<Lesson> lessons = lessonRepository.findAll();
        model.addAttribute("lessons",lessons);

        return "lessons";
    }




    public ScheduleController(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    @GetMapping("/schedule")
    public String getSchedule(Model model) {
        List<Lesson> lessons = lessonRepository.findAll();
//        model.addAttribute("lessons", lessons);
        return "schedule";
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

    @GetMapping("/start")
    public String startPage() {
        return "welcumPage";
    }

    @PostMapping ("/start")
    public String startPageP(Model model) {
        List<Lesson> lessons = lessonRepository.findAll();
        model.addAttribute("lessons",lessons);
        return "lessons";
    }

    @GetMapping("/nyan")
    public String nyan() {
        return "nyan";
    }
}
