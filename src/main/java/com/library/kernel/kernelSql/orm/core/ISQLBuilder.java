package com.library.kernel.kernelSql.orm.core;

import com.library.kernel.kernelSql.orm.core.utils.SQLBuilderException;
import com.library.kernel.kernelSql.orm.entity.Entity;

import java.util.HashMap;

abstract interface ISQLBuilder {

    public String[] createJoinsStatments(HashMap<Class<?>, String> relationships) throws SQLBuilderException;

    public  String createSelectQuery(Entity entity, String value, String field) throws SQLBuilderException;

    public String createInsertQuery(Entity entity) throws SQLBuilderException;

    public String createUpdateQuery(Entity entity, String id) throws SQLBuilderException;

    public String createDeleteQuery(Entity entity, String id) throws SQLBuilderException;

}
