package com.npu.aoxiangbackend.controller;

import cn.dev33.satoken.util.SaResult;
import com.npu.aoxiangbackend.exception.business.UserServiceException;
import com.npu.aoxiangbackend.exception.internal.DatabaseAccessException;
import com.npu.aoxiangbackend.exception.internal.InternalException;
import com.npu.aoxiangbackend.model.User;
import com.npu.aoxiangbackend.protocol.LoginRequest;
import com.npu.aoxiangbackend.protocol.RegisterRequest;
import com.npu.aoxiangbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public SaResult login(@RequestParam String username, @RequestParam String password) {
        try {
            String token = userService.loginUser(username, password);
            return SaResult.ok("登录成功！").setData(token);
        } catch (UserServiceException | InternalException e) {
            return SaResult.error(e.getMessage());
        }
    }

    @PostMapping("/login")
    public SaResult login(@RequestBody LoginRequest request) {
        return login(request.getUsername(), request.getPassword());
    }

    @PostMapping("/register")
    public SaResult register(@RequestBody RegisterRequest request) {
        try {
            userService.registerUser(request);
        } catch (UserServiceException | DatabaseAccessException e) {
            return SaResult.error(e.getMessage());
        }
        return SaResult.ok("注册成功！");
    }

    @GetMapping(value = "/profile")
    @PostMapping(value = "/profile")
    public SaResult getProfile(@RequestParam(required = true) String token) {
        User user = null;
        try {
            user = userService.getUser(token);
            user.setPassword("***");
        } catch (UserServiceException | DatabaseAccessException e) {
            return SaResult.error(e.getMessage());
        }
        return SaResult.ok().setData(user);
    }

    @GetMapping("/logout")
    public SaResult logout(@RequestParam(required = true) String token) {
        try {
            userService.logoutUser(token);
        } catch (UserServiceException | InternalException e) {
            return SaResult.error(e.getMessage());
        }
        return SaResult.ok();
    }

}
