package com.nie.library.controller;

import com.nie.library.VO.ResultVO;
import com.nie.library.form.AddMenuCopyForm;
import com.nie.library.form.EditMenuCopyForm;
import com.nie.library.form.PaginationForm;
import com.nie.library.form.SearchForm;
import com.nie.library.service.MenuCopyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author nie
 * @since 2025-12-23
 */
@RestController
@RequestMapping("/library/menu-copy")
public class MenuCopyController {

    @Autowired
    private MenuCopyService imenuCopyService;

    @GetMapping("/getMenuCopyList")
    public ResultVO getMenuCopyList() {
        ResultVO resultVO = this.imenuCopyService.getMenuCopyList();
        return resultVO;
    }

    // 获取所有带分页的前台菜单数据副本
    @GetMapping("/getMenuCopy")
    public ResultVO getMenuCopy(PaginationForm paginationForm){
        ResultVO resultVO = this.imenuCopyService.getMenuCopy(paginationForm);
        return resultVO;
    }

    // 搜索前台菜单副本
    @PostMapping("/searchMenuCopy")
    public ResultVO searchMenuCopy(@RequestBody SearchForm searchForm, PaginationForm paginationForm){
        ResultVO resultVO = this.imenuCopyService.searchMenuCopy(searchForm,paginationForm);
        return resultVO;
    }

    // 为前台菜单副本表新增菜单
    @PostMapping("/addMenuCopy")
    public ResultVO addMenuCopy(@RequestBody AddMenuCopyForm addMenuCopyForm){
        ResultVO resultVO = this.imenuCopyService.addMenuCopy(addMenuCopyForm);
        return resultVO;
    }

    // 修改选中的前台菜单副本
    @PostMapping("/editSelectMenuCopy")
    public ResultVO editSelectMenuCopy(@RequestBody EditMenuCopyForm editMenuCopyForm){
        ResultVO resultVO = this.imenuCopyService.editSelectMenuCopy(editMenuCopyForm);
        return resultVO;
    }

    // 删除当前选中的前台菜单副本
    @GetMapping("/deleteSelectMenuCopy")
    public ResultVO deleteSelectMenuCopy(Integer id){
        ResultVO resultVO = this.imenuCopyService.deleteSelectMenuCopy(id);
        return resultVO;
    }

}
