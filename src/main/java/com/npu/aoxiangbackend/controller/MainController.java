package com.npu.aoxiangbackend.controller;

import com.npu.aoxiangbackend.model.User;
import com.npu.aoxiangbackend.service.UserService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;

@RestController
@RequestMapping("/")
public class MainController {

    private final UserService userService;

    @Autowired
    public MainController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    @ResponseBody
    public User login() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("admin");
        user.setDisplayName("Hell");
        return user;
    }
}
