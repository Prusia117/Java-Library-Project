package com.library.kernel.kernelSql.migrator.core.ddl.constraints;

public final class UniqueKey extends Constraint{

    public UniqueKey(String complement) {
        super(complement);
    }

    @Override
    public String toString() {
        sqlStmt.append(constraintName);
        sqlStmt.append(" UNIQUE (");
        sqlStmt.append(complement);
        sqlStmt.append(")");
        return sqlStmt.toString();
    }
}
