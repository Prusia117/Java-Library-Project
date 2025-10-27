package com.library.kernel.kernelSql.migrator.core.ddl.constraints;

public final class PrimaryKey extends Constraint{

    public PrimaryKey(String columnName) {
        super(columnName);
    }

    @Override
    public String toString() {
        sqlStmt.append(constraintName);
        sqlStmt.append(" PRIMARY KEY (");
        sqlStmt.append(complement);
        sqlStmt.append(")");
        return sqlStmt.toString();
    }
}
