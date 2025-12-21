package com.nie.library.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nie.library.VO.ResultVO;
import com.nie.library.entity.BookCategory;
import com.nie.library.mapper.BookCategoryMapper;
import com.nie.library.service.IBookCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author nie
 * @since 2025-12-17
 */
@Service
public class BookCategoryServiceImpl extends ServiceImpl<BookCategoryMapper, BookCategory> implements IBookCategoryService {

    @Autowired
    private BookCategoryMapper bookCategoryMapper;

    @Override
    public ResultVO getBookCategoryTree() {
        LambdaQueryWrapper<BookCategory> wrapper = new LambdaQueryWrapper<>();
        ResultVO resultVO = new ResultVO();
        List<BookCategory> list = bookCategoryMapper.selectList(wrapper);
        Map<Integer,BookCategory> bookCategoryMap = new HashMap<>();
        Map<Integer,List<BookCategory>> recordBookCategoryMap = new HashMap<>();
        for (BookCategory bookCategory : list) {
            bookCategoryMap.put(bookCategory.getCategoryId(), bookCategory);
            Integer parentId = bookCategory.getParentId();
            List<BookCategory> bookCategoryList = recordBookCategoryMap.get(parentId);
            if(bookCategoryList == null){
                recordBookCategoryMap.put(parentId,new ArrayList<>());
            }
            recordBookCategoryMap.get(bookCategory.getParentId()).add(bookCategory);
        }
        List<BookCategory> rootBookCategoryList = recordBookCategoryMap.get(0);
        if(rootBookCategoryList == null){
            resultVO.setCode(-1);
            resultVO.setMsg("获取失败，没有找到书籍分类树信息");
            return resultVO;
        }
        rootBookCategoryList.sort(Comparator.comparing(BookCategory::getCategoryId));
        for (BookCategory bookCategory : rootBookCategoryList) {
            buildChildren(bookCategory,recordBookCategoryMap);
        }
        resultVO.setData(rootBookCategoryList);
        resultVO.setCode(0);
        resultVO.setMsg("成功获取书籍分类树");
        return resultVO;
    }

    private void buildChildren(BookCategory bookCategory, Map<Integer,List<BookCategory>> recordBookCategoryMap) {
        List<BookCategory> bookCategoryList = recordBookCategoryMap.get(bookCategory.getCategoryId());
        if(bookCategoryList != null && !bookCategoryList.isEmpty()){
            bookCategoryList.sort(Comparator.comparing(BookCategory::getCategoryId));
            bookCategory.setChildren(bookCategoryList);
            for(BookCategory childBookCategory:bookCategoryList){
                buildChildren(childBookCategory,recordBookCategoryMap);
            }
        }
    }
}
