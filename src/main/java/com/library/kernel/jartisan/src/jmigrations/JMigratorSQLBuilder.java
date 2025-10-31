package com.library.kernel.jartisan.src.jmigrations;

final class JMigratorSQLBuilder {
    private static final String MIGRATIONS_TABLE = "T_MIGRATIONS";
    private static final String MIGRATIONS_FIELD_NAME = "mig_name";

    public static String getMigrationsFieldName(){
        return MIGRATIONS_FIELD_NAME;
    }

    public static String buildDropMigrationTableQuery(String migrationName){
        return "DROP TABLE IF EXISTS " + migrationName + ";";
    }

    public static String buildGetMigrationTableQuery(){
        return "SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = '"
                + MIGRATIONS_TABLE.toLowerCase() + "');";
    }

    public static String buildGetMigrationsQuery(){
        return "SELECT * FROM " + MIGRATIONS_TABLE + ";";
    }

    public static String buildUpdateMigrationTableQuery(String migrationName){
        return "INSERT INTO " + MIGRATIONS_TABLE + " (" + MIGRATIONS_FIELD_NAME + ") VALUES ('" + migrationName + "');";
    }

    public static String buildDeleteMigrationRecordQuery(String migrationName){
        return "DELETE FROM " + MIGRATIONS_TABLE + " WHERE " + MIGRATIONS_FIELD_NAME + " = '" + migrationName + "';";
    }

}