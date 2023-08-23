package com.cqrcb.dboptimize.benchbase.benchmarks.tpcc.procedures;

import com.cqrcb.dboptimize.benchbase.benchmarks.tpcc.TPCCWorker;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;

public class Withdraw extends TPCCProcedure {
    @Override
    public void run(Connection conn, Random gen, int terminalWarehouseID, int numWarehouses, int terminalDistrictLowerID, int terminalDistrictUpperID, TPCCWorker w) throws SQLException {

    }
}
