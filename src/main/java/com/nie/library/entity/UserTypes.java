package com.nie.library.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户类型表
 * </p>
 *
 * @author nie
 * @since 2025-12-12
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    public class UserTypes implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId(value = "user_type_id", type = IdType.AUTO)
      private Integer userTypeId;

      /**
     * 类型代码：ADMIN/USER
     */
      private String userTypeCode;

      /**
     * 类型名称
     */
      private String userTypeName;

      /**
     * 借阅上限
     */
      private Integer borrowLimit;

      /**
     * 借阅天数
     */
      private Integer borrowDays;

      /**
     * 是否有管理权限
     */
      private Boolean canManage;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

      /**
     * 描述
     */
      private String notes;


}
