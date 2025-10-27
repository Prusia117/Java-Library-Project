package com.library.kernel.migrator.core.ddl.constraints;

public abstract class Constraint {

    protected String constraintName;
    protected String complement;
    protected String secondComplement;
    protected StringBuilder sqlStmt = new StringBuilder("CONSTRAINT ");

    public Constraint(String complement){
        this.complement = complement;
    }

    public void setConstraintName(String constraintName){
        this.constraintName = constraintName;
    }

    public abstract String toString();

}
