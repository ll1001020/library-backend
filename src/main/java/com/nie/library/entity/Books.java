package com.nie.library.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 图书基本信息表
 * </p>
 *
 * @author nie
 * @since 2025-12-11
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    public class Books implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId(value = "book_id", type = IdType.AUTO)
      private Integer bookId;

    private String isbn;

    private String title;

    private String author;

    private String publisher;

    private LocalDate publishDate;

    private BigDecimal price;

    private String summary;

    private String coverImage;

    private Integer totalCopies;

    private Integer availableCopies;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
