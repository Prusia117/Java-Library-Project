package com.library.kernel.jartisan.src.jmigrations;

import com.library.kernel.jartisan.src.jmigrations.utils.JMigratorReaderException;
import com.library.kernel.logger.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;
/**
 * Lector de clases de migración desde el árbol de fuentes del proyecto.
 *
 * <p>Esta clase busca archivos \".java\" dentro del directorio de migraciones
 * definido en {@link #MIGRATIONS_PATH} y devuelve una lista con los nombres de
 * las clases (sin la extensión \".java\").</p>
 */
final class JMigratorReader {
    /**
     * Ruta relativa donde se encuentran las clases de migración dentro del proyecto.
     * Debe apuntar al paquete \`com.library.database.migrations\` en la estructura de fuentes.
     */
    private static final String MIGRATIONS_PATH = "src/main/java/com/library/database/migrations/";
    /**
     * Lee los archivos de migración del directorio configurado y devuelve sus nombres de clase.
     *
     * <p>Se utiliza un \`Stream\` sobre las entradas del directorio. Solo se obtienen los
     * nombres de fichero y se elimina la extensión \".java\".</p>
     *
     * @return lista con los nombres de las clases de migración (sin \".java\")
     * @throws JMigratorReaderException si ocurre un error de E/S al acceder al directorio
     */
    public static ArrayList<String>  readMigrations() throws JMigratorReaderException {
        Path path = Paths.get(MIGRATIONS_PATH);
        try(
                Stream<Path> stream = Files.list(path)
        ) {

            ArrayList<String> migrationClasses = new ArrayList<String>();

            // Iterar sobre las entradas del directorio y extraer el nombre de clase
            stream.forEach(migrationClass -> {
                migrationClasses.add(
                        migrationClass.getFileName().toString().replace(".java", "")
                );
            });

            return migrationClasses;

        }catch(IOException error){
            Logger.errorLog("Jartisan Migrator Reader Exception", error.getMessage());
            throw new JMigratorReaderException("Jartisan Migrator Reader Exception: " + error.getMessage());
        }
    }
}
