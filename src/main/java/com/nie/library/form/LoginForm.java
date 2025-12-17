package com.nie.library.form;

import lombok.Data;

@Data
public class LoginForm {
    private String username;
    private String password;
    private String loginType;
}
