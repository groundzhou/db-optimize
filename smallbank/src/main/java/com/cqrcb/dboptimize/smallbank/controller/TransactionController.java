package com.cqrcb.dboptimize.smallbank.controller;

import com.cqrcb.dboptimize.smallbank.model.dto.PageBean;
import com.cqrcb.dboptimize.smallbank.model.dto.Result;
import com.cqrcb.dboptimize.smallbank.model.entity.TransactionRecord;
import com.cqrcb.dboptimize.smallbank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    // TODO 1: 插入测试接口
    @PostMapping("/save")
    public Result insertTransaction(@RequestBody TransactionRecord record) {
        transactionService.insertTransactionRecord(record);
        return Result.success();
    }

    @PostMapping("/saveList")
    public Result insertTransactionList(@RequestBody List<TransactionRecord> records) {
        transactionService.insertTransactionRecords(records);
        return Result.success();
    }

    // TODO 2: 查找接口
    @GetMapping("/{id}")
    public Result getTransaction(@PathVariable Integer id) {
        TransactionRecord record = transactionService.getTransactionRecordById(id);

        return Result.success(record);
    }

    // TODO 3: 更新接口
    // TODO 4: 删除接口

    // TODO 5: 分页接口
    @GetMapping
    public Result listTransactions(@RequestParam(defaultValue = "1") Integer pageNum,
                                  @RequestParam(defaultValue = "10") Integer pageSize) {
        PageBean pageBean = null;
//        for (int i = 0; i < 1000; i++) {
//            pageBean = transactionService.page(10, i % 10 + 1, null, null, null);
//        }
        pageBean = transactionService.page(pageNum, pageSize, null, null, null);

        return Result.success(pageBean.getRows());
    }

    // TODO 6: 复杂查询接口
}
