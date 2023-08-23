package com.cqrcb.dboptimize.mybank.mapper;

import com.cqrcb.dboptimize.mybank.model.entity.TransactionRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TransactionRecordMapper {

    List<TransactionRecord> page(Integer accountId, Integer start, Integer pageSize);

    Long count();
}
