package com.library.kernel.kernelSql.orm.repository;

import java.util.ArrayList;
import java.util.List;

import com.library.kernel.classes.Colors;
import com.library.kernel.kernelSql.orm.core.IORMEngine;
import com.library.kernel.logger.Logger;
import com.library.kernel.kernelSql.orm.core.ORM;
import com.library.kernel.kernelSql.orm.core.utils.ORMException;
import com.library.kernel.kernelSql.orm.entity.Entity;
import com.library.kernel.kernelSql.orm.entity.EntityFactory;
import com.library.kernel.kernelSql.orm.entity.utils.EntityFactoryException;
import com.library.kernel.kernelSql.orm.repository.utils.RepositoryException;

/**
 * Repositorio genérico para entidades {@code Entity}.
 *
 * Implementa el patrón Repository: centraliza el acceso a datos y delega la
 * construcción y ejecución de consultas al motor ORM ({@link ORM}).
 *
 * Responsabilidades:
 * - Proveer métodos CRUD básicos (listar, obtener por id, guardar, actualizar, eliminar).
 * - Manejar excepciones de capa de repositorio y registrar errores mediante {@link Logger}.
 *
 * @param <T> Tipo concreto que extiende {@link Entity}.
 */
public final class Repository<T extends Entity> implements IRepository<T>, Colors
{
    /**
     * Instancia del motor ORM que realiza las consultas sobre la entidad.
     */
    private IORMEngine<T> orm;

    /**
     * Clase de la entidad que este repositorio manejará.
     */
    private Class<T> entityType;
    /**
     * Crea un repositorio para el tipo de entidad proporcionado.
     *
     * @param type Clase concreta de la entidad que gestionará este repositorio.
     */
    public Repository(Class<T> type)
    {
        orm = new ORM<T>();

        this.entityType = type;
    }

    /**
     * Obtiene todos los registros de la entidad.
     *
     * @return Lista de instancias de la entidad.
     * @throws RepositoryException Si ocurre cualquier error en la operación del repositorio.
     */
    @Override
    public ArrayList<T> getAll() throws RepositoryException
    {
        try {
            // Delegar al ORM la obtención de todos los registros (sin filtro)
            ArrayList<T> entityArrayList = orm.selectQuery(EntityFactory.createEntity(entityType),null, null);
            
            return entityArrayList;

        } catch (RepositoryException | ORMException | EntityFactoryException error) {
            Logger.errorLog("Repository Exception", error.getMessage());
            throw new RepositoryException("Repository Exception: " + error.getMessage());
        }
    }

    /**
     * Obtiene las entidades que coinciden con la fila y el identificador proporcionado.
     *
     * @param field fila por la cual se quiere buscar
     * @param arg valor de la fila
     * @return lista de entidades que coinciden con {@code id}; puede contener múltiples entradas o estar vacía
     * @throws RepositoryException si ocurre un error al acceder al repositorio
     */
    public ArrayList<T> getByField(String field, String arg) {
        try {

            if(arg == null){
                throw new RepositoryException("Invalid values provided");
            }

            // TODO: Implementar filtro por campo y valor

            return null;

        } catch (RepositoryException error) {
            Logger.errorLog("Repository Exception", error.getMessage());
            throw new RepositoryException("Repository Exception: " + error.getMessage());
        }
    }


    /**
     * Obtiene un registro (o registros) de la entidad por su id.
     *
     * @param id Identificador del registro a buscar (como cadena).
     * @return Lista con el/los registros encontrados.
     * @throws RepositoryException Si los valores son inválidos o ocurre un error en la operación.
     */
    @Override
    public T getById(String id) throws RepositoryException
    {
        try {
            
            if(id == null){
                throw new RepositoryException("Invalid values provided");
            }

            ArrayList<T> entityArrayList = orm.selectQuery(EntityFactory.createEntity(entityType),id, null);
            
            return entityArrayList.get(0);

        } catch (RepositoryException | EntityFactoryException | ORMException error) {
            Logger.errorLog("Repository Exception", error.getMessage());
            throw new RepositoryException("Repository Exception: " + error.getMessage());
        }
    }


    /**
     * Guarda (inserta) una entidad en la base de datos.
     *
     * @param entity Instancia de la entidad a guardar.
     * @return {@code true} si la operación reportó éxito, {@code false} en caso contrario.
     * @throws RepositoryException Si los valores son inválidos o la operación falla.
     */    @Override
    public boolean save(T entity) throws RepositoryException
    {
        try {

            if(entity == null){
                throw new RepositoryException("Invalid values provided");
            }

            int result = orm.updateQuery(entity, null);

            if(result == 1){
                Logger.infoLog(GREEN + "Success", "The Entity Was Saved Successfully");
            }

            return result == 1 ? true : false;

        } catch (RepositoryException | ORMException error) {
            Logger.errorLog("RepositoryException", "Repository operation failed: " + error.getMessage());
            throw new RepositoryException("Repository error occurred: " + error.getMessage());
        }
    }


    /**
     * Actualiza un registro existente identificado por {@code id}.
     *
     * @param entity Entidad con los nuevos valores.
     * @param id     Identificador del registro a actualizar.
     * @return {@code true} si la actualización fue exitosa, {@code false} en caso contrario.
     * @throws RepositoryException Si los valores son inválidos o la operación falla.
     */    @Override
    public boolean update(T entity, String id) throws RepositoryException
    {
        try {

            if(id == null || entity == null){
                throw new RepositoryException("Invalid values provided");
            }

            int result = orm.updateQuery(entity, id);

            if(result == 1){
                Logger.infoLog(GREEN + "Success", "The Entity Was Updated Successfully");
            }

            return result == 1 ? true : false;

        } catch (RepositoryException | ORMException error) {
            Logger.errorLog("Repository Exception", error.getMessage());
            throw new RepositoryException("Repository Exception: " + error.getMessage());
        }
    }


    /**
     * Elimina un registro de la entidad por su identificador.
     *
     * @param id Identificador del registro a eliminar.
     * @return {@code true} si la eliminación fue exitosa, {@code false} en caso contrario.
     * @throws RepositoryException Si los valores son inválidos o la operación falla.
     */    @Override
    public boolean delete(String id) throws RepositoryException
    {
        try {

            if(id == null){
                throw new RepositoryException("Invalid values provided");
            }

            int result = orm.deleteQuery(EntityFactory.createEntity(entityType), id);

            if(result == 1){
                Logger.infoLog(GREEN + "Success", "The Entity Was Deleted Successfully");
            }

            return result == 1 ? true : false;

        } catch (RepositoryException | ORMException | EntityFactoryException error) {
            Logger.errorLog("Repository Exception", error.getMessage());
            throw new RepositoryException("Repository Exception: " + error.getMessage());
        }
    }

}
