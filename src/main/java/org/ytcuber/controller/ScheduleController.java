package org.ytcuber.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.ytcuber.database.model.Lesson;
import org.ytcuber.database.repository.GroupRepository;
import org.ytcuber.database.repository.LessonRepository;

import java.util.List;

@Controller
public class ScheduleController {
    private final LessonRepository lessonRepository;
    private GroupRepository groupRepository;
    public ScheduleController(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }
    @GetMapping("/")
    public String startPage() {
        return "welcumPage";
    }
    @PostMapping("/start")
    public String startPageP(@RequestParam("subgroup") int subgroup, @RequestParam("group_text") String groupText, Model model) {
        System.err.println(groupText.toLowerCase());
//        Long groupId = 1L;
        List<Lesson> lessonsForSelectedSubgroup = lessonRepository.getLessons(subgroup);
        model.addAttribute("lessons", lessonsForSelectedSubgroup);
        return "lessons";
    }
    @GetMapping("/nyan")
    public String nyan() {
        return "nyan";
    }
}