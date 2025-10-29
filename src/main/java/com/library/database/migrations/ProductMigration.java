package com.library.database.migrations;

import java.util.ArrayList;
import java.util.List;
import com.library.kernel.kernelSql.migrator.Migration;
import com.library.kernel.kernelSql.migrator.core.ddl.table.Schema;
import com.library.kernel.kernelSql.migrator.core.ddl.utils.SQLDataTypes;

public class ProductMigration extends Migration {

	@Override
	public ArrayList<Schema> up() {
		return new ArrayList<Schema>(List.of(
                new Schema("productsA")
                        .addColumn("book_id", SQLDataTypes.SERIAL)
		
		));
	}

	@Override
	public void rollback() {
		
	}
}

