package com.nie.library.form;

import lombok.Data;

@Data
public class AddMenuCopyForm {
    private String title;

    private Integer parentId;

    private Integer creatorId;

    private Integer status;

}
