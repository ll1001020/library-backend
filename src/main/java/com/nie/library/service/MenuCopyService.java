package com.nie.library.service;

import com.nie.library.VO.ResultVO;
import com.nie.library.entity.MenuCopy;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nie.library.form.AddMenuCopyForm;
import com.nie.library.form.EditMenuCopyForm;
import com.nie.library.form.PaginationForm;
import com.nie.library.form.SearchForm;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author nie
 * @since 2025-12-23
 */
public interface MenuCopyService extends IService<MenuCopy> {
    // 获取所有前台菜单数据
    public ResultVO getMenuCopyList();

    // 获取所有带分页的前台菜单数据副本
    public ResultVO getMenuCopy(PaginationForm paginationForm);

    // 搜索前台菜单副本
    public ResultVO searchMenuCopy(SearchForm searchForm, PaginationForm paginationForm);

    // 为前台菜单副本添加菜单
    public ResultVO addMenuCopy(AddMenuCopyForm addMenuCopyForm);

    // 修改选中的前台菜单副本
    public ResultVO editSelectMenuCopy(EditMenuCopyForm editMenuCopyForm);

    // 删除选中的前台菜单副本
    public ResultVO deleteSelectMenuCopy(Integer id);
}
