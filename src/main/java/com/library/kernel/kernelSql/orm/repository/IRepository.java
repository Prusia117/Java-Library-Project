package com.library.kernel.kernelSql.orm.repository;

import com.library.kernel.kernelSql.orm.entity.Entity;
import com.library.kernel.kernelSql.orm.repository.utils.RepositoryException;

import java.util.ArrayList;
import java.util.List;
/**
 * Interfaz genérica para repositorios que manejan entidades del tipo {@code T}.
 *
 * @param <T> tipo de entidad que extiende {@link Entity}
 */
public abstract interface IRepository<T extends Entity> {
    /**
     * Obtiene todas las entidades disponibles en el repositorio.
     *
     * @return lista de entidades {@code T}; puede estar vacía si no hay resultados
     * @throws RepositoryException si ocurre un error al acceder al repositorio
     */
    public abstract ArrayList<T> getAll() throws RepositoryException;
    /**
     * Obtiene las entidades que coinciden con el identificador proporcionado.
     *
     * @param id identificador de la(s) entidad(es) a recuperar
     * @return lista de entidades que coinciden con {@code id}; puede contener múltiples entradas o estar vacía
     * @throws RepositoryException si ocurre un error al acceder al repositorio
     */
    public abstract ArrayList<T> getById(String id) throws RepositoryException;
    /**
     * Persiste una nueva entidad en el repositorio.
     *
     * @param entity entidad a guardar
     * @return {@code true} si la operación fue exitosa; {@code false} en caso contrario
     * @throws RepositoryException si ocurre un error al guardar la entidad
     */
    public abstract boolean save(T entity) throws RepositoryException;
    /**
     * Actualiza la entidad identificada por {@code id} con los datos de {@code entity}.
     *
     * @param entity entidad con los datos a actualizar
     * @param id identificador de la entidad a actualizar
     * @return {@code true} si la actualización fue exitosa; {@code false} en caso contrario
     * @throws RepositoryException si ocurre un error al actualizar la entidad
     */
    public abstract boolean update(T entity, String id) throws RepositoryException;
    /**
     * Elimina la entidad identificada por {@code id} del repositorio.
     *
     * @param id identificador de la entidad a eliminar
     * @return {@code true} si la eliminación fue exitosa; {@code false} en caso contrario
     * @throws RepositoryException si ocurre un error al eliminar la entidad
     */
    public abstract boolean delete(String id) throws RepositoryException;
}
