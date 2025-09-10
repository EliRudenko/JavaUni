package org.example.model;

public class Dog
{
    private final String name;

    public Dog(String name) { this.name = name; }

    public String getName() { return name; }

    @Override
    public String toString() { return "Dog{name='" + name + "'}"; }
}
