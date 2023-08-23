package com.cqrcb.dboptimize.mybank.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer userId;

    private String accountNumber;

    private BigDecimal balance;

    private Date createdAt;

    private Date updatedAt;
}
