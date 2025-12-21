package com.nie.library.form;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class AddBookExcelForm {
    @ExcelProperty("ISBN")
    private String isbn;

    @ExcelProperty("书名")
    private String title;  // 不能为空

    @ExcelProperty("作者")
    private String author;

    @ExcelProperty("出版社")
    private String publisher;

    @ExcelProperty("出版日期")
    private String publishDate;

    @ExcelProperty("单价")
    private Integer price;

    @ExcelProperty("简介")
    private String summary;

    @ExcelProperty("分类名")
    private String categoryName;  // 数据库没有该字段

    @ExcelProperty("分类ID")
    private Integer categoryId;  // 不能为空

    @ExcelProperty("封面文件名")
    private String coverImage;
}
