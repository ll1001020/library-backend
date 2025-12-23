package com.nie.library.form;

import lombok.Data;

@Data
public class EditMenuCopyForm {
    private Integer menuId;
    private String title;
    private Integer parentId;
    private Integer status;
}
