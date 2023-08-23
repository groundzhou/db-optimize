package com.cqrcb.dboptimize.mybank.service.impl;

import com.cqrcb.dboptimize.mybank.mapper.TransactionRecordMapper;
import com.cqrcb.dboptimize.mybank.model.dto.PageBean;
import com.cqrcb.dboptimize.mybank.model.entity.Account;
import com.cqrcb.dboptimize.mybank.model.entity.TransactionRecord;
import com.cqrcb.dboptimize.mybank.model.entity.User;
import com.cqrcb.dboptimize.mybank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    TransactionRecordMapper transactionRecordMapper;

    @Override
    public void insertTransactionRecord(TransactionRecord record) {

    }

    @Override
    public void insertTransactionRecords(List<TransactionRecord> records) {

    }

    @Override
    public TransactionRecord getTransactionRecordById(Integer recordId) {
        return null;
    }

    @Override
    public void listTransactionRecords() {

    }

    @Override
    public void listTransactionRecordsByUser(User user) {

    }

    @Override
    public void listTransactionRecordsByAccount(Account account) {

    }

    @Override
    public PageBean page(Integer page, Integer pageSize, Integer accountId, LocalDateTime begin, LocalDateTime end) {
        Long count = transactionRecordMapper.count();
        Integer start = (page - 1) * pageSize;
        List<TransactionRecord> records = transactionRecordMapper.page(accountId, start, pageSize);

        return new PageBean(count, records);
    }

    @Override
    public void deleteById(Integer recordId) {

    }
}
