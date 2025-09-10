package main;

import models.Cat;
import comparators.CatNameLengthComparator;

import java.util.PriorityQueue;

public class Main
{
    public static void main(String[] args)
    {
        PriorityQueue<Cat> q = new PriorityQueue<>(new CatNameLengthComparator().reversed());

        Cat a = new Cat("Murchyk");
        Cat b = new Cat("Vaska");
        Cat c = new Cat("Barsik");

        q.add(a);
        q.add(b);
        q.add(c);

        System.out.println("Всех добавили: " + q);

        Cat first = q.poll();
        System.out.println("Первый покинул чат: " + first);

        Cat s = q.poll();
        System.out.println("Первый покинул чат: " + s);

        System.out.println("Посел удаления: " + q);
    }
}
