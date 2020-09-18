package com.chenjing.transactional;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author Chenjing
 * @date 2020/9/17
 */
public interface Transaction {

    void commit(DataSource[] dataSources) throws SQLException;

    void beginTransaction(DataSource[] dataSources) throws SQLException;

    void rollback(DataSource[] dataSources) throws SQLException;
}
