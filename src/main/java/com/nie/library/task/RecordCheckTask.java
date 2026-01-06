package com.nie.library.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nie.library.entity.BorrowRecords;
import com.nie.library.entity.Users;
import com.nie.library.mapper.BorrowRecordsMapper;
import com.nie.library.mapper.UsersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class RecordCheckTask {

    @Autowired
    private BorrowRecordsMapper borrowRecordsMapper;
    @Autowired
    private UsersMapper usersMapper;

    // 检查借书是否逾期未还
    @Scheduled(cron = "0 0 */1 * * ?") // 每一个小时执行一次
    public void CheckRecord() {
        LambdaQueryWrapper<BorrowRecords> recordQueryWrapper = new LambdaQueryWrapper<>();
        recordQueryWrapper.eq(BorrowRecords::getBorrowStatus,"借阅中");
        List<BorrowRecords> recordList = borrowRecordsMapper.selectList(recordQueryWrapper);
        int rows = 0;
        for(BorrowRecords borrowRecords : recordList) {
            LocalDateTime dueTime = borrowRecords.getDueDate();
            LocalDateTime now = LocalDateTime.now();
            if(now.isAfter(dueTime) && borrowRecords.getActualReturnDate()==null){
                // 设置书籍逾期
                borrowRecords.setBorrowStatus("逾期");
                borrowRecordsMapper.updateById(borrowRecords);
                // 拉黑借阅人
                LambdaQueryWrapper<Users> userQueryWrapper = new LambdaQueryWrapper<>();
                userQueryWrapper.eq(Users::getUserId,borrowRecords.getUserId());
                Users user = usersMapper.selectOne(userQueryWrapper);
                user.setStatus("禁用");
                usersMapper.updateById(user);
                rows++;
            }
        }
        System.out.println("成功检查"+rows+"条图书借阅记录");
    }
}
