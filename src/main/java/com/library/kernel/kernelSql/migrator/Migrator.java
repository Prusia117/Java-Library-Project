package com.library.kernel.kernelSql.migrator;

import com.library.kernel.kernelSql.migrator.core.ddl.table.Schema;

import java.util.ArrayList;

public abstract class Migrator {

    protected void ge

    public abstract ArrayList<Schema> up();
    public abstract void rollback();
}
