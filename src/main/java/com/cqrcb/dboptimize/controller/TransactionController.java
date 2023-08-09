package com.cqrcb.dboptimize.controller;

import com.cqrcb.dboptimize.model.dto.PageBean;
import com.cqrcb.dboptimize.model.dto.Result;
import com.cqrcb.dboptimize.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @GetMapping("")
    public Result getTransactions() {
        PageBean page = transactionService.page(10, 1, null, null, null);
        return Result.success(page.getRows());
    }

    // TODO 1: 插入测试接口
    // TODO 2: 查找接口
    // TODO 3: 更新接口
    // TODO 4: 删除接口
    // TODO 5: 分页接口
    // TODO 6: 复杂查询接口
}
