package com.library.kernel.orm;

import java.util.ArrayList;
import java.util.List;

import com.library.kernel.classes.Colors;
/*
 * Este archivo implementa el patrón Repository
 * ¿Qué significa eso? Que aquí se maneja el acceso a los datos (como si fuera el mesero del sistema)
 * La lógica de negocio no se mete con la base de datos directamente, sino que le pide al repositorio que lo haga
 * Ventajas: el código es más limpio, fácil de mantener, y se puede cambiar la fuente de datos sin romper todo
 */
public final class Repository<T extends Entity> implements Colors
{
    // ORM consulta la clase para mas información
    private ORM<T> orm;

    // Esta variable alamcenara el tipo de Entity el cual el repository utilizara
    private Class<T> entityType;

    public Repository(Class<T> type) throws ORMException
    {
        orm = new ORM<T>();

        this.entityType = type;
    }

    // Método que obtiene todos los regristros de la entity
    public List<T> getAll()
    {
        try {

            ArrayList<T> entityArrayList = orm.selectQuery(EntityFactory.createEntity(entityType),null);
            
            return entityArrayList;

        } catch (Exception e) {
            System.out.println("[" + RED + "ERROR" + RESET + "]: " + RED + "Repository Error: " + e);
            return null;
        }
    }
    
    
    // Método que obtiene un registro de la entity por medio de su id
    public List<T> getById(String id) throws ORMException
    {
        try {
            
            if(id == null){
                throw new ORMException("Invalid values provided");
            }

            ArrayList<T> entityArrayList = orm.selectQuery(EntityFactory.createEntity(entityType),id);
            
            return entityArrayList;

        } catch (Exception e) {
            System.out.println("[" + RED + "ERROR" + RESET + "]: " + RED + "Repository Error: " + e);
            return null;
        }
    }
    
    // Método que guarda un registro de la entity
    public boolean save(T entity) throws ORMException
    {
        try {

            if(entity == null){
                throw new ORMException("Invalid values provided");
            }

            int result = orm.updateQuery(entity, null);
            
            return result == 1 ? true : false;

        } catch (Exception e) {
            System.out.println("[" + RED + "ERROR" + RESET + "]: " + RED + "Repository Error: " + e);
            return false;
        }
    }
    
    // Método que actualiza un registro de la entity
    public boolean update(T entity, String id) throws ORMException
    {
        try {

            if(id == null || entity == null){
                throw new ORMException("Invalid values provided");
            }

            int result = orm.updateQuery(entity, id);
            
            return result == 1 ? true : false;

        } catch (Exception e) {
            System.out.println("[" + RED + "ERROR" + RESET + "]: " + RED + "Repository Error: " + e);
            return false;
        }
    }

    // Método que elimina un registro de la entity
    public boolean delete(String id) throws ORMException
    {
        try {

            if(id == null){
                throw new ORMException("Invalid values provided");
            }

            int result = orm.deleteQuery(EntityFactory.createEntity(entityType), id);
            
            return result == 1 ? true : false;

        } catch (Exception e) {
            System.out.println("[" + RED + "ERROR" + RESET + "]: " + RED + "Repository Error: " + e);
            return false;
        }
    }

}
