package com.library.kernel.kernelSql.migrator.core.ddl.constraints;

public final class Check extends Constraint {

    String columnName;
    SQLCheckOperators operator;

    public Check(String columnName, SQLCheckOperators operator, String predicate) {
        super(predicate);
        this.columnName = columnName;
        this.operator = operator;

    }

    @Override
    public String toString() {
        sqlStmt.append(constraintName);
        sqlStmt.append(" CHECK (");
        sqlStmt.append(columnName);
        sqlStmt.append(" ");
        sqlStmt.append(operator.getOperator());
        sqlStmt.append(" ");
        sqlStmt.append(complement);
        sqlStmt.append(")");
        return sqlStmt.toString();
    }
}
