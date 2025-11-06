package com.library.kernel.kernelSql.migrator.core.ddl.constraints;
/**
 * Representa una restricción UNIQUE en una sentencia DDL.
 * Utiliza el campo {@code complement} heredado de {@code Constraint} para almacenar
 * la(s) columna(s) que forman la restricción UNIQUE.
 */
public final class UniqueKey extends Constraint{
    /**
     * Crea una nueva restricción UNIQUE para la(s) columna(s) indicada(s).
     *
     * @param complement nombre de la columna o lista de columnas (por ejemplo "email" o "col1, col2")
     */
    public UniqueKey(String complement) {
        super(complement);
    }
    /**
     * Construye y devuelve la representación SQL completa de la restricción UNIQUE.
     * Formato resultante:
     * CONSTRAINT {constraintName} UNIQUE ({complement})
     *
     * @return cadena SQL que representa la restricción UNIQUE
     */
    @Override
    public String toString() {
        sqlStmt.append(constraintName);
        sqlStmt.append(" UNIQUE (");
        sqlStmt.append(complement);
        sqlStmt.append(")");
        return sqlStmt.toString();
    }
}
