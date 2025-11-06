package com.library.kernel.kernelSql.migrator.core.ddl.constraints;
/**
 * Representa una restricción PRIMARY KEY en una sentencia DDL.
 * Utiliza el campo {@code complement} heredado de {@code Constraint} para almacenar
 * el nombre de la(s) columna(s) que forman la clave primaria.
 */
public final class PrimaryKey extends Constraint{
    /**
     * Crea una nueva restricción PRIMARY KEY para la(s) columna(s) indicada(s).
     *
     * @param columnName nombre de la columna o lista de columnas (por ejemplo "id" o "id, other_id")
     */
    public PrimaryKey(String columnName) {
        super(columnName);
    }
    /**
     * Construye y devuelve la representación SQL completa de la restricción PRIMARY KEY.
     * Formato resultante:
     * CONSTRAINT {constraintName} PRIMARY KEY ({complement})
     *
     * @return cadena SQL que representa la restricción PRIMARY KEY
     */
    @Override
    public String toString() {
        sqlStmt.append(constraintName);
        sqlStmt.append(" PRIMARY KEY (");
        sqlStmt.append(complement);
        sqlStmt.append(")");
        return sqlStmt.toString();
    }
}
