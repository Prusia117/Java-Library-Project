package com.library.kernel.kernelSql.orm.core;

import com.library.entities.UserEntity;
import com.library.kernel.kernelSql.orm.core.utils.SQLBuilderException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SQLBuilderTest {

    private final SQLBuilder builder = new SQLBuilder();

    @Test
    void selectAllOrdersByPrimaryKey() {
        SQLStatement stmt = builder.createSelectQuery(new UserEntity(), null, null);

        assertEquals("SELECT user_id, user_name, user_mail, user_password FROM T_Users ORDER BY user_id ASC;", stmt.sql());
        assertEquals(List.of(), stmt.params());
    }

    @Test
    void selectByFieldUsesPlaceholder() {
        SQLStatement stmt = builder.createSelectQuery(new UserEntity(), "ana@mail.com", "user_mail");

        assertEquals(
                "SELECT user_id, user_name, user_mail, user_password FROM T_Users"
                        + " WHERE user_mail = ? ORDER BY user_id ASC;",
                stmt.sql());
        assertEquals(List.of("ana@mail.com"), stmt.params());
    }

    @Test
    void selectByIdConvertsValueToFieldType() {
        SQLStatement stmt = builder.createSelectQuery(new UserEntity(), "7", null);

        assertEquals(List.of(7), stmt.params());
    }

    @Test
    void injectionInValueStaysAsParameter() {
        String attack = "' OR '1'='1";
        SQLStatement stmt = builder.createSelectQuery(new UserEntity(), attack, "user_mail");

        assertFalse(stmt.sql().contains(attack));
        assertEquals(List.of(attack), stmt.params());
    }

    @Test
    void rejectsUnknownColumnName() {
        assertThrows(SQLBuilderException.class,
                () -> builder.createSelectQuery(new UserEntity(), "x", "user_mail = '' OR 1=1 --"));
    }

    @Test
    void rejectsNonNumericId() {
        assertThrows(SQLBuilderException.class,
                () -> builder.createDeleteQuery(new UserEntity(), "1 OR 1=1"));
    }

    @Test
    void insertSkipsAutoIncrementPrimaryKeyAndNulls() {
        UserEntity user = new UserEntity();
        user.user_name = "Ana";
        user.user_mail = "ana@mail.com";

        SQLStatement stmt = builder.createInsertQuery(user);

        assertEquals("INSERT INTO T_Users (user_name, user_mail) VALUES (?, ?);", stmt.sql());
        assertEquals(List.of("Ana", "ana@mail.com"), stmt.params());
    }

    @Test
    void updateOnlySetsNonNullFields() {
        UserEntity user = new UserEntity();
        user.user_name = "O'Brien";

        SQLStatement stmt = builder.createUpdateQuery(user, "7");

        assertEquals("UPDATE T_Users SET user_name = ? WHERE user_id = ?;", stmt.sql());
        assertEquals(List.of("O'Brien", 7), stmt.params());
    }

    @Test
    void deleteByPrimaryKey() {
        SQLStatement stmt = builder.createDeleteQuery(new UserEntity(), "7");

        assertEquals("DELETE FROM T_Users WHERE user_id = ?;", stmt.sql());
        assertEquals(List.of(7), stmt.params());
    }

    @Test
    void rejectsNullEntity() {
        assertThrows(SQLBuilderException.class, () -> builder.createSelectQuery(null, null, null));
    }
}
