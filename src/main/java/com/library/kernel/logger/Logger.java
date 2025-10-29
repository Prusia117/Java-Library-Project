package com.library.kernel.logger;

import com.library.kernel.classes.Colors;

public final class Logger implements Colors
{
    private static boolean DEBUG_MODE = false;

    public static void setLoggerMode(boolean debugMode){
        DEBUG_MODE = debugMode;
    }

    public static void infoLog(String tittle, String message){
        if(!DEBUG_MODE) return;
        System.out.println("[" + BLUE + "INFO" + RESET + "]: " + CIAN + tittle + ": " + BLUE + message + RESET);
    }

    public static void errorLog(String tittle, String message){
        if(!DEBUG_MODE) return;
        System.out.println("[" + RED + "ERROR" + RESET + "]: " + RED + tittle + ": " + message + RESET);
    }

    public static void warningLog(String tittle, String message){
        if(!DEBUG_MODE) return;
        System.out.println("[" + YELLOW + "WARNING" + RESET + "]: " + YELLOW + tittle + ": " + message + RESET);
    }
}
