package com.nie.library.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 图书副本表
 * </p>
 *
 * @author nie
 * @since 2025-12-11
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    public class BookCopies implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId(value = "copy_id", type = IdType.AUTO)
      private Integer copyId;

    private Integer bookId;

    private String barcode;

    private String location;

    private String status;

    private LocalDate purchaseDate;

    private String notes;


}
