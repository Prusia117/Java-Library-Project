package com.library.kernel.migrator.core.ddl.constraints;

public final class ForeignKey extends Constraint {

    public String externalTableName;
    public String externalTableAttribute;

    public ForeignKey(String complement, String externalTableName, String externalTableAttribute) {
        super(complement);
        this.externalTableName = externalTableName;
        this.externalTableAttribute = externalTableAttribute;
    }

    @Override
    public String toString() {
        sqlStmt.append(constraintName);
        sqlStmt.append(" FOREIGN KEY (");
        sqlStmt.append(complement);
        sqlStmt.append(")");
        sqlStmt.append(" REFERENCES ");
        sqlStmt.append(externalTableName);
        sqlStmt.append("(");
        sqlStmt.append(externalTableAttribute);
        sqlStmt.append(")");
        return sqlStmt.toString();
    }
}
