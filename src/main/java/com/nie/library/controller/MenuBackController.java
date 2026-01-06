package com.nie.library.controller;


import com.nie.library.VO.ResultVO;
import com.nie.library.entity.MenuBack;
import com.nie.library.form.AddMenuForm;
import com.nie.library.form.EditMenuForm;
import com.nie.library.form.PaginationForm;
import com.nie.library.form.SearchForm;
import com.nie.library.service.IMenuBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 后台菜单表 前端控制器
 * </p>
 *
 * @author nie
 * @since 2025-12-16
 */
@RestController
@RequestMapping("/library/menu-back")
public class MenuBackController {
    @Autowired private IMenuBackService imenuBackService;

    // 获取树形后台菜单
    @GetMapping("/getMenuBackList")
    public ResultVO getMenuBackList(){
        ResultVO resultVO = this.imenuBackService.getMenuBackList();
        return resultVO;
    }

    // 获取后台菜单数据（带分页）
    @PostMapping("/getMenuBack")
    public ResultVO getMenuBack(@RequestBody PaginationForm paginationForm){
        ResultVO resultVO = this.imenuBackService.getMenuBack(paginationForm);
        return resultVO;
    }

    // 搜索后台菜单
    @PostMapping("/searchMenuBack")
    public ResultVO searchMenuBack(@RequestBody SearchForm searchForm,PaginationForm paginationForm){
        ResultVO resultVO = this.imenuBackService.searchMenuBack(searchForm,paginationForm);
        return resultVO;
    }

    // 添加后台菜单
    @PostMapping("/addMenuBack")
    public ResultVO addMenuBack(@RequestBody AddMenuForm addMenuForm){
        ResultVO resultVO = this.imenuBackService.addMenuBack(addMenuForm);
        return resultVO;
    }

    // 删除选中后台菜单
    @GetMapping("/deleteSelectMenuBack")
    public ResultVO deleteSelectMenuBack(Integer id){
        ResultVO resultVO = this.imenuBackService.deleteSelectMenuBack(id);
        return resultVO;
    }

    // 修改选中该的后台菜单
    @PostMapping("/editSelectMenuBack")
    public ResultVO editSelectMenuBack(@RequestBody EditMenuForm editMenuForm){
        ResultVO resultVO = this.imenuBackService.editSelectMenuBack(editMenuForm);
        return resultVO;
    }
}

