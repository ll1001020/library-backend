package com.nie.library.form;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AddCopyExcelForm {
    @ExcelProperty("书籍ID")
    private Integer bookId;
    @ExcelProperty("馆藏编号")
    private String barcode;
    @ExcelProperty("馆藏位置")
    private String location;
    @ExcelProperty("馆藏状态")
    private String status;
    @ExcelProperty("采购日期")
    private String purchaseDate;
    @ExcelProperty("备注")
    private String notes;
}
