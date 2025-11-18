package com.library.database.migrations;

import java.util.ArrayList;
import java.util.List;
import com.library.kernel.kernelSql.migrator.Migration;
import com.library.kernel.kernelSql.migrator.core.ddl.table.Schema;
import com.library.kernel.kernelSql.migrator.core.ddl.utils.SQLDataTypes;

public class M20251111_141232335894400_UsersMigration extends Migration {

    @Override
    public ArrayList<Schema> up() {
        return new ArrayList<>(List.of(
                new Schema("T_Users")
                        .addColumn("user_id", SQLDataTypes.SERIAL)
                        .primaryKey()
                        .addColumn("user_name", SQLDataTypes.VARCHAR)
                        .addColumn("user_mail", SQLDataTypes.VARCHAR)
                        .addColumn("user_password", SQLDataTypes.VARCHAR)
        ));
    }

    @Override
    public ArrayList<String> rollback() {
        return new ArrayList<>(List.of("T_USERS"));
    }
}
