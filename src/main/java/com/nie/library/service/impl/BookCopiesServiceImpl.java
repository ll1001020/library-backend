package com.nie.library.service.impl;

import com.nie.library.entity.BookCopies;
import com.nie.library.mapper.BookCopiesMapper;
import com.nie.library.service.IBookCopiesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 图书副本表 服务实现类
 * </p>
 *
 * @author nie
 * @since 2025-12-11
 */
@Service
public class BookCopiesServiceImpl extends ServiceImpl<BookCopiesMapper, BookCopies> implements IBookCopiesService {

}
