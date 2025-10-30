package com.library.database.migrations;

import com.library.kernel.kernelSql.migrator.Migration;
import com.library.kernel.kernelSql.migrator.core.ddl.table.Schema;
import com.library.kernel.kernelSql.migrator.core.ddl.utils.SQLDataTypes;

import java.util.ArrayList;
import java.util.List;

public class DefaultMigration extends Migration {

    @Override
    public ArrayList<Schema> up() {
        return new ArrayList<Schema>(List.of(
                new Schema("T_MIGRATIONS")
                        .addColumn("mig_id", SQLDataTypes.SERIAL)
                        .nullable(false)
                        .primaryKey()
                        .addColumn("mig_name", SQLDataTypes.VARCHAR)
                        .nullable(false)
        ));
    }

    @Override
    public void rollback() {

    }
}
