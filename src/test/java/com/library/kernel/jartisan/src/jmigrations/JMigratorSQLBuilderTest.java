package com.library.kernel.jartisan.src.jmigrations;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JMigratorSQLBuilderTest {

    @Test
    void checksMigrationsTableExistence() {
        assertEquals(
                "SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 't_migrations');",
                JMigratorSQLBuilder.buildGetMigrationTableQuery());
    }

    @Test
    void recordsAndDeletesExecutedMigrations() {
        String name = "M20251111_141232335894400_UsersMigration";

        assertEquals(
                "INSERT INTO T_MIGRATIONS (mig_name) VALUES ('" + name + "');",
                JMigratorSQLBuilder.buildUpdateMigrationTableQuery(name));
        assertEquals(
                "DELETE FROM T_MIGRATIONS WHERE mig_name = '" + name + "';",
                JMigratorSQLBuilder.buildDeleteMigrationRecordQuery(name));
    }

    @Test
    void dropsTableOnRollback() {
        assertEquals("DROP TABLE IF EXISTS T_USERS;", JMigratorSQLBuilder.buildDropMigrationTableQuery("T_USERS"));
    }
}
