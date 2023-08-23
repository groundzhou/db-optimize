package com.cqrcb.dboptimize.benchbase.benchmarks.tpcc.procedures;

import com.cqrcb.dboptimize.benchbase.benchmarks.tpcc.TPCCWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;

public class Deposit extends TPCCProcedure {
    private static final Logger LOG = LoggerFactory.getLogger(NewOrder.class);

    @Override
    public void run(Connection conn, Random gen, int terminalWarehouseID, int numWarehouses, int terminalDistrictLowerID, int terminalDistrictUpperID, TPCCWorker w) throws SQLException {

    }
}
