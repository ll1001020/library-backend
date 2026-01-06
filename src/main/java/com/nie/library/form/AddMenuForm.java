package com.nie.library.form;

import lombok.Data;

@Data
public class AddMenuForm {
    private String title;

    private String icon;

    private String path;

    private Integer parentId;

    private Integer creatorId;

    private Integer status;

}
