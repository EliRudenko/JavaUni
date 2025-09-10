package models;

public class Cat
{
    public String name;

    public Cat(String name) { this.name = name; }

    @Override
    public String toString() { return name; }
}
