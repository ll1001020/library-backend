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
 *
 * </p>
 *
 * @author nie
 * @since 2025-12-17
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    public class BookCategory implements Serializable {

    private static final long serialVersionUID=1L;

      /**
     * 分类id
     */
        @TableId(value = "category_id", type = IdType.AUTO)
      private Integer categoryId;

      /**
     * 中图法分类号（含/-数字）例如A1
     */
      private String clcCode;

      /**
     * 类目中文名
     */
      private String clcName;

      /**
     * 父节点，0=顶级
     */
      private Integer parentId;

      /**
     * 同级排序码
     */
      private Integer sort;

      /**
     * 版次，目前第5版
     */
      private Integer edition;

      /**
     * 创建时间
     */
      private LocalDateTime createTime;

      /**
     * 更新时间
     */
      private LocalDateTime updateTime;

      @TableField(exist = false)
      private List<BookCategory> children;

}
