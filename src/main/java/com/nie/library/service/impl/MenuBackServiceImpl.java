package com.nie.library.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nie.library.VO.PaginationVO;
import com.nie.library.VO.ResultVO;
import com.nie.library.entity.Menu;
import com.nie.library.entity.MenuBack;
import com.nie.library.entity.MenuCopy;
import com.nie.library.entity.Users;
import com.nie.library.form.AddMenuForm;
import com.nie.library.form.EditMenuForm;
import com.nie.library.form.PaginationForm;
import com.nie.library.form.SearchForm;
import com.nie.library.mapper.MenuBackMapper;
import com.nie.library.mapper.UsersMapper;
import com.nie.library.service.IMenuBackService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 后台菜单表 服务实现类
 * </p>
 *
 * @author nie
 * @since 2025-12-16
 */
@Service
public class MenuBackServiceImpl extends ServiceImpl<MenuBackMapper, MenuBack> implements IMenuBackService {

    @Autowired
    private MenuBackMapper menuBackMapper;
    @Autowired
    private UsersMapper usersMapper;

    @Override
    public ResultVO getMenuBackList() {
        LambdaQueryWrapper<MenuBack> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MenuBack::getStatus, "1");
        queryWrapper.orderByAsc(MenuBack::getMenuBackId);
        ResultVO resultVO = new ResultVO<>();
        List<MenuBack> allMenuBackList = menuBackMapper.selectList(queryWrapper); // 获取所有菜单数据
        Map<Integer , MenuBack> menuBackMap = new HashMap<>();
        Map<Integer , List<MenuBack>>  recodeMenuBackMap = new HashMap<>();
        for(MenuBack menuBack : allMenuBackList){
            menuBackMap.put(menuBack.getMenuBackId(),menuBack);
            Integer menuBackParentId = menuBack.getParentId();
            if(!recodeMenuBackMap.containsKey( menuBackParentId)){
                recodeMenuBackMap.put(menuBackParentId,new ArrayList<>());
            }
            recodeMenuBackMap.get(menuBackParentId).add(menuBack);
        }
        List<MenuBack> rootMenuBackList = recodeMenuBackMap.get(0);
        if(rootMenuBackList == null){
            resultVO.setCode(-1);
            resultVO.setData(null);
            resultVO.setMsg("没有菜单数据");
        }
        rootMenuBackList.sort(Comparator.comparing(MenuBack::getMenuBackId));
        for(MenuBack menuBack:rootMenuBackList){
            buildChildren(menuBack,recodeMenuBackMap);
        }
        resultVO.setCode(0);
        resultVO.setData(rootMenuBackList);
        resultVO.setMsg("成功获取菜单数据");
        return resultVO;
    }

    @Override
    public ResultVO getMenuBack(PaginationForm paginationForm) {  // 获取带分页的后台菜单数据
        LambdaQueryWrapper<MenuBack> queryWrapper = new LambdaQueryWrapper<>();
        ResultVO resultVO = new ResultVO();
        Page<MenuBack> page = new Page<>(paginationForm.getCurrentPage(),paginationForm.getPageSize());
        Page<MenuBack> menuBackPage = menuBackMapper.selectPage(page, queryWrapper);
        List<MenuBack> allMenuBackList = menuBackPage.getRecords();
        PaginationVO paginationVO = new PaginationVO();
        paginationVO.setCurrentPage(menuBackPage.getCurrent());
        paginationVO.setPageSize(menuBackPage.getSize());
        paginationVO.setTotal(menuBackPage.getTotal());
        if(allMenuBackList != null && allMenuBackList.size() > 0){
            resultVO.setCode(0);
            resultVO.setData(allMenuBackList);
            resultVO.setPaginationVO(paginationVO);
            resultVO.setMsg("成功获取后台菜单数据");
        }else{
            resultVO.setCode(-1);
            resultVO.setData(null);
            resultVO.setMsg("获取后台菜单数据失败！");
        }
        return resultVO;
    }

    @Override
    public ResultVO searchMenuBack(SearchForm searchForm,PaginationForm paginationForm) {  // 搜素后台菜单
        LambdaQueryWrapper<MenuBack> queryWrapper = new LambdaQueryWrapper<>();
        ResultVO resultVO = new ResultVO();
        queryWrapper.orderByAsc(MenuBack::getMenuBackId);
        if(searchForm.getSearchType() != null){
            if(searchForm.getSearchPrecise() == 0){  // 为0表示精确查找
                if(searchForm.getSearchType().equals("菜单名称")){
                    queryWrapper.eq(MenuBack::getTitle, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("菜单ID")){
                    queryWrapper.eq(MenuBack::getMenuBackId, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("父菜单ID")){
                    queryWrapper.eq(MenuBack::getParentId, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("菜单状态")){
                    if(searchForm.getSearchContent().equals("启用")){
                        queryWrapper.eq(MenuBack::getStatus, 1);
                    }
                    if(searchForm.getSearchContent().equals("禁用")){
                        queryWrapper.eq(MenuBack::getStatus, 0);
                    }
                }
            }else if(searchForm.getSearchPrecise() == 1){  // 为1表示模糊查找
                if(searchForm.getSearchType().equals("菜单名称")){
                    queryWrapper.like(MenuBack::getTitle, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("菜单ID")){
                    queryWrapper.like(MenuBack::getMenuBackId, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("父菜单ID")){
                    queryWrapper.like(MenuBack::getParentId, searchForm.getSearchContent());
                }else if(searchForm.getSearchType().equals("菜单状态")){
                    if(searchForm.getSearchContent().equals("启用")){
                        queryWrapper.eq(MenuBack::getStatus, 1);
                    }
                    if(searchForm.getSearchContent().equals("禁用")){
                        queryWrapper.eq(MenuBack::getStatus, 0);
                    }
                }
            }
        }else{
            resultVO.setCode(-2);
            resultVO.setData(null);
            resultVO.setMsg("查询失败，缺少查询类型");
            return resultVO;
        }
        List<MenuBack> allMenuList = menuBackMapper.selectList(queryWrapper); // 成功获取到所有菜单数据储存到allMenuList中
        if (allMenuList == null){
            resultVO.setCode(-1);  // 这里设置-1同前端统一处理，表示没有菜单数据
            resultVO.setData(null);
            resultVO.setMsg("没有菜单数据");
            return resultVO;
        }
        Page<MenuBack> menuPage = new Page<>(paginationForm.getCurrentPage(), paginationForm.getPageSize());
        Page<MenuBack> menuBackPgae = menuBackMapper.selectPage(menuPage,queryWrapper);
        List<MenuBack> subMenuList = menuBackPgae.getRecords();
        PaginationVO paginationVO = new PaginationVO();
        paginationVO.setCurrentPage(menuBackPgae.getCurrent());
        paginationVO.setPageSize(menuBackPgae.getSize());
        paginationVO.setTotal(menuBackPgae.getTotal());
        resultVO.setCode(0);  // 这里设置0同前端统一处理，表示成功获取菜单数据
        resultVO.setData(subMenuList);
        resultVO.setPaginationVO(paginationVO);
        resultVO.setMsg("成功获取菜单数据");
        return resultVO;
    }

    @Override
    public ResultVO editSelectMenuBack(EditMenuForm editMenuForm) { // 修改选中后台菜单
        LambdaQueryWrapper<MenuBack> queryWrapper = new LambdaQueryWrapper<>();
        ResultVO resultVO = new ResultVO();
        queryWrapper.eq(MenuBack::getMenuBackId, editMenuForm.getMenuId());
        MenuBack menuBack = menuBackMapper.selectOne(queryWrapper);
        if(menuBack == null){
            resultVO.setCode(-1);
            resultVO.setMsg("当前选中菜单信息不存在，请重新选中！！");
            return resultVO;
        }
        if(editMenuForm.getTitle()==null || editMenuForm.getTitle().trim().isEmpty()
                || editMenuForm.getStatus() == null
                || editMenuForm.getIcon() == null || editMenuForm.getIcon().trim().isEmpty()
                || editMenuForm.getPath() == null || editMenuForm.getPath().trim().isEmpty()
                || editMenuForm.getCreatorId() == null
        ){
            resultVO.setCode(-2);
            resultVO.setMsg("请仔细检查必填项！！");
            return resultVO;
        }
        LocalDateTime now = LocalDateTime.now();
        menuBack.setTitle(editMenuForm.getTitle());
        menuBack.setIcon(editMenuForm.getIcon());
        menuBack.setPath(editMenuForm.getPath());
        menuBack.setParentId(editMenuForm.getParentId());
        menuBack.setUpdateTime(now);
        menuBack.setStatus(editMenuForm.getStatus());
        int rows = menuBackMapper.updateById(menuBack);
        if(rows > 0){
            resultVO.setCode(0);
            resultVO.setData(rows);
            resultVO.setMsg("成功修改"+rows+"条记录");
        }
        return resultVO;
    }

    @Override
    public ResultVO addMenuBack(AddMenuForm addMenuForm) {  // 新增后台菜单数据
        ResultVO resultVO = new ResultVO();
        LambdaQueryWrapper<MenuBack> queryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Users> usersLambdaQueryWrapper = new LambdaQueryWrapper<>();
        List<Users> users = usersMapper.selectList(usersLambdaQueryWrapper);
        int hasId = 0;
        for(Users user : users){
            if(user.getUserId() != addMenuForm.getCreatorId()){
                continue;
            }
            if(user.getUserTypeId() == 0){
                hasId++;
                break;
            }
        }
        if(hasId == 0){
            resultVO.setCode(-1);
            resultVO.setData(null);
            resultVO.setMsg("当前用户不存在或没有权限！！");
            return resultVO;
        }
        if(addMenuForm.getTitle() == null || addMenuForm.getTitle().equals("")
            || addMenuForm.getIcon() == null || addMenuForm.getIcon().equals("")
                || addMenuForm.getPath() == null || addMenuForm.getPath().equals("")
                    || addMenuForm.getStatus() == null || addMenuForm.getStatus().equals("")
        ){
            resultVO.setCode(-2);
            resultVO.setData(null);
            resultVO.setMsg("请完善必填项！");
            return resultVO;
        }
        if(addMenuForm.getParentId() != 0){
            queryWrapper.eq(MenuBack::getParentId,addMenuForm.getParentId());
            MenuBack menuBack = menuBackMapper.selectOne(queryWrapper);
            if(menuBack == null){
                resultVO.setCode(-3);
                resultVO.setData(null);
                resultVO.setMsg("当前菜单中没有该父菜单！！！");
                return resultVO;
            }
        }else{
            MenuBack newMenuBack = new MenuBack();
            newMenuBack.setTitle(addMenuForm.getTitle());
            newMenuBack.setIcon(addMenuForm.getIcon());
            newMenuBack.setPath(addMenuForm.getPath());
            newMenuBack.setParentId(addMenuForm.getParentId());
            newMenuBack.setCreatorId(addMenuForm.getCreatorId());
            newMenuBack.setStatus(addMenuForm.getStatus());
            int row = menuBackMapper.insert(newMenuBack);
            resultVO.setCode(0);
            resultVO.setData(row);
            resultVO.setMsg("成功新增菜单！");
        }
        return resultVO;
    }

    @Override
    public ResultVO deleteSelectMenuBack(Integer id) {  // 删除选中后台菜单
        LambdaQueryWrapper<MenuBack> queryWrapper = new LambdaQueryWrapper<>();
        ResultVO resultVO = new ResultVO();
        queryWrapper.eq(MenuBack::getMenuBackId,id);
        MenuBack menuBack = menuBackMapper.selectOne(queryWrapper);
        if(menuBack == null){
            resultVO.setCode(-1);
            resultVO.setMsg("当前菜单不存在，请刷新页面重新选择！");
            return resultVO;
        }
        int row = menuBackMapper.delete(queryWrapper);
        if(row != 0){
            resultVO.setCode(0);
            resultVO.setData(row);
            resultVO.setMsg("删除成功！请刷新页面");
        }
        return resultVO;
    }



    private void buildChildren(MenuBack menuBack, Map<Integer,List<MenuBack>> recodeMenuBackMap){
        List<MenuBack> childrenMenuBackList = recodeMenuBackMap.get(menuBack.getMenuBackId());
        if(childrenMenuBackList != null && !childrenMenuBackList.isEmpty()){
            childrenMenuBackList.sort(Comparator.comparing(MenuBack::getMenuBackId));
            menuBack.setChildren(childrenMenuBackList);
            for(MenuBack childMenuBack : childrenMenuBackList){
                buildChildren(childMenuBack,recodeMenuBackMap);
            }
        }

    }
}
