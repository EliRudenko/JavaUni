package main;

import models.Cat;
import comparators.CatNameLengthComparator;

import java.util.ArrayList;
import java.util.List;

public class Main
{
    public static void main(String[] args)
    {
        List<Cat> cats = new ArrayList<>();
        cats.add(new Cat("Murchyk"));
        cats.add(new Cat("Pyshok"));
        cats.add(new Cat("Barsik"));
        cats.add(new Cat("Tom"));
        cats.add(new Cat("Kitty"));

        System.out.println("котики:");
        cats.forEach(System.out::println);

        System.out.println("\n____Stream API");
        // имя > 4 символів
        // sort через comparator длина имени котиков
        // sort алфавит реверс для интереса
        // имя нв К

        System.out.println("\nимя > 4 символів, sort длина имени котиков:");
        cats.stream()
                .filter(c -> c.name.length() > 4)
                .sorted(new CatNameLengthComparator())
                .forEach(System.out::println);


        System.out.println("\nреверс алфавита:");
        cats.stream()
                .sorted((c1, c2) -> c2.name.compareTo(c1.name))
                .forEach(System.out::println);


        System.out.println("\nимя моего китика:");
        cats.stream()
                .filter(c -> c.name.startsWith("K"))
                .forEach(System.out::println);
    }
}
