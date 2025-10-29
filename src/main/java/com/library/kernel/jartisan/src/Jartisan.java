package com.library.kernel.jartisan.src;

import com.library.kernel.jartisan.src.jfiles.JFilesGenerator;

import java.util.List;

import static com.library.kernel.jartisan.src.jfiles.JFilesGenerator.J_SUPPORTED_FILE_TYPES;

public final class Jartisan implements Colors {

    private static final String[] SUPPORTED_COMMANDS = new String[] {"Make", "Execute"};

    public static void main(String[] args) {

        try {
            boolean result = false;

            if(args[0].equals("Make")){
                if(J_SUPPORTED_FILE_TYPES.contains(args[1])){
                    result = JFilesGenerator.generateNewFile(args[1], args[2]);
                }
            }
            else if(args[0].equals("Execute")){

            }



            if (result) {
                System.out.println("[" + BLUE + "INFO" + RESET + "]: " + GREEN + "Jartisan Created " + BLUE + args[0]
                        + GREEN + " Successfully" + RESET);
            }
        } catch (Exception e) {
            System.out.println("[" + RED + "ERROR" + RESET + "]: " + RED + "Jartisan Exception: " + e + RESET);
        }
    }
}
