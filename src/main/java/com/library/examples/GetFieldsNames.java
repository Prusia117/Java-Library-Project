package com.library.examples;

import java.lang.reflect.Field;

/*
 *  Ejemplo para obtener los nombres de los atributos de un objeto en Java.
 *  Referencias: https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html
 */

 // Se declara una clase Test con tres atributos.
class Test {
    public String fieldA = "Hello!!";
    private String fieldB = "Hola!!";
    protected String fieldC = "Hallo!!";
}

public class GetFieldsNames extends Colors {
    public static void main(String[] args) {


        System.out.println(BLUE + "getDeclaredFields" + RESET);

        /* 
        * Field en palabras de Java es "A Field provides information about, and dynamic access to, a single field of a class or an interface".
        * En este caso se declara un array de Fields ya que un objeto puedo contener mas de un atributo.
        * .class es en palabras de Java: "Instances of the class Class represent classes and interfaces in a running Java application. ".
        * Por lo que podriamos decir que la clase Class es uno de los hijos de Object el objeto superior en java, pero este Class es tipo generico.
        * En esta clase contiene algunos metodos como getDeclaredFields que devuelve un array de tipo Field;
        * Con todos los atributos declarados por la clase o interfaz sin importar su modificador de acceso.
        */
        Field[] allFields = Test.class.getDeclaredFields();

        System.out.println(RED + "Atributos completos" + RESET);
        /*
         * A su vez field tiene otros metodos ya que es un objeto, en este caso si imprimos solo la variable nos devolvera todo la 
         * informacion del objeto por el metodo toString.
         */
        for (Field field : allFields) {
            System.out.println("Field: " + field); // <-- Toda la informacion del objeto
        }

        System.out.println(GREEN + "Solo el nombre de los atributos" + RESET);
        /*
         * Como field tiene otros metodos podemos acceder solo al nombre y no a la informacion completa del objeto, que puede ser para 
         * nosotros irrelevante.
         */
        for (Field field : allFields) {
            System.out.println("FieldName: " + field.getName()); // <-- Solo el nombre
        }

        System.out.println(BLUE + "getFields" + RESET);
        /*
         * Ejemplo pero con getFields que a diferencia de getDeclaredFields solo devuelve los atributos con modificador publico.
         */

        Field[] publicFields = Test.class.getFields();

        System.out.println(RED + "Atributos completos" + RESET);
        for (Field field : publicFields) {
            System.out.println("Field: " + field); // <-- Toda la informacion del objeto
        }

        Test t = new Test();

        System.out.println(GREEN +"Solo el nombre de los matributos" + RESET);
        for (Field field : publicFields) {
            System.out.println("FieldName: " + field.getName()); // <-- Solo el nombre
            Object value = field.get(t);
        }
    }

}