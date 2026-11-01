package com.library.kernel.kernelSql.orm.core;

import com.library.kernel.kernelSql.orm.core.utils.SQLBuilderException;
import com.library.kernel.kernelSql.orm.entity.Entity;

import java.util.HashMap;
/**
 * Define las operaciones para construir sentencias SQL a partir de entidades.
 *
 * Esta interfaz expone métodos para generar consultas SELECT, INSERT, UPDATE y DELETE,
 * así como para crear las cláusulas JOIN necesarias cuando una entidad tiene relaciones.
 * Los implementadores deben devolver sentencias parametrizadas ({@link SQLStatement}) y lanzar
 * {@link SQLBuilderException} ante parámetros inválidos o errores de generación.
 *
 * Nota: la responsabilidad de ejecutar las sentencias y gestionar conexiones corresponde
 * a otra capa; aquí solo se construyen las cadenas SQL.
 *
 * @param <T> Tipo genérico no usado directamente, pero mantiene la semántica de entidad.
 */
abstract interface ISQLBuilder {
    /**
     * Crea las cláusulas JOIN y las columnas adicionales que provienen de entidades relacionadas.
     *
     * @param relationships Mapa donde la clave es la clase de la entidad relacionada y el valor
     *                      es el campo que une la entidad dependiente con la independiente.
     * @return Un arreglo de dos posiciones:
     *         [0] = cadena con los JOINs (por ejemplo " INNER JOIN ... ON ...")
     *         [1] = cadena con las columnas adicionales separadas por comas
     * @throws SQLBuilderException Si el parámetro es nulo, vacío o ocurre un error al crear las entidades relacionadas.
     */
    public String[] createJoinsStatments(HashMap<Class<?>, String> relationships) throws SQLBuilderException;
    /**
     * Construye una sentencia SELECT para la entidad indicada.
     *
     * @param entity Entidad sobre la que se construye la consulta (usa metadata como tableName y primaryKey).
     * @param value  Valor para la cláusula WHERE; si es {@code null} se omite la cláusula WHERE.
     * @param field  Campo a usar en la cláusula WHERE; si es {@code null} se usa la clave primaria.
     * @return Sentencia SELECT parametrizada.
     * @throws SQLBuilderException Si la entidad es nula o ocurre un error durante la construcción.
     */
    public SQLStatement createSelectQuery(Entity entity, String value, String field) throws SQLBuilderException;
    /**
     * Construye una sentencia INSERT para la entidad proporcionada.
     *
     * @param entity Entidad cuyos campos se convertirán en columnas y valores para INSERT.
     * @return Sentencia INSERT parametrizada.
     * @throws SQLBuilderException Si ocurre un error al generar la sentencia.
     */
    public SQLStatement createInsertQuery(Entity entity) throws SQLBuilderException;
    /**
     * Construye una sentencia UPDATE para la entidad especificada.
     *
     * @param entity Entidad con los nuevos valores a persistir.
     * @param id     Identificador (valor) de la fila a actualizar.
     * @return Sentencia UPDATE parametrizada.
     * @throws SQLBuilderException Si alguno de los parámetros es nulo o ocurre un error.
     */
    public SQLStatement createUpdateQuery(Entity entity, String id) throws SQLBuilderException;
    /**
     * Construye una sentencia DELETE para la entidad y el id proporcionados.
     *
     * @param entity Entidad desde la cual se eliminará la fila (usa tableName y primaryKey).
     * @param id     Identificador (valor) de la fila a eliminar.
     * @return Sentencia DELETE parametrizada.
     * @throws SQLBuilderException Si alguno de los parámetros es nulo o inválido.
     */
    public SQLStatement createDeleteQuery(Entity entity, String id) throws SQLBuilderException;

}
