package com.library.kernel.kernelSql.migrator.core.ddl.table;

import com.library.kernel.kernelSql.migrator.core.ddl.utils.SQLDataTypes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SchemaTest {

    @Test
    void buildsCreateTableWithColumnsAndPrimaryKey() {
        String sql = new Schema("T_Users")
                .addColumn("user_id", SQLDataTypes.SERIAL).primaryKey()
                .addColumn("user_name", SQLDataTypes.VARCHAR).size(80)
                .addColumn("user_mail", SQLDataTypes.VARCHAR).nullable(true)
                .toString();

        assertEquals(
                "CREATE TABLE T_Users ("
                        + "user_id SERIAL NOT NULL, "
                        + "user_name VARCHAR(80) NOT NULL, "
                        + "user_mail VARCHAR NULL, "
                        + "CONSTRAINT t_users_user_id___pk PRIMARY KEY (user_id));",
                sql);
    }

    @Test
    void buildsForeignAndUniqueKeyConstraints() {
        String sql = new Schema("T_Books")
                .addColumn("book_id", SQLDataTypes.SERIAL).primaryKey()
                .addColumn("book_isbn", SQLDataTypes.VARCHAR).uniqueKey()
                .addColumn("user_id", SQLDataTypes.INT).foreignKey("T_Users", "user_id")
                .toString();

        assertEquals(
                "CREATE TABLE T_Books ("
                        + "book_id SERIAL NOT NULL, "
                        + "book_isbn VARCHAR NOT NULL, "
                        + "user_id INT NOT NULL, "
                        + "CONSTRAINT t_books_book_id___pk PRIMARY KEY (book_id), "
                        + "CONSTRAINT t_books_book_isbn___uk UNIQUE (book_isbn), "
                        + "CONSTRAINT t_books_user_id___fk FOREIGN KEY (user_id) REFERENCES T_Users(user_id));",
                sql);
    }
}
