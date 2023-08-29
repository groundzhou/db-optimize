package com.cqrcb.dboptimize.benchbase.benchmarks.tpcc1.procedures;

import com.cqrcb.dboptimize.benchbase.api.SQLStmt;
import com.cqrcb.dboptimize.benchbase.benchmarks.tpcc1.TPCCConstants;
import com.cqrcb.dboptimize.benchbase.benchmarks.tpcc1.TPCCUtil;
import com.cqrcb.dboptimize.benchbase.benchmarks.tpcc1.TPCCWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class BillQuery extends TPCCProcedure {
    private static final Logger LOG = LoggerFactory.getLogger(BillQuery.class);

    public final SQLStmt stmtGetAcctSQL = new SQLStmt(
            """
                    SELECT a_id, a_type, a_number, a_balance, a_balance_lim, a_day_lim
                    FROM %s
                    WHERE a_w_id = ?
                      AND a_d_id = ?
                      AND a_c_id = ?;
                    """.formatted(TPCCConstants.TABLENAME_ACCOUNT));

    public final SQLStmt stmtGetTRSQL = new SQLStmt(
            """
                    SELECT tr_id, tr_a_id, tr_type, tr_amount, tr_date, tr_description, tr_created_at, tr_updated_at
                    FROM %s a JOIN %s tr ON a.a_id = tr.tr_a_id
                    WHERE a_w_id = ?
                      AND a_d_id = ?
                      AND a_c_id = ?;
                    """.formatted(TPCCConstants.TABLENAME_ACCOUNT, TPCCConstants.TABLENAME_TRANSACTIONRECORD));

    @Override
    public void run(Connection conn, Random gen, int terminalWarehouseID, int numWarehouses, int terminalDistrictLowerID, int terminalDistrictUpperID, TPCCWorker w) throws SQLException {
        // 1. 查询 account
        int districtID = TPCCUtil.randomNumber(terminalDistrictLowerID, terminalDistrictUpperID, gen);
        int customerID = TPCCUtil.getCustomerID(gen);
        getTransactionRecords(conn, terminalWarehouseID, districtID, customerID);
    }

    private void getTransactionRecords(Connection conn, int w_id, int d_id, int c_id) throws SQLException {
        try (PreparedStatement stmtGetTR = this.getPreparedStatement(conn, stmtGetTRSQL)) {
            stmtGetTR.setInt(1, w_id);
            stmtGetTR.setInt(2, d_id);
            stmtGetTR.setInt(3, c_id);
            try (ResultSet rs = stmtGetTR.executeQuery()) {
                if (!rs.next()) {
                    throw new RuntimeException("C_D_ID=" + d_id + " C_ID=" + c_id + " not found!");
                }
            }
        }
    }
}
