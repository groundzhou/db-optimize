package com.cqrcb.dboptimize.benchbase.benchmarks.tpcc1.procedures;

import com.cqrcb.dboptimize.benchbase.api.SQLStmt;
import com.cqrcb.dboptimize.benchbase.benchmarks.tpcc.TPCCConstants;
import com.cqrcb.dboptimize.benchbase.benchmarks.tpcc1.TPCCUtil;
import com.cqrcb.dboptimize.benchbase.benchmarks.tpcc1.TPCCWorker;
import com.cqrcb.dboptimize.benchbase.benchmarks.tpcc1.pojo.Account;
import com.cqrcb.dboptimize.benchbase.benchmarks.tpcc1.pojo.TransactionRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Transfer extends TPCCProcedure {
    private static final Logger LOG = LoggerFactory.getLogger(Transfer.class);

    public final SQLStmt stmtGetAcctSQL = new SQLStmt(
            """
                    SELECT a_id, a_type, a_number, a_balance, a_balance_lim, a_day_lim
                    FROM %s
                    WHERE a_w_id = ?
                      AND a_d_id = ?
                      AND a_c_id = ?;
                    """.formatted(TPCCConstants.TABLENAME_ACCOUNT));

    public final SQLStmt stmtInsertTRSQL = new SQLStmt(
            """
                    INSERT INTO %s (tr_a_id, tr_type, tr_amount, tr_date, tr_description)
                    VALUES (?, ?, ?, ?, ?)
                    """.formatted(TPCCConstants.TABLENAME_TRANSACTIONRECORD));

    public final SQLStmt stmtUpdateBalanceSQL = new SQLStmt(
            """
                    UPDATE %s
                    SET a_balance = ?,
                        a_day_lim = ?,
                        a_updated_at = ?
                    WHERE a_id = ?;
                    """.formatted(TPCCConstants.TABLENAME_ACCOUNT));

    @Override
    public void run(Connection conn, Random gen, int terminalWarehouseID, int numWarehouses, int terminalDistrictLowerID, int terminalDistrictUpperID, TPCCWorker w) throws SQLException {
        int fromDistrictID = TPCCUtil.randomNumber(terminalDistrictLowerID, terminalDistrictUpperID, gen);
        int fromCustomerID = TPCCUtil.getCustomerID(gen);

        int toDistrictID = TPCCUtil.randomNumber(terminalDistrictLowerID, terminalDistrictUpperID, gen);
        int toCustomerID = TPCCUtil.getCustomerID(gen);

        Account a1 = getAccount(conn, terminalWarehouseID, fromDistrictID, fromCustomerID);
        Account a2 = getAccount(conn, terminalWarehouseID, toDistrictID, toCustomerID);

        if (a1.a_balance < 0.01 || a1.a_day_lim < 0.01 || a2.a_balance + 0.01 > a2.a_balance_lim) {
            return;
        }

        TransactionRecord tr1 = new TransactionRecord();
        TransactionRecord tr2 = new TransactionRecord();

        double amount = TPCCUtil.randomAmount(
                Math.min(a1.a_balance, Math.min(a1.a_day_lim, a2.a_balance_lim - a2.a_balance)), gen);

        // 转出
        tr1.tr_type = "Transfer-Out";
        tr1.tr_amount = amount;
        a1.a_balance -= amount;
        a1.a_day_lim -= amount;

        tr1.tr_a_id = a1.a_id;
        tr1.tr_date = new Timestamp(System.currentTimeMillis());
        tr1.tr_description = TPCCUtil.randomStr(256);

        // 转入
        tr2.tr_type = "Transfer-In";
        tr2.tr_amount = amount;
        a2.a_balance += amount;

        tr2.tr_a_id = a2.a_id;
        tr2.tr_date = tr1.tr_date;
        tr2.tr_description = TPCCUtil.randomStr(256);

        List<TransactionRecord> records = new ArrayList<>();
        records.add(tr1);
        records.add(tr2);

        insertTransferRecords(conn, records);
        updateBalance(conn, a1, tr1);
        updateBalance(conn, a2, tr2);
    }

    /**
     * 插入交易数据
     */
    private void insertTransferRecords(Connection conn, List<TransactionRecord> records) throws SQLException {
        try (PreparedStatement stmtInsertTR = this.getPreparedStatement(conn, stmtInsertTRSQL)) {
            for (TransactionRecord tr : records) {
                stmtInsertTR.setInt(1, tr.tr_a_id);
                stmtInsertTR.setString(2, tr.tr_type);
                stmtInsertTR.setDouble(3, tr.tr_amount);
                stmtInsertTR.setTimestamp(4, tr.tr_date);
                stmtInsertTR.setString(5, tr.tr_description);

                stmtInsertTR.addBatch();
            }

            stmtInsertTR.executeBatch();
            stmtInsertTR.clearBatch();
        }
    }

    /**
     * 修改余额
     */
    private void updateBalance(Connection conn, Account a, TransactionRecord tr) throws SQLException {
        try (PreparedStatement stmtUpdateBalance = this.getPreparedStatement(conn, stmtUpdateBalanceSQL)) {
            stmtUpdateBalance.setDouble(1, a.a_balance);
            stmtUpdateBalance.setDouble(2, a.a_day_lim);
            stmtUpdateBalance.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            stmtUpdateBalance.setInt(4, tr.tr_a_id);

            stmtUpdateBalance.execute();
        }
    }

    /**
     * 根据客户查找账户
     */
    private Account getAccount(Connection conn, int w_id, int d_id, int c_id) throws SQLException {

        try (PreparedStatement stmtGetAcct = this.getPreparedStatement(conn, stmtGetAcctSQL)) {
            stmtGetAcct.setInt(1, w_id);
            stmtGetAcct.setInt(2, d_id);
            stmtGetAcct.setInt(3, c_id);

            try (ResultSet rs = stmtGetAcct.executeQuery()) {
                if (rs.next()) {
                    Account a = new Account();

                    a.a_w_id = w_id;
                    a.a_d_id = d_id;
                    a.a_c_id = c_id;
                    a.a_id = rs.getInt("a_id");
                    a.a_type = rs.getInt("a_type");
                    a.a_number = rs.getString("a_number");
                    a.a_balance = rs.getDouble("a_balance");
                    a.a_balance_lim = rs.getDouble("a_balance_lim");
                    a.a_day_lim = rs.getDouble("a_day_lim");

                    return a;
                } else {
                    throw new RuntimeException("C_D_ID=" + d_id + " C_ID=" + c_id + " not found!");
                }
            }
        }
    }
}
