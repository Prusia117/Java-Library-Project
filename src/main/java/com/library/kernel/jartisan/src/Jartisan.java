package com.library.kernel.jartisan.src;

import com.library.kernel.classes.Colors;
import com.library.kernel.jartisan.src.jfiles.JFilesGenerator;
import com.library.kernel.jartisan.src.jmigrations.JMigrator;

import static com.library.kernel.jartisan.src.jfiles.JFilesGenerator.J_SUPPORTED_FILE_TYPES;
import static com.library.kernel.jartisan.src.jmigrations.JMigrator.J_SUPPORTED_MIGRATIONS_ACTIONS;

public final class Jartisan implements Colors {

    public static void main(String[] args) {

        try {

            if(args[0].equals("Make")){
                if(J_SUPPORTED_FILE_TYPES.contains(args[1])){
                    JFilesGenerator.generateNewFile(args[1], args[2]);
                }
            }
            else if(args[0].equals("Migrations")){
                if(J_SUPPORTED_MIGRATIONS_ACTIONS.contains(args[1])){
                    JMigrator.runMigrationsCommand(args[1]);
                }
            }

        } catch (Exception e) {
            System.out.println("[" + RED + "ERROR" + RESET + "]: " + RED + "Jartisan Exception: " + e + RESET);
        }
    }
}
