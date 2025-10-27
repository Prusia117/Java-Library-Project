package com.library.kernel.migrator;

import com.library.kernel.migrator.core.ddl.table.Schema;

import java.util.ArrayList;

public abstract interface Migrator {

    public abstract ArrayList<Schema> up();
    public abstract void rollback();
}
