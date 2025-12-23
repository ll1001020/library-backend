package com.nie.library.service;

import com.nie.library.VO.ResultVO;
import com.nie.library.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nie.library.form.PaginationForm;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author nie
 * @since 2025-12-11
 */
public interface IMenuService extends IService<Menu> {
    public ResultVO getMenuList();


}
