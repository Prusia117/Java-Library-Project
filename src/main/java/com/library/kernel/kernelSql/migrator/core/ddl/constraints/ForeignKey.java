package com.library.kernel.kernelSql.migrator.core.ddl.constraints;
/**
 * Representa una restricción FOREIGN KEY en una sentencia DDL.
 * Construye la cláusula SQL correspondiente usando un nombre opcional de restricción,
 * las columnas locales indicadas en {@code complement} y la tabla/columna externa referenciada.
 */
public final class ForeignKey extends Constraint {
    /**
     * Nombre de la tabla externa referenciada por la clave foránea.
     */
    public String externalTableName;
    /**
     * Nombre de la columna en la tabla externa a la que se refiere la clave foránea.
     */
    public String externalTableAttribute;
    /**
     * Crea una nueva instancia de {@code ForeignKey}.
     *
     * @param complement expresión que representa la(s) columna(s) local(es) que forman la clave foránea
     * @param externalTableName nombre de la tabla externa referenciada
     * @param externalTableAttribute nombre de la columna en la tabla externa referenciada
     */
    public ForeignKey(String complement, String externalTableName, String externalTableAttribute) {
        super(complement);
        this.externalTableName = externalTableName;
        this.externalTableAttribute = externalTableAttribute;
    }
    /**
     * Construye y devuelve la representación SQL completa de la restricción FOREIGN KEY.
     * Formato resultante:
     * <pre>
     * CONSTRAINT {constraintName} FOREIGN KEY ({complement}) REFERENCES {externalTableName}({externalTableAttribute})
     * </pre>
     *
     * @return cadena SQL que representa la restricción FOREIGN KEY
     */
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
