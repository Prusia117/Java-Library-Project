package com.library.database.migrations;

import java.util.ArrayList;
import java.util.List;
import com.library.kernel.kernelSql.migrator.Migration;
import com.library.kernel.kernelSql.migrator.core.ddl.table.Schema;
import com.library.kernel.kernelSql.migrator.core.ddl.utils.SQLDataTypes;

public class M20251031_173842136679300_BooksAndAuthorsMigration extends Migration {

	@Override
	public ArrayList<Schema> up() {
		return new ArrayList<Schema>(List.of(
                new Schema("autores")
                        .addColumn("autor_id", SQLDataTypes.SERIAL)
                        .nullable(false)
                        .primaryKey()
                        .addColumn("nombre", SQLDataTypes.VARCHAR)
                        .size(50)
                        .nullable(false),
                new Schema("books")
                        .addColumn("book_id", SQLDataTypes.SERIAL)
                        .nullable(false)
                        .primaryKey()
                        .addColumn("tittle", SQLDataTypes.VARCHAR)
                        .size(50)
                        .nullable(false)
                        .addColumn("img", SQLDataTypes.VARCHAR)
                        .size(50)
                        .nullable(false)
                        .addColumn("bo_autor_id", SQLDataTypes.INT)
                        .nullable(false)
                        .foreignKey("autores", "autor_id")
		
		));
	}

	@Override
	public ArrayList<String> rollback() {
		return new ArrayList<String>(List.of(

                "books",
                "autores"
		
		));
	}
}

