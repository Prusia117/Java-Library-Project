package com.library.kernel.kernelSql.orm.core;

import com.library.kernel.kernelSql.orm.core.utils.ORMException;
import com.library.kernel.kernelSql.orm.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public abstract interface IORMEngine<T extends Entity> {

    public abstract List<T> selectQuery(T entity, String value, String field) throws ORMException;

    public abstract int updateQuery(T entity, String id) throws ORMException;

    public abstract int deleteQuery(T entity, String id) throws ORMException;

}
