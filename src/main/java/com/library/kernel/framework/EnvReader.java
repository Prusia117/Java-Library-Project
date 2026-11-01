package com.library.kernel.framework;

import com.library.kernel.logger.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Paths;

public final class EnvReader {

    private static final String envPath = Paths.get(System.getProperty("user.dir"), ".env").toString();

    public static String getEnv(String key){
        try(
                BufferedReader bufferedReader = new BufferedReader(
                        new FileReader(envPath)
                );
                )
        {
            String line;
            while((line = bufferedReader.readLine()) != null){
                String value = parseLine(line, key);
                if(value != null){
                    return value;
                }
            }

        }catch (Exception e){
            Logger.errorLog("EnvReader Exception", e.getMessage());
        }
        return System.getenv(key);
    }

    /**
     * Devuelve el valor de la línea si su clave coincide exactamente con {@code key}.
     * Ignora líneas vacías y comentarios, conserva los '=' dentro del valor
     * y quita comillas envolventes.
     */
    static String parseLine(String line, String key){
        String trimmed = line.trim();
        if(trimmed.isEmpty() || trimmed.startsWith("#")){
            return null;
        }

        int separator = trimmed.indexOf('=');
        if(separator == -1 || !trimmed.substring(0, separator).trim().equals(key)){
            return null;
        }

        String value = trimmed.substring(separator + 1).trim();
        if(value.length() >= 2
                && (value.startsWith("\"") && value.endsWith("\"")
                || value.startsWith("'") && value.endsWith("'"))){
            value = value.substring(1, value.length() - 1);
        }
        return value;
    }

}
