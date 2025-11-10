package com.library.kernel.kernelSql.orm.core;

import com.library.kernel.kernelSql.orm.core.utils.ORMException;
import com.library.kernel.kernelSql.orm.entity.Entity;

import java.util.ArrayList;
import java.util.List;
/**
 * Interfaz que define las operaciones básicas del motor ORM para entidades.
 *
 * Proporciona métodos para realizar consultas de selección, actualización y
 * eliminación sobre instancias de {@link com.library.kernel.kernelSql.orm.entity.Entity}.
 * Las implementaciones deben encargarse de la gestión de conexiones, el mapeo
 * entre filas SQL y objetos, y del manejo apropiado de transacciones y recursos.
 *
 * @param <T> Tipo concreto de {@code Entity} manejado por el motor ORM.
 */
public abstract interface IORMEngine<T extends Entity> {

    /**
     * Ejecuta una consulta de selección sobre la entidad indicada.
     *
     * @param entity Instancia que representa la entidad (se usa su clase/metadata
     *               para construir la consulta). Puede contener valores base o
     *               simplemente servir como referencia del tipo.
     * @param value  Valor para la cláusula WHERE; si es {@code null} se omite la cláusula.
     * @param field  Nombre del campo a usar en la cláusula WHERE; si es {@code null}
     *               se utilizará la clave primaria de la entidad.
     * @return Lista de entidades del tipo {@code T} resultantes de la consulta.
     * @throws ORMException Si ocurre un error en la capa ORM o en la ejecución de la consulta.
     */
    public abstract ArrayList<T> selectQuery(T entity, String value, String field) throws ORMException;
    /**
     * Ejecuta una operación de actualización (UPDATE) para la entidad proporcionada.
     *
     * @param entity Entidad que contiene los nuevos valores a persistir.
     * @param id     Identificador (valor) de la fila a actualizar.
     * @return Número de filas afectadas por la operación.
     * @throws ORMException Si ocurre un error durante la actualización.
     */
    public abstract int updateQuery(T entity, String id) throws ORMException;
    /**
     * Ejecuta una operación de eliminación (DELETE) para la entidad y el id indicados.
     *
     * @param entity Entidad que define la tabla/metadata sobre la cual se realiza el borrado.
     * @param id     Identificador (valor) de la fila a eliminar.
     * @return Número de filas afectadas por la operación de borrado.
     * @throws ORMException Si ocurre un error durante la eliminación.
     */
    public abstract int deleteQuery(T entity, String id) throws ORMException;

}
