package com.nie.library.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 借阅记录表
 * </p>
 *
 * @author nie
 * @since 2025-12-11
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    public class BorrowRecords implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId(value = "record_id", type = IdType.AUTO)
      private Integer recordId;

    private Integer userId;

    private String username;

    private Integer copyId;

    private String title;

    private LocalDateTime borrowDate;

    private LocalDateTime dueDate;

    private LocalDateTime actualReturnDate;

    private String borrowStatus;

    private Integer renewCount;

    private Integer creatorId;


}
