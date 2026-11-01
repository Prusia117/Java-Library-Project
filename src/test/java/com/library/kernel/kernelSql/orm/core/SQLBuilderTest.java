package com.library.kernel.kernelSql.orm.core;

import com.library.entities.UserEntity;
import com.library.kernel.kernelSql.orm.core.utils.SQLBuilderException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SQLBuilderTest {

    private final SQLBuilder builder = new SQLBuilder();

    @Test
    void selectAllOrdersByPrimaryKey() throws Exception {
        assertEquals(
                "SELECT user_id, user_name, user_mail, user_password FROM T_Users ORDER BY user_id ASC;",
                builder.createSelectQuery(new UserEntity(), null, null));
    }

    @Test
    void selectByFieldAddsWhereClause() throws Exception {
        assertEquals(
                "SELECT user_id, user_name, user_mail, user_password FROM T_Users"
                        + " WHERE user_mail = 'ana@mail.com' ORDER BY user_id ASC;",
                builder.createSelectQuery(new UserEntity(), "ana@mail.com", "user_mail"));
    }

    @Test
    void insertSkipsAutoIncrementPrimaryKeyAndMetadata() throws Exception {
        UserEntity user = new UserEntity();
        user.user_name = "Ana";
        user.user_mail = "ana@mail.com";
        user.user_password = "hash";

        assertEquals(
                "INSERT INTO T_Users (user_name, user_mail, user_password) VALUES ('Ana', 'ana@mail.com', 'hash');",
                builder.createInsertQuery(user));
    }

    @Test
    void updateOnlySetsNonNullFields() throws Exception {
        UserEntity user = new UserEntity();
        user.user_name = "Ana";

        assertEquals(
                "UPDATE T_Users SET user_name = 'Ana' WHERE user_id = 7;",
                builder.createUpdateQuery(user, "7"));
    }

    @Test
    void deleteByPrimaryKey() throws Exception {
        assertEquals(
                "DELETE FROM T_Users WHERE user_id = '7';",
                builder.createDeleteQuery(new UserEntity(), "7"));
    }

    @Test
    void rejectsNullEntity() {
        assertThrows(SQLBuilderException.class, () -> builder.createSelectQuery(null, null, null));
    }
}
