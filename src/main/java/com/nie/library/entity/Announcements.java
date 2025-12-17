package com.nie.library.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 公告表
 * </p>
 *
 * @author nie
 * @since 2025-12-11
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    public class Announcements implements Serializable {

    private static final long serialVersionUID=1L;

      /**
     * 公告id
     */
        @TableId(value = "announcement_id", type = IdType.AUTO)
      private Integer announcementId;

    private String title;

    private String content;

    private Integer publisherId;

    private LocalDateTime publishTime;

    private LocalDateTime expireTime;

    private String status;

    private Integer viewCount;


}
