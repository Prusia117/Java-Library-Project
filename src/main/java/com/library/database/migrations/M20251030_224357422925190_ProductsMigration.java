package com.library.database.migrations;

import java.util.ArrayList;
import java.util.List;
import com.library.kernel.kernelSql.migrator.Migration;
import com.library.kernel.kernelSql.migrator.core.ddl.constraints.SQLCheckOperators;
import com.library.kernel.kernelSql.migrator.core.ddl.table.Schema;
import com.library.kernel.kernelSql.migrator.core.ddl.utils.SQLDataTypes;

public class M20251030_224357422925190_ProductsMigration extends Migration {

	@Override
    public ArrayList<Schema> up() {
        return new ArrayList<Schema>(List.of(
                new Schema("Products")
                        .addColumn("pro_id", SQLDataTypes.SERIAL)
                        .nullable(false)
                        .primaryKey()
                        .addColumn("pro_name", SQLDataTypes.VARCHAR)
                        .size(50)
                        .nullable(false)
                        .addCheck(SQLCheckOperators.LIKE, "'A'")

        ));
    }

    @Override
    public ArrayList<String> rollback() {
        return new ArrayList<String>(List.of(
                "Products"
        ));
    }
}

