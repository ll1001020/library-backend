package com.nie.library.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author nie
 * @since 2025-12-23
 */
@Getter
@Setter
  @Accessors(chain = true)
  @TableName("menu_copy")
public class MenuCopy implements Serializable {

    private static final long serialVersionUID = 1L;

      /**
     * 菜单id
     */
        @TableId(value = "menu_id", type = IdType.AUTO)
      private Integer menuId;

      /**
     * 标题
     */
      @TableField("title")
    private String title;

      /**
     * 父菜单id，默认0为最高级
     */
      @TableField("parent_id")
    private Integer parentId;

      /**
     * 创建用户id
     */
      @TableField("creator_id")
    private Integer creatorId;

      /**
     * 创建时间
     */
      @TableField("create_time")
    private LocalDateTime createTime;

      /**
     * 更新时间
     */
      @TableField("update_time")
    private LocalDateTime updateTime;

      /**
     * 启用状态
     */
      @TableField("status")
    private Integer status;

    /**
     * 子菜单,用来搭建树形结构
     */
    @TableField(exist = false) // 表示该字段不是数据库字段
    private List<MenuCopy> children;
}
