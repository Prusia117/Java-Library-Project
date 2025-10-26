package com.library.kernel.logger;

import com.library.kernel.classes.Colors;

public final class Logger implements Colors
{

    public static void infoLog(String tittle, String message){
        System.out.println("[" + BLUE + "INFO" + RESET + "]: " + CIAN + tittle + ": " + BLUE + message + RESET);
    }

    public static void errorLog(String tittle, String message){
        System.out.println("[" + RED + "ERROR" + RESET + "]: " + RED + tittle + ": " + message + RESET);
    }

    public static void warningLog(String tittle, String message){
        System.out.println("[" + YELLOW + "WARNING" + RESET + "]: " + YELLOW + tittle + ": " + message + RESET);
    }
}
