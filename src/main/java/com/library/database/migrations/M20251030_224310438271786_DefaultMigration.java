package com.library.database.migrations;

import java.util.ArrayList;
import java.util.List;
import com.library.kernel.kernelSql.migrator.Migration;
import com.library.kernel.kernelSql.migrator.core.ddl.table.Schema;
import com.library.kernel.kernelSql.migrator.core.ddl.utils.SQLDataTypes;

public class M20251030_224310438271786_DefaultMigration extends Migration {

    @Override
    public ArrayList<Schema> up() {
        return new ArrayList<Schema>(List.of(
                new Schema("T_MIGRATIONS")
                        .addColumn("mig_id", SQLDataTypes.SERIAL)
                        .nullable(false)
                        .primaryKey()
                        .addColumn("mig_name", SQLDataTypes.VARCHAR)
                        .size(50)
                        .nullable(false)

        ));
    }

    @Override
    public ArrayList<String> rollback() {
        return new ArrayList<String>(List.of(
                "T_MIGRATIONS"
        ));
    }
}
