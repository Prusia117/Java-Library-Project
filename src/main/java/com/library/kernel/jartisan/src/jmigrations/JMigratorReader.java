package com.library.kernel.jartisan.src.jmigrations;

import com.library.kernel.jartisan.src.jmigrations.utils.JMigratorReaderException;
import com.library.kernel.logger.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public final class JMigratorReader {

    private static final String MIGRATIONS_PATH = "src/main/java/com/library/database/migrations/";

    public static ArrayList<String>  readMigrations() throws JMigratorReaderException {
        Path path = Paths.get(MIGRATIONS_PATH);
        try(
                Stream<Path> stream = Files.list(path)
        ) {

            ArrayList<String> migrationClasses = new ArrayList<String>();

            stream.forEach(migrationClass -> {
                migrationClasses.add(
                        migrationClass.getFileName().toString().replace(".java", "")
                );
            });

            return migrationClasses;

        }catch(IOException error){
            Logger.errorLog("Error Reading Migrations", error.getMessage());
            throw new JMigratorReaderException(error.getMessage());
        }
    }
}
