package com.chenjing.transactional;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Chenjing
 * @date 2020/9/17
 */
public interface Transaction {

    void commit(Connection[] connections) throws SQLException;

    void beginTransaction(Connection[] connections) throws SQLException;

    void rollback(Connection[] connections) throws SQLException;
}
