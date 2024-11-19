package com.npu.aoxiangbackend.service;

import com.npu.aoxiangbackend.dao.IUserDao;
import com.npu.aoxiangbackend.dao.UserDao;
import com.npu.aoxiangbackend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final IUserDao userDao;

    @Autowired
    public UserService(IUserDao userDao) {
        this.userDao = userDao;
    }

    public void registerUser(User user) {

    }

    public void loginUser(User user) {

    }
}
