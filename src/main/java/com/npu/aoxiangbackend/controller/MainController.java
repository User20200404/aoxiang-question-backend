package com.npu.aoxiangbackend.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.npu.aoxiangbackend.protocol.LoginRequest;
import com.npu.aoxiangbackend.protocol.RegisterRequest;
import com.npu.aoxiangbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/")
public class MainController {

    private final UserService userService;

    @Autowired
    public MainController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public SaResult login(@RequestParam String username, @RequestParam String password) {
        return userService.loginUser(username, password)
                .setMsg("警告：GET方法仅用于测试目的。");
    }

    @PostMapping("/login")
    public SaResult login(@RequestBody LoginRequest request) {
        return userService.loginUser(request.getUsername(), request.getPassword());
    }

    @PostMapping("/register")
    public SaResult register(@RequestBody RegisterRequest request) {
        return userService.registerUser(request);
    }

    @GetMapping("/profile")
    public SaResult getProfile(@RequestParam(required = true) String token) {
        var userOptional = userService.getUser(token);
        if (userOptional.isEmpty()) {
            return SaResult.error("提供的token无效。");
        }
        return SaResult.ok().setData(userOptional.get());
    }

    @GetMapping("/logout")
    public SaResult logout(@RequestParam(required = true) String token) {
        return userService.logoutUser(token);
    }

}
