package com.npu.aoxiangbackend.protocol;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}