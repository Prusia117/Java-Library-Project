package com.library.kernel.migrator.core.ddl.constraints;

public final class Check extends Constraint {

    public Check(String predicate) {
        super(predicate);
    }

    @Override
    public String toString() {
        sqlStmt.append(constraintName);
        sqlStmt.append(" CHECK (");
        sqlStmt.append(complement);
        sqlStmt.append(")");
        return sqlStmt.toString();
    }
}
