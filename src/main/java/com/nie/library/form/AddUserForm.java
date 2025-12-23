package com.nie.library.form;

import lombok.Data;

@Data
public class AddUserForm {
    private String username;
    private String password;
    private String realName;
    private String studentId;
    private Integer userTypeId;
    private String phone;
    private String status;
    private String notes;
}
