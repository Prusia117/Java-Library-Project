package com.library.examples;


import com.library.kernel.classes.Colors;

public class StringVsStringBuilder implements Colors {
    public static void main(String[] args) {

        System.out.println(BLUE + "=== String Example ===" + RESET);

        // String es inmutable: cada vez que lo modificamos se crea un nuevo objeto
        String str = "Hola";
        System.out.println(RED + "Original String: " + RESET + str);
        System.out.println(BLUE + "Memory direction: " + RESET + str.hashCode());

        // Concatenación con +
        str = str + " Mundo";
        System.out.println(GREEN + "After concatenation: " + RESET + str);
        System.out.println(BLUE + "Memory direction: " + RESET + str.hashCode());

        // Reemplazo de caracteres
        str = str.replace("Mundo", "Javac");
        System.out.println(GREEN + "After replace: " + RESET + str);
        System.out.println(BLUE + "Memory direction: " + RESET + str.hashCode());

        // Longitud del String
        System.out.println(YELLOW + "Length: " + RESET + str.length());

        System.out.println(BLUE + "\n=== StringBuilder Example ===" + RESET);

        // StringBuilder es mutable: podemos modificar el mismo objeto
        StringBuilder sb = new StringBuilder("Hola");
        System.out.println(RED + "Original StringBuilder: " + RESET + sb);
        System.out.println(BLUE + "Memory direction: " + RESET + sb.hashCode());

        // Append agrega al final
        sb.append(" Mundo");
        System.out.println(GREEN + "After append: " + RESET + sb);
        System.out.println(BLUE + "Memory direction: " + RESET + sb.hashCode());

        // Insertar en posición específica
        sb.insert(5, "Java ");
        System.out.println(GREEN + "After insert: " + RESET + sb);
        System.out.println(BLUE + "Memory direction: " + RESET + sb.hashCode());

        // Reemplazar una sección
        sb.replace(5, 9, "Python");
        System.out.println(GREEN + "After replace: " + RESET + sb);
        System.out.println(BLUE + "Memory direction: " + RESET + sb.hashCode());

        // Eliminar caracteres
        sb.delete(5, 11);
        System.out.println(GREEN + "After delete: " + RESET + sb);
        System.out.println(BLUE + "Memory direction: " + RESET + sb.hashCode());

        // Longitud y capacidad
        System.out.println(YELLOW + "Length: " + RESET + sb.length());
        System.out.println(YELLOW + "Capacity: " + RESET + sb.capacity());
    }
}