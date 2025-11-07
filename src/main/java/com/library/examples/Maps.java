package com.library.examples;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.library.kernel.classes.Colors;

/*
 * Ejemplo para demostrar las diferencias entre HashMap y LinkedHashMap en Java.
 * Incluye explicación, métodos básicos y demostración de orden.
 * Referencias:
 * - HashMap: https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html
 * - LinkedHashMap: https://docs.oracle.com/javase/8/docs/api/java/util/LinkedHashMap.html
 */

public class Maps implements Colors {

    public static void main(String[] args) {

        System.out.println(BLUE + "==== HASHMAP ====" + RESET);

        /*
         * HashMap es una implementación de la interfaz Map que almacena pares clave-valor.
         * Características:
         * - No mantiene el orden de inserción.
         * - Permite un null como clave y múltiples null como valores.
         */

        Map<String, Integer> hashMap = new HashMap<>();

        // Métodos básicos de HashMap:
        hashMap.put("Uno", 1); // Añadir elemento
        hashMap.put("Dos", 2);
        hashMap.put("Tres", 3);
        hashMap.put("Cuatro", 4);
        hashMap.put("Cinco", 5);

        System.out.println(RED + "Elementos en HashMap:" + RESET);
        for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
            System.out.println(entry.getKey() + " => " + entry.getValue());
        }

        System.out.println(GREEN + "\nMétodos básicos de HashMap:" + RESET);
        System.out.println("hashMap.size() = " + hashMap.size()); // Número de elementos
        System.out.println("hashMap.containsKey(\"Tres\") = " + hashMap.containsKey("Tres")); // Ver si contiene clave
        System.out.println("hashMap.get(\"Cuatro\") = " + hashMap.get("Cuatro")); // Obtener valor por clave
        hashMap.remove("Cinco"); // Eliminar elemento
        System.out.println("Después de remove(\"Cinco\"), hashMap = " + hashMap);

        System.out.println(BLUE + "\n==== LINKEDHASHMAP ====" + RESET);

        /*
         * LinkedHashMap es similar a HashMap, pero mantiene el orden de inserción de los elementos.
         * Características:
         * - Mantiene orden de inserción (o acceso si se usa modo acceso-orden).
         * - Permite null como clave y múltiples null como valores.
         */

        Map<String, Integer> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("Uno", 1);
        linkedHashMap.put("Dos", 2);
        linkedHashMap.put("Tres", 3);
        linkedHashMap.put("Cuatro", 4);
        linkedHashMap.put("Cinco", 5);

        System.out.println(RED + "Elementos en LinkedHashMap:" + RESET);
        for (Map.Entry<String, Integer> entry : linkedHashMap.entrySet()) {
            System.out.println(entry.getKey() + " => " + entry.getValue());
        }

        System.out.println(GREEN + "\nMétodos básicos de LinkedHashMap:" + RESET);
        System.out.println("linkedHashMap.size() = " + linkedHashMap.size());
        System.out.println("linkedHashMap.containsKey(\"Tres\") = " + linkedHashMap.containsKey("Tres"));
        System.out.println("linkedHashMap.get(\"Cuatro\") = " + linkedHashMap.get("Cuatro"));
        linkedHashMap.remove("Cinco");
        System.out.println("Después de remove(\"Cinco\"), linkedHashMap = " + linkedHashMap);

        /*
         * Conclusión:
         * - HashMap: rápido, no mantiene orden.
         * - LinkedHashMap: mantiene orden de inserción, ideal cuando se requiere recorrer los elementos en orden.
         */
    }
}
