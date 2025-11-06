package com.library.kernel.kernelSql.migrator.core.ddl.constraints;
/**
 * Clase abstracta que representa una restricción en una sentencia DDL (por ejemplo, una cláusula
 * CONSTRAINT). Proporciona campos comunes como el nombre de la restricción, un complemento con la
 * definición y un `StringBuilder` para construir la sentencia SQL parcial.
 */
public abstract class Constraint {
    /**
     * Nombre opcional de la restricción.
     */
    protected String constraintName;
    /**
     * Complemento de la restricción que describe su definición (por ejemplo, "PRIMARY KEY (id)").
     */
    protected String complement;
    /**
     * Constructor de la sentencia SQL que comienza con la palabra reservada "CONSTRAINT ".
     * Puede completarse posteriormente con el nombre y el complemento.
     */
    protected StringBuilder sqlStmt = new StringBuilder("CONSTRAINT ");
    /**
     * Crea una nueva instancia de {@code Constraint} con el complemento proporcionado.
     *
     * @param complement definición o complemento de la restricción; no debe ser obligatorio
     */
    public Constraint(String complement){
        this.complement = complement;
    }
    /**
     * Establece el nombre de la restricción.
     *
     * @param constraintName nombre a asignar a la restricción
     */
    public void setConstraintName(String constraintName){
        this.constraintName = constraintName;
    }
    /**
     * Debe devolver la representación completa de la restricción como una sentencia SQL.
     * Las implementaciones concretas deben construir y retornar la cadena SQL final.
     *
     * @return sentencia SQL completa que representa la restricción
     */
    public abstract String toString();

}
