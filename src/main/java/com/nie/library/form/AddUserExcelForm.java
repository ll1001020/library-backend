package com.nie.library.form;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class AddUserExcelForm {
    @ExcelProperty("用户名")
    private String username;
    @ExcelProperty("用户密码")
    private String password;
    @ExcelProperty("姓名")
    private String realName;
    @ExcelProperty("学号")
    private String studentId;
    @ExcelProperty("用户类型ID")
    private Integer userTypeId;
    @ExcelProperty("电话号码")
    private String phone;
    @ExcelProperty("用户状态")
    private String status;
    @ExcelProperty("备注")
    private String notes;
}
