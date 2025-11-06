package com.library.kernel.kernelSql.migrator.core.ddl.constraints;
/**
 * Representa una restricción CHECK en una sentencia DDL.
 * Construye la cláusula SQL correspondiente usando el nombre opcional de la restricción,
 * el nombre de la columna, el operador y el predicado (complemento).
 */
public final class Check extends Constraint {
    /**
     * Nombre de la columna sobre la que se aplica la comprobación.
     */
    String columnName;
    /**
     * Operador usado en la condición CHECK (por ejemplo, =, <, >, LIKE, etc.).
     */
    SQLCheckOperators operator;
    /**
     * Crea una nueva restricción CHECK.
     *
     * @param columnName nombre de la columna afectada por el CHECK
     * @param operator operador que se aplicará en la condición
     * @param predicate expresión o valor con el que se compara la columna (se guarda como complement)
     */
    public Check(String columnName, SQLCheckOperators operator, String predicate) {
        super(predicate);
        this.columnName = columnName;
        this.operator = operator;

    }

    /**
     * Devuelve la representación completa de la restricción CHECK como sentencia SQL.
     * El formato producido es: "CONSTRAINT {constraintName} CHECK ({columnName} {operator} {complement})".
     *
     * @return cadena SQL que representa la restricción CHECK
     */
    @Override
    public String toString() {
        sqlStmt.append(constraintName);
        sqlStmt.append(" CHECK (");
        sqlStmt.append(columnName);
        sqlStmt.append(" ");
        sqlStmt.append(operator.getOperator());
        sqlStmt.append(" ");
        sqlStmt.append(complement);
        sqlStmt.append(")");
        return sqlStmt.toString();
    }
}
