package com.nie.library.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author nie
 * @since 2025-12-11
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    public class Users implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId(value = "user_id", type = IdType.AUTO)
      private Integer userId;

    private String username;

    private String password;

    private String realName;

    private String studentId;

    private String phone;

    private Integer userTypeId;

    private Integer borrowLimit;

    private Integer borrowedCount;

    private Integer borrowLimitDay;

    private String status;

    private LocalDateTime lastLoginTime;

    private LocalDateTime registerTime;

    private String notes;


}
