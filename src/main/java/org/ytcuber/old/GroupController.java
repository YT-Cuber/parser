//package org.ytcuber.controller;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.ytcuber.database.repository.GroupRepository;
//
//@Controller
//@Transactional
//@RequestMapping("/group")
//public class GroupController {
//    private GroupRepository groupRepository;
//    @GetMapping
//    public String createGroup(@RequestParam String group) {
//        return "index";
//    }
//}