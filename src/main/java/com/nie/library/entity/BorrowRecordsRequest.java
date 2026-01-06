package com.nie.library.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 借阅记录表
 * </p>
 *
 * @author nie
 * @since 2025-12-26
 */
@Getter
@Setter
  @Accessors(chain = true)
  @TableName("borrow_records_request")
public class BorrowRecordsRequest implements Serializable {

    private static final long serialVersionUID = 1L;

      /**
     * 记录ID
     */
        @TableId(value = "record_id", type = IdType.AUTO)
      private Integer recordId;

      /**
     * 用户ID，用来记录这个借书记录是哪位用户所借
     */
      @TableField("user_id")
    private Integer userId;

      @TableField("username")
    private String username;

      /**
     * 副本ID，用来指示具体借的是哪一本书
     */
      @TableField("copy_id")
    private Integer copyId;

    @TableField("title")
    private String title;

      /**
     * 申请借阅时间
     */
      @TableField("borrowRequest_date")
    private LocalDateTime borrowrequestDate;

      /**
     * 同意借阅日期
     */
      @TableField("borrowPermit_date")
    private LocalDateTime borrowpermitDate;

      /**
     * 借阅状态
     */
      @TableField("borrowRequest_status")
    private String borrowrequestStatus;

      /**
     * 操作者ID
     */
      @TableField("creator_id")
    private Integer creatorId;
}
