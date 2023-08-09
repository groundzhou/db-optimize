package com.cqrcb.dboptimize.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class TransactionRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer accountId;

    private String transactionType;

    private BigDecimal amount;

    private String currency;

    private Date transactionDate;

    private Date createdAt;

    private Date updatedAt;
}
