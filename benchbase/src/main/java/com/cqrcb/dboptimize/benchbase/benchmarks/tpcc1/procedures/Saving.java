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

/**
 * 存取款业务
 *
 * @author Ground
 */
public class Saving extends TPCCProcedure {
    private static final Logger LOG = LoggerFactory.getLogger(Saving.class);

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
        int districtID = TPCCUtil.randomNumber(terminalDistrictLowerID, terminalDistrictUpperID, gen);
        int customerID = TPCCUtil.getCustomerID(gen);

        List<Account> accounts = getAccounts(conn, terminalWarehouseID, districtID, customerID);
        Account a = accounts.get(0);

        TransactionRecord tr = new TransactionRecord();

        if (a.a_balance < 0.01 || gen.nextBoolean()) {
            if (a.a_balance + 0.01 > a.a_balance_lim) {
                return;
            }
            // 存钱
            tr.tr_type = "Deposit";
            tr.tr_amount = TPCCUtil.randomAmount(a.a_balance_lim - a.a_balance, gen);
            a.a_balance += tr.tr_amount;
        } else {
            if (a.a_balance < 0.01) {
                return;
            }
            // 取钱
            tr.tr_type = "Withdraw";
            tr.tr_amount = TPCCUtil.randomAmount(Math.min(a.a_balance, a.a_day_lim), gen);
            a.a_balance -= tr.tr_amount;
            a.a_day_lim -= tr.tr_amount;
        }

        tr.tr_a_id = a.a_id;
        tr.tr_date = new Timestamp(System.currentTimeMillis());
        tr.tr_description = TPCCUtil.randomStr(256);

        insertTransactionRecord(conn, tr);
        updateBalance(conn, a, tr);
    }

    /**
     * 插入交易数据
     */
    private void insertTransactionRecord(Connection conn, TransactionRecord tr) throws SQLException {
        try (PreparedStatement stmtInsertTR = this.getPreparedStatement(conn, stmtInsertTRSQL)) {
            stmtInsertTR.setInt(1, tr.tr_a_id);
            stmtInsertTR.setString(2, tr.tr_type);
            stmtInsertTR.setDouble(3, tr.tr_amount);
            stmtInsertTR.setTimestamp(4, tr.tr_date);
            stmtInsertTR.setString(5, tr.tr_description);

            stmtInsertTR.execute();
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
    private List<Account> getAccounts(Connection conn, int w_id, int d_id, int c_id) throws SQLException {
        List<Account> accounts = new ArrayList<>();

        try (PreparedStatement stmtGetAcct = this.getPreparedStatement(conn, stmtGetAcctSQL)) {
            stmtGetAcct.setInt(1, w_id);
            stmtGetAcct.setInt(2, d_id);
            stmtGetAcct.setInt(3, c_id);

            try (ResultSet rs = stmtGetAcct.executeQuery()) {
                while (rs.next()) {
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

                    accounts.add(a);
                }
            }
        }

        return accounts;
    }
}
