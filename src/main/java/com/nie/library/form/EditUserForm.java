package com.nie.library.form;

import lombok.Data;

@Data
public class EditUserForm {
    private int userId;

    private String username;

    private String password;

    private String realName;

    private String studentId;

    private String phone;

    private String status;
}
