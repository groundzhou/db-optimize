package com.cqrcb.dboptimize.service.impl;

import com.cqrcb.dboptimize.mapper.TransactionRecordMapper;
import com.cqrcb.dboptimize.model.dto.PageBean;
import com.cqrcb.dboptimize.model.entity.Account;
import com.cqrcb.dboptimize.model.entity.TransactionRecord;
import com.cqrcb.dboptimize.model.entity.User;
import com.cqrcb.dboptimize.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    TransactionRecordMapper transactionRecordMapper;

    @Override
    public void deleteById(Integer recordId) {

    }

    @Override
    public void insertTransactionRecord(Integer userId, String favoriteName) {

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
    public PageBean page(Integer pageSize, Integer page, Integer accountId, LocalDateTime begin, LocalDateTime end) {
        Long count = transactionRecordMapper.count();
        Integer start = (page - 1) * pageSize;
        List<TransactionRecord> records = transactionRecordMapper.page(accountId, start, pageSize);

        return new PageBean(count, records);
    }
}
