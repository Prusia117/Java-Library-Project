package com.library.kernel.kernelSql.migrator.core.ddl.table;

import com.library.kernel.kernelSql.migrator.core.ddl.utils.SQLDataTypes;
/**
 * Representa una columna de una tabla en una sentencia DDL.
 * Contiene el nombre, tipo de datos, tamaño opcional y si acepta nulos.
 */
public final class Column {
    /**
     * Nombre de la columna.
     */
    private String columName;
    /**
     * Tipo de datos SQL asociado a la columna.
     */
    private SQLDataTypes columnType;
    /**
     * Tamaño de la columna (0 indica que no se especificó tamaño).
     */
    private int columnSize;
    /**
     * Indica si la columna admite valores nulos.
     * Valor por defecto: {@code false} (NO NULL).
     */
    private Boolean nullable = false;
    /**
     * Crea una nueva instancia de {@code Column} con nombre y tipo de datos.
     *
     * @param columName nombre de la columna
     * @param columnType tipo de datos SQL de la columna
     */
    public Column(String columName, SQLDataTypes columnType){
        this.columName = columName;
        this.columnType = columnType;
    }

    /**
     * Establece el tamaño de la columna.
     *
     * @param columnSize tamaño de la columna (por ejemplo, longitud de VARCHAR)
     */
    public void setColumnSize(int columnSize){
        this.columnSize = columnSize;
    }
    /**
     * Define si la columna admite valores nulos.
     *
     * @param nullable {@code true} si permite NULL; {@code false} si es NOT NULL
     */
    public void setNullable(boolean nullable){
        this.nullable = nullable;
    }
    /**
     * Devuelve el nombre de la columna.
     *
     * @return nombre de la columna
     */
    public String getColumName(){
        return columName;
    }
    /**
     * Construye la representación SQL de la columna (nombre, tipo, tamaño y NULL/NOT NULL).
     *
     * @return cadena con la definición SQL de la columna
     */
    public String toString(){
        return columName + " " + columnType + (columnSize != 0 ? ("(" + columnSize + ") ") : " ") + (!nullable ? "NOT NULL" : "NULL");
    }
}
