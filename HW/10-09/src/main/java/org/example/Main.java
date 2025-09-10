package org.example;

import org.example.model.Cat;
import org.example.model.Dog;
import org.example.storage.ZooStorage;

public class Main
{
    public static void main(String[] args)
    {
        ZooStorage zoo = new ZooStorage();

        zoo.addCat(new Cat("Мурчик"));
        zoo.addDog(new Dog("Барсик"));

        // ClassToInstanceMap (кот, собака из хранилища)
        System.out.println("Кот: " + zoo.getCat());
        System.out.println("Собака: " + zoo.getDog());

        // ImmutableList
        System.out.println("Посетители: " + zoo.getVisitors());
    }
}

