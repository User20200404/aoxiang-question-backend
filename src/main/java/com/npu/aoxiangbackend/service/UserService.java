package com.npu.aoxiangbackend.service;

import cn.dev33.satoken.stp.StpUtil;
import com.npu.aoxiangbackend.dao.IUserDao;
import com.npu.aoxiangbackend.exception.business.UserServiceException;
import com.npu.aoxiangbackend.exception.internal.DatabaseAccessException;
import com.npu.aoxiangbackend.exception.internal.InternalException;
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

    public void throwOnInvalidRegister(String username, String password) throws UserServiceException, DatabaseAccessException {
        if (username.length() < 4 || username.length() > 40) {
            throw new UserServiceException("用户名长度必须在4和40之间。");
        }

        if (password.length() < 6 || password.length() > 64) {
            throw new UserServiceException("密码长度必须在6和64之间。");
        }

        Optional<User> userOptional;

        try {
            userOptional = userDao.findUserByUsername(username);
        } catch (Exception exception) {
            throw new DatabaseAccessException();
        }
        if (userOptional.isPresent()) {
            throw new UserServiceException(String.format("用户名为\"%s\"的用户已经存在。", username));
        }
    }

    public void registerUser(RegisterRequest req) throws UserServiceException, DatabaseAccessException {
        var username = req.getUsername();
        var password = req.getPassword();

        throwOnInvalidRegister(username, password);

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
            printer.shortPrintException(e);
            throw new DatabaseAccessException(e);
        }
    }

    public String loginUser(String userName, String password) throws InternalException, UserServiceException {
        Optional<User> userOptional;
        try {
            userOptional = userDao.findUserByUsername(userName);
        } catch (Exception e) {
            throw new DatabaseAccessException(e);
        }
        if (userOptional.isEmpty()) {
            throw new UserServiceException("用户不存在。");
        }
        var user = userOptional.get();
        if (!user.getPassword().equals(password)) {
            throw new UserServiceException("用户名或密码错误。");
        }
        try {
            StpUtil.createLoginSession(user.getId());
            return StpUtil.getTokenValueByLoginId(user.getId());
        } catch (Exception e) {
            throw new InternalException("未知错误。", e);
        }
    }

    public void logoutUser(String token) throws UserServiceException, InternalException {
        throwOnInvalidToken(token);
        try {
            StpUtil.logoutByTokenValue(token);
        } catch (Exception e) {
            printer.shortPrintException(e);
            throw new InternalException(e);
        }
    }

    public User getUser(String tokenValue) throws UserServiceException, DatabaseAccessException {
        long userId = checkAndGetUserId(tokenValue);

        Optional<User> userOptional;
        try {
            userOptional = userDao.findUserById(userId);
        } catch (Exception e) {
            printer.shortPrintException(e);
            throw new DatabaseAccessException(e);
        }
        if (userOptional.isEmpty())
            throw new UserServiceException("无法找到指定用户。");
        return userOptional.get();
    }

    public boolean isTokenValid(String token) {
        return StpUtil.getLoginIdByToken(token) != null;
    }

    public long checkAndGetUserId(String tokenValue) throws UserServiceException {
        var token = StpUtil.getLoginIdByToken(tokenValue);
        if (token == null)
            throw new UserServiceException("提供的token无效。");
        return Long.parseLong(token.toString());
    }

    public void throwOnInvalidToken(String tokenValue) throws UserServiceException {
        if (!isTokenValid(tokenValue)) {
            throw new UserServiceException("提供的token无效。");
        }
    }
}
