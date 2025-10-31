package com.library.kernel.kernelSql.migrator.core.ddl.constraints;

public abstract class Constraint {

    protected String constraintName;
    protected String complement;
    protected StringBuilder sqlStmt = new StringBuilder("CONSTRAINT ");

    public Constraint(String complement){
        this.complement = complement;
    }

    public void setConstraintName(String constraintName){
        this.constraintName = constraintName;
    }

    public abstract String toString();

}
