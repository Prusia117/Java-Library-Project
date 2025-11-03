package com.library.kernel.kernelSql.orm.core;

import com.library.kernel.kernelSql.orm.core.utils.SQLExecutorException;

import java.sql.Connection;
import java.sql.ResultSet;

abstract interface ISQLExecutor {

    public abstract ResultSet excuteSelectQuery(Connection connection, String sqlStmt) throws SQLExecutorException;

    public abstract int executeUpdateStatment(Connection connection, String sqlStmt) throws SQLExecutorException;

}
