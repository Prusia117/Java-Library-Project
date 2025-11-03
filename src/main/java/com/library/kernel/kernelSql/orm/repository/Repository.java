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

/*
 * Este archivo implementa el patrón Repository
 * ¿Qué significa eso? Que aquí se maneja el acceso a los datos (como si fuera el mesero del sistema)
 * La lógica de negocio no se mete con la base de datos directamente, sino que le pide al repositorio que lo haga
 * Ventajas: el código es más limpio, fácil de mantener, y se puede cambiar la fuente de datos sin romper todo
 */
public final class Repository<T extends Entity> implements IRepository<T>, Colors
{
    // ORM consulta la clase para mas información
    private ORM<T> orm;

    // Esta variable alamcenara el tipo de Entity el cual el repository utilizara
    private Class<T> entityType;

    public Repository(Class<T> type)
    {
        orm = new ORM<T>();

        this.entityType = type;
    }

    // Método que obtiene todos los regristros de la entity
    @Override
    public List<T> getAll() throws RepositoryException
    {
        try {

            ArrayList<T> entityArrayList = orm.selectQuery(EntityFactory.createEntity(entityType),null, null);
            
            return entityArrayList;

        } catch (RepositoryException | ORMException | EntityFactoryException error) {
            Logger.errorLog("Repository Exception", error.getMessage());
            throw new RepositoryException("Repository Exception: " + error.getMessage());
        }
    }
    
    
    // Método que obtiene un registro de la entity por medio de su id
    @Override
    public List<T> getById(String id) throws RepositoryException
    {
        try {
            
            if(id == null){
                throw new RepositoryException("Invalid values provided");
            }

            ArrayList<T> entityArrayList = orm.selectQuery(EntityFactory.createEntity(entityType),id, null);
            
            return entityArrayList;

        } catch (RepositoryException | EntityFactoryException | ORMException error) {
            Logger.errorLog("Repository Exception", error.getMessage());
            throw new RepositoryException("Repository Exception: " + error.getMessage());
        }
    }
    
    // Método que guarda un registro de la entity
    @Override
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
    
    // Método que actualiza un registro de la entity
    @Override
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

    // Método que elimina un registro de la entity
    @Override
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
