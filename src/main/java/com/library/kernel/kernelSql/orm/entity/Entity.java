package com.library.kernel.kernelSql.orm.entity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Interfaz {@code Entity}.
 *
 * Representa una tabla de la base de datos como un objeto de dominio (POO).
 * Cada implementación debe ofrecer la metadata mínima necesaria para que el
 * motor ORM construya consultas y gestione relaciones entre entidades.
 *
 * Responsabilidades:
 * - Proveer el nombre de la tabla y de la clave primaria.
 * - Indicar si la clave primaria es autoincrementable.
 * - Exponer las relaciones con otras entidades y permitir asignar las entidades relacionadas.
 */
public abstract interface Entity {

    /**
     * Obtiene el nombre de la columna que actúa como clave primaria de la entidad.
     *
     * @return Nombre de la primary key (por ejemplo, "id").
     */    public abstract String getPrimaryKey();

    /**
     * Obtiene el nombre de la tabla asociada a esta entidad.
     *
     * @return Nombre de la tabla en la base de datos.
     */
    public abstract String getTableName();
    /**
     * Indica si la clave primaria es autoincrementable en la base de datos.
     *
     * @return {@code true} si la primary key es autoincrement, {@code false} en caso contrario.
     */
    public abstract boolean getAutoIncrement();

    /**
     * Devuelve las relaciones que tiene esta entidad con otras entidades.
     *
     * El mapa tiene como clave la clase de la entidad relacionada y como valor
     * el nombre del campo que une ambas entidades (por ejemplo, "user_id").
     *
     * @return Mapa de relaciones o {@code null} si no hay relaciones.
     */
    public abstract HashMap<Class<?>, String> getRelationships();

    /**
     * Asigna las instancias de las entidades relacionadas a la entidad principal.
     *
     * Este método se usa para poblar las relaciones después de mapear un ResultSet.
     *
     * @param entities Lista de entidades relacionadas que corresponden a esta entidad.
     */
    public abstract  void setEntityRelationships(ArrayList<Entity> entities);

}
