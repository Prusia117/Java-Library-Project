package com.library.kernel.kernelSql.migrator.core.ddl.table;

import com.library.kernel.kernelSql.migrator.core.ddl.constraints.*;
import com.library.kernel.kernelSql.migrator.core.ddl.utils.SQLDataTypes;

import java.util.ArrayList;
import java.util.Locale;
/**
 * Representa el esquema (definición) de una tabla para generar la sentencia DDL
 * \"CREATE TABLE\". Permite añadir columnas y restricciones de forma encadenada.
 */
public final class Schema {
    /**
     * Nombre de la tabla que se va a crear.
     */
    private String tableName;
    /**
     * Lista de columnas definidas en este esquema.
     */
    private ArrayList<Column> columnArray = new ArrayList<Column>();
    /**
     * Lista de restricciones (PRIMARY, FOREIGN, UNIQUE, CHECK, ...) asociadas a la tabla.
     */
    private ArrayList<Constraint> constraintArray = new ArrayList<Constraint>();
    /**
     * Crea un nuevo esquema para la tabla indicada.
     *
     * @param tableName nombre de la tabla
     */
    public Schema(String tableName){
        this.tableName = tableName;
    }
    /**
     * Añade una nueva columna al esquema con un tipo de datos especificado.
     *
     * @param columnName nombre de la columna
     * @param columnType tipo de datos SQL de la columna
     * @return la instancia actual de {@code Schema} para encadenar llamadas
     */
    public Schema addColumn(String columnName, SQLDataTypes columnType){
        Column newColumn = new Column(columnName, columnType);
        columnArray.add(newColumn);
        return this;
    }
    /**
     * Establece si la última columna añadida permite valores nulos.
     *
     * @param value {@code true} si la columna admite NULL; {@code false} si es NOT NULL
     * @return la instancia actual de {@code Schema} para encadenar llamadas
     */
    public Schema nullable(boolean value){
        columnArray.get(columnArray.size() -1).setNullable(value);
        return this;
    }
    /**
     * Establece el tamaño (por ejemplo longitud de VARCHAR) de la última columna añadida.
     *
     * @param value tamaño de la columna
     * @return la instancia actual de {@code Schema} para encadenar llamadas
     */
    public Schema size(int value){
        columnArray.get(columnArray.size() -1).setColumnSize(value);
        return this;
    }
    /**
     * Añade una restricción PRIMARY KEY sobre la última columna añadida.
     * Genera un nombre de restricción por convención: {table}_{column}___pk.
     *
     * @return la instancia actual de {@code Schema} para encadenar llamadas
     */
    public Schema primaryKey(){
        String columnName = columnArray.get(columnArray.size() - 1).getColumName();
        PrimaryKey newPrimaryKey = new PrimaryKey(columnName);
        newPrimaryKey.setConstraintName(tableName.toLowerCase() + "_" + columnName.toLowerCase() + "___pk");
        constraintArray.add(newPrimaryKey);
        return this;
    }

    /**
     * Añade una restricción FOREIGN KEY sobre la última columna añadida.
     * Genera un nombre de restricción por convención: {table}_{column}___fk.
     *
     * @param externalTableName nombre de la tabla referenciada
     * @param externalTableAttribute nombre de la columna referenciada en la tabla externa
     * @return la instancia actual de {@code Schema} para encadenar llamadas
     */
    public Schema foreignKey(String externalTableName, String externalTableAttribute){
        String columnName = columnArray.get(columnArray.size() - 1).getColumName();
        ForeignKey newForeignKey = new ForeignKey(columnName, externalTableName, externalTableAttribute);
        newForeignKey.setConstraintName(tableName.toLowerCase() + "_" + columnName.toLowerCase() + "___fk");
        constraintArray.add(newForeignKey);
        return this;
    }
    /**
     * Añade una restricción UNIQUE sobre la última columna añadida.
     * Genera un nombre de restricción por convención: {table}_{column}___uk.
     *
     * @return la instancia actual de {@code Schema} para encadenar llamadas
     */
    public Schema uniqueKey(){
        String columnName = columnArray.get(columnArray.size() - 1).getColumName();
        UniqueKey newUniqueKey = new UniqueKey(columnName);
        newUniqueKey.setConstraintName(tableName.toLowerCase() + "_" + columnName.toLowerCase() + "___uk");
        constraintArray.add(newUniqueKey);
        return this;
    }
    /**
     * Añade una restricción CHECK sobre la última columna añadida.
     * Genera un nombre de restricción por convención: {table}_{column}___ck.
     *
     * @param operator operador SQL para la condición (por ejemplo EQUALS, LIKE, IN)
     * @param predicate expresión o valor a comparar en la condición CHECK
     * @return la instancia actual de {@code Schema} para encadenar llamadas
     */
    public Schema addCheck(SQLCheckOperators operator, String predicate){
        String columnName = columnArray.get(columnArray.size() - 1).getColumName();
        Check newCheck = new Check(columnName, operator ,predicate);
        newCheck.setConstraintName(tableName.toLowerCase() + "_" + columnName.toLowerCase() + "___ck");
        constraintArray.add(newCheck);
        return this;
    }
    /**
     * Construye y devuelve la sentencia SQL completa para crear la tabla con sus columnas
     * y restricciones.
     *
     * @return cadena con la sentencia DDL \"CREATE TABLE ...\"
     */
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
