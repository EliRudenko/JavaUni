package org.example;



import org.example.animals.Giraffe;
import org.example.annotations.Mammal;
import org.example.annotations.Habitat;

import java.lang.annotation.Annotation;

public class Main {
    public static void main(String[] args)
    {
        Giraffe g = new Giraffe();
        Class<?> clazz = g.getClass();

        System.out.println("______аннотации  Giraffe:");
        Annotation[] annotations = clazz.getAnnotations();
        for (Annotation annotation : annotations) { System.out.println(annotation); }

        // Mammal
        if (clazz.isAnnotationPresent(Mammal.class))
        {
            Mammal m = clazz.getAnnotation(Mammal.class);
            System.out.println("\nMammal:");
            System.out.println("  sound = " + m.sound());
            System.out.println("  color = " + m.color());
        }

        // Habitat (Repeatable)
        if (clazz.isAnnotationPresent(Habitat.class) || clazz.isAnnotationPresent(org.example.annotations.Habitats.class))
        {
            Habitat[] habitats = clazz.getAnnotationsByType(Habitat.class);
            System.out.println("\nHabitats:");
            for (Habitat h : habitats) { System.out.println("  place = " + h.place()); }
        }
    }
}
