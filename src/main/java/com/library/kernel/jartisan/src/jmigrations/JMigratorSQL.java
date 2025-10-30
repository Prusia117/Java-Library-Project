package com.library.kernel.jartisan.src.jmigrations;

import com.library.kernel.kernelSql.migrator.Migration;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class JMigratorSQL {


    public static<T extends Migration> boolean runMigrations(LinkedHashMap<String, T> migrationMap) {


        return true;
    }

}
