package com.library.kernel.kernelSql.migrator.core.ddl.constraints;

public enum SQLCheckOperators {

    EQUALS(
            "="
    ),
    NOT_EQUALS(
            "!="
    ),
    GREATER_THAN(
            ">"
    ),
    LESS_THAN(
            "<"
    ),
    GREATER_THAN_OR_EQUALS(
            ">="
    ),
    LESS_THAN_OR_EQUALS(
            "=<"
    ),
    IN(
            "IN"
    ),
    LIKE(
            "LIKE"
    );

    private final String operator;

    SQLCheckOperators(String operator){
        this.operator = operator;
    }

    public String getOperator(){
        return this.operator;
    }
}
