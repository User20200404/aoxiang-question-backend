package com.npu.aoxiangbackend.service;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.npu.aoxiangbackend.dao.IUserDao;
import com.npu.aoxiangbackend.model.Option;
import com.npu.aoxiangbackend.model.User;
import com.npu.aoxiangbackend.protocol.RegisterRequest;
import com.npu.aoxiangbackend.util.ColoredPrintStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Optional;

@Service
public class UserService {
    private final IUserDao userDao;
    private final ColoredPrintStream printer;

    @Autowired

    public UserService(IUserDao userDao, ColoredPrintStream coloredPrintStream) {
        this.userDao = userDao;
        this.printer = coloredPrintStream;
    }

    public SaResult registerUser(RegisterRequest req) {
        var username = req.getUsername();
        var password = req.getPassword();
        if (username.length() < 4 || username.length() > 40) {
            return SaResult.error("用户名的长度必须在4和40之间。");
        }

        if (password.length() < 6 || password.length() > 64) {
            return SaResult.error("用户名的长度必须在6和64之间。");
        }

        if (userDao.findUserByUsername(username).isPresent()) {
            return SaResult.error(String.format("用户名为\"%s\"的账户已经存在。", req.getUsername()));
        }

        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(req.getPassword());
        user.setEmail(req.getEmail());
        user.setPhone(req.getPhone());
        user.setCreatedAt(ZonedDateTime.now());
        user.setUpdatedAt(ZonedDateTime.now());
        user.setDisplayName(req.getDisplayName());

        try {
            userDao.addUser(user);
        } catch (Exception e) {
            printer.longPrintException(e);
            return SaResult.error("内部错误。");
        }
        return SaResult.ok("用户注册成功！");
    }

    public SaResult loginUser(String userName, String password) {
        var userOptional = userDao.findUserByUsername(userName);
        if (userOptional.isEmpty()) {
            return SaResult.error("用户不存在。");
        }
        var user = userOptional.get();
        if (!user.getPassword().equals(password)) {
            return SaResult.error("用户名或密码错误。");
        }
        StpUtil.createLoginSession(user.getId());
        return SaResult.ok("登录成功。").setData(StpUtil.getTokenValueByLoginId(user.getId()));
    }

    public SaResult logoutUser(String token) {
        try {
            StpUtil.logoutByTokenValue(token);
            return SaResult.ok("您已成功注销。");
        } catch (
                Exception e) {
            printer.shortPrintException(e);
            return SaResult.error("未知错误。");
        }
    }

    public Optional<User> getUser(String token) {
        try {
            var userId = Long.parseLong((String) StpUtil.getLoginIdByToken(token));
            return userDao.findUserById(userId);
        } catch (Exception e) {
            printer.shortPrintException(e);
            return Optional.empty();
        }
    }
}
