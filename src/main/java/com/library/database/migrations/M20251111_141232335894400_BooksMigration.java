package com.library.database.migrations;

import java.util.ArrayList;
import java.util.List;
import com.library.kernel.kernelSql.migrator.Migration;
import com.library.kernel.kernelSql.migrator.core.ddl.table.Schema;
import com.library.kernel.kernelSql.migrator.core.ddl.utils.SQLDataTypes;

public class M20251111_141232335894400_BooksMigration extends Migration {

    @Override
    public ArrayList<Schema> up() {
        return new ArrayList<>(List.of(
                new Schema("T_Books")
                        .addColumn("book_id", SQLDataTypes.SERIAL).primaryKey()
                        .addColumn("book_title", SQLDataTypes.VARCHAR)
                        .addColumn("book_author", SQLDataTypes.VARCHAR)
                        .addColumn("book_year", SQLDataTypes.INT)
                        .addColumn("book_category", SQLDataTypes.VARCHAR)
                        //Agregación de los paths
                        .addColumn("book_file_path", SQLDataTypes.VARCHAR)
                        .addColumn("book_image_path", SQLDataTypes.VARCHAR)
        ));
    }

    @Override
    public ArrayList<String> rollback() {
        return new ArrayList<>(List.of("T_BOOKS"));
    }
}
