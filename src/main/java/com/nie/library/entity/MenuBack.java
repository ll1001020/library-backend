package com.nie.library.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 后台菜单表
 * </p>
 *
 * @author nie
 * @since 2025-12-16
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    public class MenuBack implements Serializable {

    private static final long serialVersionUID=1L;

      /**
     * 菜单id
     */
        @TableId(value = "menu_back_id", type = IdType.AUTO)
      private Integer menuBackId;

      /**
     * 标题
     */
      private String title;


      /**
      * 图标
      */
      private String icon;

      /**
      * 图标
      */
      private String path;

      /**
     * 父菜单id，默认0为最高级
     */
      private Integer parentId;

      /**
     * 创建用户id
     */
      private Integer creatorId;

      /**
     * 创建时间
     */
      private LocalDateTime createTime;

      /**
     * 更新时间
     */
      private LocalDateTime updateTime;

      /**
     * 启用状态
     */
      private Integer status;

      @TableField(exist = false) // 表示该字段不是数据库字段
      private List<MenuBack> children;

}
