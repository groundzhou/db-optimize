package com.cqrcb.dboptimize.smallbank.service;


import com.cqrcb.dboptimize.smallbank.model.dto.PageBean;
import com.cqrcb.dboptimize.smallbank.model.entity.Account;
import com.cqrcb.dboptimize.smallbank.model.entity.TransactionRecord;
import com.cqrcb.dboptimize.smallbank.model.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {

    /**
     * 创建交易记录
     */
    void insertTransactionRecord(TransactionRecord record);

    /**
     * 批量创建交易记录
     */
    void insertTransactionRecords(List<TransactionRecord> records);

    /**
     * 根据id获取交易内容
     *
     * @param recordId 交易id
     * @return 交易信息
     */
    TransactionRecord getTransactionRecordById(Integer recordId);

    /**
     * 获取流水列表
     */
    void listTransactionRecords();

    /**
     * 根据用户获取流水列表
     */
    void listTransactionRecordsByUser(User user);

    /**
     * 根据账户获取流水列表
     */
    void listTransactionRecordsByAccount(Account account);

    void deleteById(Integer recordId);

    /***
     * 分页查询
     * @param pageSize 页面大小
     * @param page 页码
     * @param accountId 账户id
     * @param begin 起始时间
     * @param end 结束时间
     * @return 页面对象
     */
    PageBean page(Integer page, Integer pageSize, Integer accountId, LocalDateTime begin, LocalDateTime end);
}
