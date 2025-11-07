package com.library.kernel.framework;

import com.library.kernel.logger.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
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
                if(line.startsWith(key)){
                    return line.split("=")[1];
                }
            }

        }catch (Exception e){
            Logger.errorLog("EnvReader Exception", e.getMessage());
        }
        return System.getenv(key);
    }

}
