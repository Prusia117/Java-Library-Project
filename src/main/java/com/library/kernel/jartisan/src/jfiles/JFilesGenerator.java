package com.library.kernel.jartisan.src.jfiles;

import com.library.kernel.classes.Colors;
import com.library.kernel.jartisan.src.jfiles.jtemplates.*;
import com.library.kernel.logger.Logger;

import java.io.File;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
/**
 * Generador de archivos para Jartisan.
 *
 * <p>Proporciona utilidades para crear nuevas clases de tipo Entity, IModel,
 * Controller o Migration a partir de plantillas. Se encarga de construir la
 * ruta destino, generar el nombre para migraciones y escribir el contenido
 * en el sistema de ficheros.</p>
 */
public final class JFilesGenerator implements Colors {
    /**
     * Tipos de fichero soportados por el generador.
     * Valores permitidos: "Entity", "IModel", "Controller", "Migration".
     */
    public static final List<String> J_SUPPORTED_FILE_TYPES = Arrays.asList("Entity", "Model", "Controller", "Migration");
    /**
     * Mapa que asocia cada tipo con el subdirectorio relativo dentro de
     * `src/main/java/com/library/`. Las rutas terminan en barra para facilitar
     * la concatenación.
     */
    private static final HashMap<String, String> DIRECTORIES = new HashMap<String, String>() {{
        put("Entity", "entities/");
        put("Model", "models/");
        put("Controller", "controllers/");
        put("Migration", "database/migrations/");
    }};
    /**
     * Mapa de plantillas que devuelve el contenido a escribir según el tipo.
     * Cada entrada implementa la interfaz {@code IJTemplate} responsable de
     * generar el texto fuente de la clase.
     */
    private static final HashMap<String, IJTemplate> CLASSES = new HashMap<String, IJTemplate>() {{
        put("Entity", new JEntityTemplate());
        put("Model", new JModelTemplate());
        put("Controller", new JControllerTemplate());
        put("Migration", new JMigrationTemplate());
    }};
    /**
     * Genera un nuevo archivo de código según el tipo y nombre de clase.
     *
     * <p>Si {@code type} es "Migration", se antepone un prefijo con la fecha y
     * hora para garantizar unicidad (formato: MYYYYMMDD_HHMMSS\_Nombre).</p>
     *
     * @param type      tipo de archivo a generar ("Entity","IModel","Controller","Migration")
     * @param className nombre base de la clase (sin sufijo de tipo)
     */
    public static void generateNewFile(String type, String className){
        // Habilitar modo logger para mostrar la actividad del generador
        Logger.setLoggerMode(true);
        // Si es una migración, construir nombre único con prefijo temporal
        if(type.equals("Migration")){
            className = "M" + LocalDate.now().toString().replace("-", "") + "_" + LocalTime.now().toString().replace(":","").replace(".","")  + "_" + className;
        }
        // Construir la ruta completa del fichero destino
        String filePath = "src/main/java/com/library/" + DIRECTORIES.get(type) + className + type +".java";
        // Obtener el template correspondiente y crear el fichero
        String fileTemplate = CLASSES.get(type).getTemplate(className);
        boolean result = newFile(filePath, fileTemplate);
        // Informar el resultado mediante el logger
        if(result == true){
            Logger.infoLog("Jartisan Files Generator", type + GREEN + " created successfully");
        }else{
            Logger.errorLog("Jartisan Files Generator", type + RED + " could not be created");
        }
    }
    /**
     * Crea el fichero en disco y escribe el contenido proporcionado.
     *
     * <p>Utiliza {@code PrintWriter} para volcar el texto. Devuelve {@code true}
     * si el fichero existe al finalizar, o {@code false} en caso de error.</p>
     *
     * @param path    ruta completa del fichero a crear
     * @param content contenido que se escribirá en el fichero
     * @return {@code true} si el fichero fue creado y existe, {@code false} si ocurrió un error
     */
    private static boolean newFile(String path, String content){
        try {
            File file = new File(path);
            // Crear el fichero físico (y los directorios padres si fueran necesarios)
            file.createNewFile();
            PrintWriter output = new PrintWriter(file);
            output.println(content);
            output.close();
            return file.exists();
        } catch (Exception e) {
            Logger.errorLog("Jartisan Files Generator Exception", e.getMessage());
            return false;
        }
    }
}
