package com.library.examples;

import com.library.kernel.classes.Colors;

import java.util.ArrayList;
import java.util.List;
/*
 * Ejemplo práctico para entender Generics en Java.
 *
 * Referencias:
 * https://docs.oracle.com/javase/tutorial/java/generics/
 */

// Clase simple sin generics
class Box {
    private Object value; // Usando Object -> no hay seguridad de tipo

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}

// Clase genérica que reemplaza Object por un tipo T
class GenericBox<T> {
    private T value; // T será determinado cuando se instancie la clase

    public void setValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}

// Clase genérica con restricción: solo acepta números
class NumberBox<T extends Number> {
    private T value;

    public NumberBox(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public double doubleValue() {
        return value.doubleValue(); // Podemos usar métodos de Number
    }
}

public class Generics implements Colors {

    public static void main(String[] args) {

        System.out.println(BLUE + "Ejemplo sin Generics (usando Object)" + RESET);
        Box box = new Box();
        box.setValue("Hola mundo"); // Guardamos un String
        String value1 = (String) box.getValue(); // Necesitamos casting
        System.out.println("Value: " + value1);

        System.out.println(GREEN + "Ejemplo con Generics" + RESET);
        GenericBox<String> genericBox = new GenericBox<>();
        genericBox.setValue("Hola Generics"); // Ahora no necesitamos casting
        String value2 = genericBox.getValue();
        System.out.println("Value: " + value2);

        GenericBox<Integer> intBox = new GenericBox<>();
        intBox.setValue(42); // También podemos usarlo con Integer
        int value3 = intBox.getValue();
        System.out.println("Value: " + value3);

        System.out.println(RED + "Ejemplo con restricción de tipo (NumberBox<T extends Number>)" + RESET);
        NumberBox<Integer> intNumberBox = new NumberBox<>(100);
        System.out.println("Integer as double: " + intNumberBox.doubleValue());

        NumberBox<Double> doubleNumberBox = new NumberBox<>(99.99);
        System.out.println("Double as double: " + doubleNumberBox.doubleValue());

        // Esto daría error de compilación:
        // NumberBox<String> stringBox = new NumberBox<>("Hola");
        // String no extiende Number

        System.out.println(BLUE + "Ejemplo con Listas Genéricas" + RESET);
        List<String> names = new ArrayList<>(); // Lista genérica solo de Strings
        names.add("Alice");
        names.add("Bob");
        // names.add(123); // Error de compilación: seguridad de tipo
        for (String name : names) {
            System.out.println("Name: " + name);
        }

        System.out.println(GREEN + "Ejemplo con métodos genéricos" + RESET);
        System.out.println("Suma de enteros: " + sum(10, 20));
        System.out.println("Suma de doubles: " + sum(5.5, 4.5));
    }

    // Método genérico <T> que funciona con cualquier tipo de Number
    public static <T extends Number> double sum(T a, T b) {
        return a.doubleValue() + b.doubleValue();
    }
}