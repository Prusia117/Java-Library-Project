package com.library.kernel.kernelSql.orm.repository;

import com.library.kernel.kernelSql.orm.entity.Entity;
import com.library.kernel.kernelSql.orm.repository.utils.RepositoryException;

import java.util.List;

public abstract interface IRepository<T extends Entity> {

    public abstract List<T> getAll() throws RepositoryException;

    public abstract List<T> getById(String id) throws RepositoryException;

    public abstract boolean save(T entity) throws RepositoryException;

    public abstract boolean update(T entity, String id) throws RepositoryException;

    public abstract boolean delete(String id) throws RepositoryException;
}
