package com.nie.library.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 系统日志表
 * </p>
 *
 * @author nie
 * @since 2025-12-11
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    public class SystemLogs implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId(value = "log_id", type = IdType.AUTO)
      private Integer logId;

    private Integer userId;

    private String logType;

    private String operation;

    private String ipAddress;

    private LocalDateTime logTime;

    private String details;


}
