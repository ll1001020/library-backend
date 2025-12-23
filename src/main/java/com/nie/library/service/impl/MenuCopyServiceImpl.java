package com.nie.library.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nie.library.VO.PaginationVO;
import com.nie.library.VO.ResultVO;
import com.nie.library.entity.BookCopies;
import com.nie.library.entity.Menu;
import com.nie.library.entity.MenuCopy;
import com.nie.library.form.AddMenuCopyForm;
import com.nie.library.form.EditMenuCopyForm;
import com.nie.library.form.PaginationForm;
import com.nie.library.form.SearchForm;
import com.nie.library.mapper.MenuCopyMapper;
import com.nie.library.service.MenuCopyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author nie
 * @since 2025-12-23
 */
@Service
public class MenuCopyServiceImpl extends ServiceImpl<MenuCopyMapper, MenuCopy> implements MenuCopyService {

    @Autowired
    private MenuCopyMapper menuCopyMapper;

    @Override
    public ResultVO getMenuCopyList() {
        // 1.获取所有菜单
        LambdaQueryWrapper<MenuCopy> queryWrapper = new LambdaQueryWrapper<>();
        ResultVO resultVO = new ResultVO();
        queryWrapper.eq(MenuCopy::getStatus, "1");
        queryWrapper.orderByAsc(MenuCopy::getMenuId);
        List<MenuCopy> allMenuList = menuCopyMapper.selectList(queryWrapper); // 成功获取到所有菜单数据储存到allMenuList中
        Map<Integer,MenuCopy> menuMap = new HashMap<>();  // 创建一个字典menuMap，用于存储菜单数据，键为id，值为Menu对象
        Map<Integer,List<MenuCopy>> recordMap = new HashMap<>();  // 创建一个字典recordMap，用于存储菜单数据，键为父id，值为储存Menu的List<Menu>对象
        // 2.分别储存菜单数据和父子关系到menuMap和recordMap中
        for(MenuCopy menu: allMenuList){  // 循环，每一次循环都从所有菜单数据中取出一个Menu对象
            menuMap.put(menu.getMenuId(), menu); // 将Menu对象存储到menuMap中，键为menuId，值为Menu对象
//            System.out.println(menuMap.get(menu.getMenuId()));
            Integer parentId = menu.getParentId();  // 获取当前Menu对象的父id到parentId中
            if (!recordMap.containsKey(parentId)){  // 如果当前recordMap中不存在当前Menu对象的父id，则创建一个空的List<Menu>对象同这个父id储存起来
                recordMap.put(parentId, new ArrayList<>());
            }
            recordMap.get(parentId).add(menu); // 将当前Menu对象添加到recordMap中，键为当前对象的父id
        }
        // 3.构建根菜单rootMenuList并调用buildChildren方法构建菜单树结构
        List<MenuCopy> rootMenuList = recordMap.get(0);  // 从recordMap中取出父id为0的菜单数据
        if (rootMenuList == null){
            resultVO.setCode(-1);  // 这里设置-1同前端统一处理，表示没有菜单数据
            resultVO.setData(null);
            resultVO.setMsg("没有菜单数据");
            return resultVO;
        }
        rootMenuList.sort(Comparator.comparing(MenuCopy::getMenuId));  // 对取出的一级菜单进行排序根据menuId
        for(MenuCopy menu1: rootMenuList){
            buildChildren(menu1, recordMap);
        }
        resultVO.setCode(0);  // 这里设置0同前端统一处理，表示成功获取菜单数据
        resultVO.setData(rootMenuList);
        resultVO.setMsg("成功获取菜单数据");
        return resultVO;
    }


    @Override
    public ResultVO getMenuCopy(PaginationForm paginationForm) {  // 获取带分页的所有前台菜单数据
        LambdaQueryWrapper<MenuCopy> queryWrapper = new LambdaQueryWrapper<>();
        ResultVO resultVO = new ResultVO();
        queryWrapper.eq(MenuCopy::getStatus, "1");
        queryWrapper.orderByAsc(MenuCopy::getMenuId);
        List<MenuCopy> allMenuList = menuCopyMapper.selectList(queryWrapper); // 成功获取到所有菜单数据储存到allMenuList中
        if (allMenuList == null){
            resultVO.setCode(-1);  // 这里设置-1同前端统一处理，表示没有菜单数据
            resultVO.setData(null);
            resultVO.setMsg("没有菜单数据");
            return resultVO;
        }
        int startIndex = (paginationForm.getCurrentPage()-1) * paginationForm.getPageSize();
        int endIndex = Math.min(startIndex + paginationForm.getPageSize(), allMenuList.size());
        List<MenuCopy> subMenuList = allMenuList.subList(startIndex, endIndex);
        Page<MenuCopy> menuPage = new Page<>(paginationForm.getCurrentPage(), paginationForm.getPageSize());
        PaginationVO paginationVO = new PaginationVO();
        paginationVO.setCurrentPage(menuPage.getCurrent());
        paginationVO.setPageSize(menuPage.getSize());
        paginationVO.setTotal((long)allMenuList.size());
        resultVO.setCode(0);  // 这里设置0同前端统一处理，表示成功获取菜单数据
        resultVO.setData(subMenuList);
        resultVO.setPaginationVO(paginationVO);
        resultVO.setMsg("成功获取菜单数据");
        return resultVO;
    }

    @Override
    public ResultVO searchMenuCopy(SearchForm searchForm, PaginationForm paginationForm) {
        LambdaQueryWrapper<MenuCopy> queryWrapper = new LambdaQueryWrapper<>();
        ResultVO resultVO = new ResultVO();
        queryWrapper.eq(MenuCopy::getStatus, "1");
        queryWrapper.orderByAsc(MenuCopy::getMenuId);
        if(searchForm.getSearchType() != null){
            if(searchForm.getSearchPrecise() == 0){  // 为0表示精确查找
                if(searchForm.getSearchType().equals("菜单名称")){
                    queryWrapper.eq(MenuCopy::getTitle, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("菜单ID")){
                    queryWrapper.eq(MenuCopy::getMenuId, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("父菜单ID")){
                    queryWrapper.eq(MenuCopy::getParentId, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("菜单状态")){
                    if(searchForm.getSearchContent().equals("启用")){
                        queryWrapper.eq(MenuCopy::getStatus, 1);
                    }
                    if(searchForm.getSearchContent().equals("禁用")){
                        queryWrapper.eq(MenuCopy::getStatus, 0);
                    }
                }
            }else if(searchForm.getSearchPrecise() == 1){  // 为1表示模糊查找
                if(searchForm.getSearchType().equals("菜单名称")){
                    queryWrapper.like(MenuCopy::getTitle, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("菜单ID")){
                    queryWrapper.like(MenuCopy::getMenuId, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("父菜单ID")){
                    queryWrapper.like(MenuCopy::getParentId, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("菜单状态")){
                    if(searchForm.getSearchContent().equals("启用")){
                        queryWrapper.eq(MenuCopy::getStatus, 1);
                    }
                    if(searchForm.getSearchContent().equals("禁用")){
                        queryWrapper.eq(MenuCopy::getStatus, 0);
                    }
                }
            }
        }else{
            resultVO.setCode(-2);
            resultVO.setData(null);
            resultVO.setMsg("查询失败，缺少查询类型");
            return resultVO;
        }
        List<MenuCopy> allMenuList = menuCopyMapper.selectList(queryWrapper); // 成功获取到所有菜单数据储存到allMenuList中
        if (allMenuList == null){
            resultVO.setCode(-1);  // 这里设置-1同前端统一处理，表示没有菜单数据
            resultVO.setData(null);
            resultVO.setMsg("没有菜单数据");
            return resultVO;
        }
        int startIndex = (paginationForm.getCurrentPage()-1) * paginationForm.getPageSize();
        int endIndex = Math.min(startIndex + paginationForm.getPageSize(), allMenuList.size());
        List<MenuCopy> subMenuList = allMenuList.subList(startIndex, endIndex);
        Page<MenuCopy> menuPage = new Page<>(paginationForm.getCurrentPage(), paginationForm.getPageSize());
        PaginationVO paginationVO = new PaginationVO();
        paginationVO.setCurrentPage(menuPage.getCurrent());
        paginationVO.setPageSize(menuPage.getSize());
        paginationVO.setTotal((long)allMenuList.size());
        resultVO.setCode(0);  // 这里设置0同前端统一处理，表示成功获取菜单数据
        resultVO.setData(subMenuList);
        resultVO.setPaginationVO(paginationVO);
        resultVO.setMsg("成功获取菜单数据");
        return resultVO;
    }

    @Override
    public ResultVO addMenuCopy(AddMenuCopyForm addMenuCopyForm) {
        LambdaQueryWrapper<MenuCopy> queryWrapper = new LambdaQueryWrapper<>();
        ResultVO resultVO = new ResultVO();
        if(addMenuCopyForm.getTitle()==null || addMenuCopyForm.getTitle().trim().isEmpty()
            || addMenuCopyForm.getCreatorId()==null
        ){
            resultVO.setCode(-1);
            resultVO.setMsg("菜单名称、创建用户ID不可为空！！");
            return resultVO;
        }
        int success = 0;
        int rows = 0;
        List<MenuCopy> menuCopyList = menuCopyMapper.selectList(queryWrapper);
        for(MenuCopy menu: menuCopyList){
            if(menu.getMenuId() == addMenuCopyForm.getParentId()){
                success++;
            }
        }
        if(addMenuCopyForm.getParentId() == 0){
            success++;
        }
        if(success > 0){
            MenuCopy menu = new MenuCopy();
            menu.setTitle(addMenuCopyForm.getTitle());
            menu.setParentId(addMenuCopyForm.getParentId());
            menu.setCreatorId(addMenuCopyForm.getCreatorId());
            menu.setStatus(addMenuCopyForm.getStatus());
            rows = menuCopyMapper.insert(menu);
        }else{
            resultVO.setCode(-2);
            resultVO.setMsg("父菜单不存在！！");
        }
        if(rows > 0){
            resultVO.setCode(0);
            resultVO.setMsg("成功新增"+rows+"条菜单记录，请前往预览确认是否发送前台!");
            resultVO.setData(rows);
        }
        return resultVO;
    }

    @Override
    public ResultVO editSelectMenuCopy(EditMenuCopyForm editMenuCopyForm) {
        LambdaQueryWrapper<MenuCopy> queryWrapper = new LambdaQueryWrapper<>();
        ResultVO resultVO = new ResultVO();
        queryWrapper.eq(MenuCopy::getMenuId, editMenuCopyForm.getMenuId());
        MenuCopy menuCopy = menuCopyMapper.selectOne(queryWrapper);
        if(menuCopy == null){
            resultVO.setCode(-1);
            resultVO.setMsg("当前选中菜单信息不存在，请重新选中！！");
            return resultVO;
        }
        if(editMenuCopyForm.getTitle()==null || editMenuCopyForm.getTitle().trim().isEmpty()
            || editMenuCopyForm.getStatus() == null
        ){
            resultVO.setCode(-2);
            resultVO.setMsg("菜单名称、菜单状态不可为空！！");
            return resultVO;
        }
        menuCopy.setTitle(editMenuCopyForm.getTitle());
        menuCopy.setParentId(editMenuCopyForm.getParentId());
        menuCopy.setStatus(editMenuCopyForm.getStatus());
        int rows = menuCopyMapper.updateById(menuCopy);
        if(rows > 0){
            resultVO.setCode(0);
            resultVO.setData(rows);
            resultVO.setMsg("成功修改"+rows+"条记录");
        }
        return resultVO;
    }

    @Override
    public ResultVO deleteSelectMenuCopy(Integer id) {
        ResultVO resultVO = new ResultVO();
        if(id == null){
            resultVO.setCode(-1);
            resultVO.setMsg("选取未选中菜单，请重新选择！！");
            return resultVO;
        }
        // 1.判断当前菜单是否存在
        LambdaQueryWrapper<MenuCopy> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(MenuCopy::getMenuId, id);
        MenuCopy menuCopy1 = menuCopyMapper.selectOne(queryWrapper1);
        if(menuCopy1 == null){
            resultVO.setCode(-2);
            resultVO.setMsg("当前菜单不存在，请刷新页面重新选择！！");
            return resultVO;
        }
        // 2.判断当前菜单是否存在子菜单
        LambdaQueryWrapper<MenuCopy> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(MenuCopy::getParentId, id);
        MenuCopy menuCopy2 = menuCopyMapper.selectOne(queryWrapper2);
        if(menuCopy2 != null){
            resultVO.setCode(-3);
            resultVO.setMsg("请先删除子菜单！！");
            return resultVO;
        }
        // 3.删除当前菜单
        int rows =  menuCopyMapper.delete(queryWrapper1);
        if(rows > 0){
            resultVO.setCode(0);
            resultVO.setMsg("成功删除"+rows+"条记录");
            resultVO.setData(rows);
        }
        return resultVO;
    }


    private void buildChildren(MenuCopy menu, Map<Integer, List<MenuCopy>> recordMap) {
        List<MenuCopy> children = recordMap.get(menu.getMenuId());  // 获取当前菜单的子菜单数据
        if (children != null && !children.isEmpty()) {  // 如果子菜单不为空
            children.sort(Comparator.comparing(MenuCopy::getMenuId));  // 将取出子菜单根据menuId排序
            menu.setChildren(children);  // 将储存子菜单的列表设置为当前菜单对象的children属性
            for (MenuCopy child : children) {  // 递归设置子菜单的子菜单
                buildChildren(child, recordMap);
            }
        }
    }
}
