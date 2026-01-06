package com.nie.library.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nie.library.entity.BookCopies;
import com.nie.library.entity.BorrowRecordsRequest;
import com.nie.library.mapper.BookCopiesMapper;
import com.nie.library.mapper.BorrowRecordsRequestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class RequestCheckTask {

    @Autowired
    private BorrowRecordsRequestMapper borrowRecordsRequestMapper;
    @Autowired
    private BookCopiesMapper bookCopiesMapper;

    // 检查申请是否过期
    @Scheduled(cron = "0 0 */1 * * ?") // 每一个小时执行一次
    public void CheckRequest() {
        LambdaQueryWrapper<BorrowRecordsRequest> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BorrowRecordsRequest::getBorrowrequestStatus,"申请中");
        List<BorrowRecordsRequest> borrowRecordsRequestList = borrowRecordsRequestMapper.selectList(queryWrapper);
        int row = 0;
        for(BorrowRecordsRequest borrowRecordsRequest : borrowRecordsRequestList){
            LocalDateTime requestTime = borrowRecordsRequest.getBorrowrequestDate();
            LocalDateTime now = LocalDateTime.now();
            if(requestTime.isBefore(now.minusDays(3))){ // 如果申请超过三天
                // 记录设置逾期
                borrowRecordsRequest.setBorrowrequestStatus("已逾期");
                borrowRecordsRequest.setBorrowpermitDate(now);
                borrowRecordsRequestMapper.updateById(borrowRecordsRequest);
                // 书籍状态修改
                LambdaQueryWrapper<BookCopies> copyQueryWrapper = new LambdaQueryWrapper<>();
                copyQueryWrapper.eq(BookCopies::getCopyId,borrowRecordsRequest.getCopyId());
                BookCopies copy = bookCopiesMapper.selectOne(copyQueryWrapper);
                copy.setStatus("在架");
                row++;
            }
        }
        System.out.println("成功处理"+row+"条逾期申请");
    }
}
