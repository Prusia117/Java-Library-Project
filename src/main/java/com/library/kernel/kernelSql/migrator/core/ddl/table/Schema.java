package com.library.kernel.kernelSql.migrator.core.ddl.table;

import com.library.kernel.kernelSql.migrator.core.ddl.constraints.*;
import com.library.kernel.kernelSql.migrator.core.ddl.utils.SQLDataTypes;

import java.util.ArrayList;
import java.util.Locale;

public final class Schema {

    private String tableName;
    private ArrayList<Column> columnArray = new ArrayList<Column>();
    private ArrayList<Constraint> constraintArray = new ArrayList<Constraint>();

    public Schema(String tableName){
        this.tableName = tableName;
    }

    public Schema addColumn(String columnName, SQLDataTypes columnType){
        Column newColumn = new Column(columnName, columnType);
        columnArray.add(newColumn);
        return this;
    }

    public Schema nullable(boolean value){
        columnArray.get(columnArray.size() -1).setNullable(value);
        return this;
    }

    public Schema size(int value){
        columnArray.get(columnArray.size() -1).setColumnSize(value);
        return this;
    }

    public Schema primaryKey(){
        String columnName = columnArray.get(columnArray.size() - 1).getColumName();
        PrimaryKey newPrimaryKey = new PrimaryKey(columnName);
        newPrimaryKey.setConstraintName(tableName.toLowerCase(Locale.ROOT) + "_" + columnName + "___pk");
        constraintArray.add(newPrimaryKey);
        return this;
    }


    public Schema foreignKey(String externalTableName, String externalTableAttribute){
        String columnName = columnArray.get(columnArray.size() - 1).getColumName();
        ForeignKey newForeignKey = new ForeignKey(columnName, externalTableName, externalTableAttribute);
        newForeignKey.setConstraintName(tableName.toLowerCase(Locale.ROOT) + "_" + columnName + "___fk");
        constraintArray.add(newForeignKey);
        return this;
    }

    public Schema uniqueKey(){
        String columnName = columnArray.get(columnArray.size() - 1).getColumName();
        UniqueKey newUniqueKey = new UniqueKey(columnName);
        newUniqueKey.setConstraintName(tableName.toLowerCase(Locale.ROOT) + "_" + columnName + "___uk");
        constraintArray.add(newUniqueKey);
        return this;
    }

    public Schema addCheck(SQLCheckOperators operator, String predicate){
        String columnName = columnArray.get(columnArray.size() - 1).getColumName();
        Check newCheck = new Check(columnName, operator ,predicate);
        newCheck.setConstraintName(tableName.toLowerCase(Locale.ROOT) + "_" + columnName + "___ck");
        constraintArray.add(newCheck);
        return this;
    }

    public String toString(){

        StringBuilder sqlStmt = new StringBuilder("CREATE TABLE ");
        sqlStmt.append(tableName).append(" (");

        for (int i = 0; i <= columnArray.size()-1; i++) {
            sqlStmt.append(columnArray.get(i).toString())
                    .append(", ");
        }

        for (int j = 0; j <= constraintArray.size()-1; j++) {
            sqlStmt.append(constraintArray.get(j).toString());
            if (j != constraintArray.size() - 1){
                sqlStmt.append(", ");
            }
        }

        sqlStmt.append(");");
        return sqlStmt.toString();
    }
}
