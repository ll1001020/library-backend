package com.nie.library.service;

import com.nie.library.VO.ResultVO;
import com.nie.library.entity.MenuBack;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nie.library.form.AddMenuForm;
import com.nie.library.form.EditMenuForm;
import com.nie.library.form.PaginationForm;
import com.nie.library.form.SearchForm;

/**
 * <p>
 * 后台菜单表 服务类
 * </p>
 *
 * @author nie
 * @since 2025-12-16
 */
public interface IMenuBackService extends IService<MenuBack> {
    // 获取后台菜单数据
    public ResultVO getMenuBackList();

    // 获取后台菜单数据（带分页）
    public ResultVO getMenuBack(PaginationForm paginationForm);

    // 新增后台菜单
    public ResultVO addMenuBack(AddMenuForm addMenuForm);

    // 删除选中后台菜单
    public ResultVO deleteSelectMenuBack(Integer id);

    // 搜素后台菜单
    public ResultVO searchMenuBack(SearchForm searchForm,PaginationForm paginationForm);

    // 修改选中后台菜单
    public ResultVO editSelectMenuBack(EditMenuForm editMenuForm);
}
