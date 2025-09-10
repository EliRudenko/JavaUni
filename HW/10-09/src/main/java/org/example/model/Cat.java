package org.example.model;

public class Cat
{
    private final String name;

    public Cat(String name) { this.name = name; }

    public String getName() { return name; }

    @Override
    public String toString() { return "Cat{name='" + name + "'}"; }
}
