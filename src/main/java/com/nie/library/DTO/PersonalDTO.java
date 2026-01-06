package com.nie.library.DTO;

import com.nie.library.entity.BorrowRecords;
import com.nie.library.entity.BorrowRecordsRequest;
import lombok.Data;

import java.util.List;

@Data
public class PersonalDTO {
    private List<BorrowRecords> currentBorrow;
    private List<BorrowRecords> pastBorrow;
    private List<BorrowRecordsRequest> requestBorrow;
}
