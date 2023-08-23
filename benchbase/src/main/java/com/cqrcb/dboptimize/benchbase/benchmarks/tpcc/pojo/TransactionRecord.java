package com.cqrcb.dboptimize.benchbase.benchmarks.tpcc.pojo;

import java.sql.Timestamp;

public class TransactionRecord {

    public int tr_id;
    public int tr_a_id;
    public String tr_type;
    public float tr_amount;
    public Timestamp tr_date;
    public String tr_description;
    public Timestamp tr_created_at;
    public Timestamp tr_updated_at;

}
